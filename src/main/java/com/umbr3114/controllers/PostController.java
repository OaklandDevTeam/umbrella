package com.umbr3114.controllers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.umbr3114.Main;
import com.umbr3114.ServiceLocator;
import com.umbr3114.auth.PermissionCheckProvider;
import com.umbr3114.auth.PermissionChecker;
import com.umbr3114.auth.SessionManager;
import com.umbr3114.auth.SparkSessionManager;
import com.umbr3114.common.GeneralResponse;
import com.umbr3114.common.PostScore;
import com.umbr3114.common.RequestParamHelper;
import com.umbr3114.data.CollectionFactory;
import com.umbr3114.models.DropModel;
import com.umbr3114.models.PostListingModel;
import com.umbr3114.models.PostModel;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.eclipse.jetty.http.HttpStatus;
import org.mongojack.JacksonMongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static spark.Spark.halt;

public class PostController {


    public static final int DEFAULT_POST_LOAD_LIMIT = 25;
    public static final int POST_LOAD_MAX_LIMIT = 100;
    public static final String DEFAULT_SORT = "scoreHour";

    public static Route savePosts = ((request, response) -> {
        JacksonMongoCollection<PostModel> mongoCollection =
                new CollectionFactory<PostModel>(ServiceLocator.getService().dbService(), PostModel.class).getCollection();
        PostModel postModel;
        RequestParamHelper params = new RequestParamHelper(request);
        SessionManager session = new SparkSessionManager(request);

        String title = params.valueOf("title");
        String bodyText = params.valueOf("body");
        String authorId = session.getCurrentUserId();
        String author = session.getCurrentUsername();
        String dropId = params.valueOf("drop_id");

        if (title == null || bodyText == null || authorId == null || dropId == null || author == null) {
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.OK_200, "invalid request").toJSON());
        }


        postModel = new PostModel();
        postModel.setAuthorId(authorId);
        postModel.setBodyText(bodyText);
        postModel.setTitle(title);
        postModel = new PostModel(title, bodyText, authorId, dropId);
        postModel.author = author;
        postModel.createdDate = Date.from(Instant.now());

        PostScore.calculateScoreForPostModel(postModel, System.currentTimeMillis());

        mongoCollection.save(postModel);

        return new GeneralResponse(HttpStatus.OK_200, "added");
    });

    public static Route listPosts = ((request, response) -> {
        String dropId = request.params("drop");
        String offsetString = request.queryParamOrDefault("offset", "0");
        String limitString = request.queryParamOrDefault("limit", Integer.toString(DEFAULT_POST_LOAD_LIMIT));
        String sortBy = request.queryParamOrDefault("sort", DEFAULT_SORT);
        PostListingModel listingModel;
        List<PostModel> postModels;
        int limit;
        int offset;

        // sanity check inputs
        if (dropId == null) {
            halt(HttpStatus.NOT_FOUND_404, new GeneralResponse(HttpStatus.NOT_FOUND_404,
                    "how did you get here?").toJSON());
        }

        try{
            limit = Integer.parseInt(limitString);
        } catch (NumberFormatException e) {
            limit = DEFAULT_POST_LOAD_LIMIT;
        }

        try {
            offset = Integer.parseInt(offsetString);
        } catch (NumberFormatException e) {
            offset = 0;
        }

        if (limit > POST_LOAD_MAX_LIMIT || limit < 0) {
            limit = POST_LOAD_MAX_LIMIT;
        }

        if (!ObjectId.isValid(dropId)) { // in the case that a drop title was passed to lookup
            dropId = lookupDropId(dropId);
        }

        if (dropId == null) {
            halt(HttpStatus.NOT_FOUND_404, new GeneralResponse(HttpStatus.NOT_FOUND_404,
                    "drop does not exist").toJSON());
        }

        postModels = loadPosts(dropId, offset, limit, sortBy);

        // package results
        listingModel = new PostListingModel();
        listingModel.count = postModels.size();
        listingModel.offset = offset;
        listingModel.dropId = dropId;
        listingModel.posts = postModels;

        return listingModel;
    });

    private static List<PostModel> loadPosts(String dropId, int skip, int limit, String sortBy) {
        Logger log = LoggerFactory.getLogger("PostLoader");
        JacksonMongoCollection<PostModel> collection = new CollectionFactory<PostModel>(Main.services.dbService(), PostModel.class)
                .getCollection();
        FindIterable<PostModel> iterable;
        List<PostModel> models = new ArrayList<>();

        iterable = collection.find(Filters.eq("dropId", dropId));

        // handle sort option
        switch (sortBy) {
            case "new":
                iterable.sort(Sorts.descending("createdDate"));
                break;
            case "scoreDay":
            case "scoreThreeDay":
            case "scoreWeek":
            case "scoreHour":
                iterable.sort(Sorts.descending(sortBy));
                break;
            default:
                iterable.sort(Sorts.descending(DEFAULT_SORT));
        }
        iterable.skip(skip);
        iterable.limit(limit);
        iterable.into(models);
        return models;
    }

    private static String lookupDropId(String dropName) {
        JacksonMongoCollection<DropModel> dropCollection;
        DropModel model;

        dropCollection = new CollectionFactory<DropModel>(
                ServiceLocator.getService().dbService(),
                DropModel.class
        ).getCollection();

        model = dropCollection.findOne(Filters.eq("title", dropName));

        if (model == null) {
            return null;
        }

        return model._id.toString();
    }

    /*
     * to modify posts
     */
    public static Route modifyPosts = ((request, response) -> {
        PermissionChecker myPermissionChecker;
        PostModel postModel;

        //collection to store the modified posts
        JacksonMongoCollection<PostModel> updateCollection = new CollectionFactory<PostModel>
                (ServiceLocator.getService().dbService(), PostModel.class).getCollection();

        RequestParamHelper params = new RequestParamHelper(request);
        String post_Id = params.valueOf("post_id");
        String body_text = params.valueOf("body");


        postModel = updateCollection.findOne(eq("_id", new ObjectId(post_Id)));
        if (postModel == null) {
            halt(HttpStatus.FORBIDDEN_403,
                    new GeneralResponse(HttpStatus.FORBIDDEN_403, "Post doesn't exist or can't be modified")
                            .toJSON());
        }
        myPermissionChecker = new PermissionChecker(request, new PostPermissionCheckProvider(postModel));
        if (!myPermissionChecker.verify()) {
            halt(HttpStatus.FORBIDDEN_403,
                    new GeneralResponse(HttpStatus.FORBIDDEN_403, "unauthorized")
                            .toJSON());
        }
        //check if the post_id exists in the database
        if (post_Id == null) {
            halt(HttpStatus.FORBIDDEN_403,
                    new GeneralResponse(HttpStatus.FORBIDDEN_403, "Post doesn't exist")
                            .toJSON());
        }
        //modify the body
        postModel.bodyText = body_text;
        //find the post by its id and update the body of the post
        postModel.editedDate = Date.from(Instant.now());
        updateCollection.findOneAndReplace(eq("_id", new ObjectId(post_Id)), postModel);
        return new GeneralResponse(HttpStatus.OK_200, "modified");

    });
    /*
     * to delete posts
     */
    public static Route deletePosts = ((request, response) -> {
        PostModel postModel;
        DeleteResult result;
        PermissionChecker permissionChecker;
        RequestParamHelper params = new RequestParamHelper(request);
        String post_Id = params.valueOf("post_id");

        JacksonMongoCollection<PostModel> deleteCollection = new CollectionFactory<PostModel>
                (ServiceLocator.getService().dbService(), PostModel.class).getCollection();

        //check if the post exist
        postModel = deleteCollection.findOne(eq("_id", new ObjectId(post_Id)));
        //check the user's permission
        permissionChecker = new PermissionChecker(request, new PostPermissionCheckProvider(postModel));
        if (!permissionChecker.verify()) {
            halt(HttpStatus.FORBIDDEN_403,
                    new GeneralResponse(HttpStatus.FORBIDDEN_403, "unauthorized`.")
                            .toJSON());
        }
        //delete the post
        result = deleteCollection.deleteOne(eq("idString", post_Id));
        if (result.getDeletedCount() == 0) {
            halt(HttpStatus.FORBIDDEN_403,
                    new GeneralResponse(HttpStatus.FORBIDDEN_403, "Post doesn't exist.")
                            .toJSON());
        }
        return new GeneralResponse(HttpStatus.OK_200, "deleted");
    });

    /*
     * permission check for the endpoints to modify/delete posts
     */
    public static class PostPermissionCheckProvider extends PermissionCheckProvider {
        private PostModel postModel;

        //find the dropId
        public PostPermissionCheckProvider(JacksonMongoCollection<PostModel> collection, String dropId) {
            postModel = collection.findOne(Filters.eq("_id", new ObjectId(dropId)));
        }

        public PostPermissionCheckProvider(PostModel postModel) {
            this.postModel = postModel;
        }

        //get the dropModel
        @Override
        public DropModel provideDropModel() {
            JacksonMongoCollection<DropModel> collection = new CollectionFactory<DropModel>(Main.services.dbService(),
                    DropModel.class).getCollection();
            DropModel dropModel = collection.findOne(eq("_id", new ObjectId(postModel.dropId)));
            return dropModel;
        }

        //get the authorId
        @Override
        public String provideOwnerId() {
            if (postModel == null) {
                return null;
            }
            return postModel.authorId;
        }
    }

    /*
     * to view the data about a single post
     */
    public static Route viewAPost = ((request, response) -> {

        String postId;
        PostModel postModel;

        postId = request.params("postid");
        if (postId == null) {
            halt(HttpStatus.FORBIDDEN_403, new GeneralResponse(HttpStatus.FORBIDDEN_403,
                    "post doesn't exist.").toJSON());
        }
        JacksonMongoCollection<PostModel> postModelCollection = new CollectionFactory<PostModel>(Main.services.dbService(),
                PostModel.class).getCollection();

        //retrieve data from the PostModel
        postModel = postModelCollection.findOne(eq("_id", new ObjectId(postId)));
        if (postModel == null) {
            halt(HttpStatus.FORBIDDEN_403, new GeneralResponse(HttpStatus.FORBIDDEN_403,
                    "post doesn't exist").toJSON());
        }
        return postModel;
    });

}
