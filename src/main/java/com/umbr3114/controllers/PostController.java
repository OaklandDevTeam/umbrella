package com.umbr3114.controllers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.umbr3114.Main;
import com.umbr3114.ServiceLocator;
import com.umbr3114.auth.SessionManager;
import com.umbr3114.auth.SparkSessionManager;
import com.umbr3114.common.GeneralResponse;
import com.umbr3114.common.RequestParamHelper;
import com.umbr3114.data.CollectionFactory;
import com.umbr3114.models.DropModel;
import com.umbr3114.models.PostListingModel;
import com.umbr3114.models.PostModel;
import org.bson.types.ObjectId;
import org.eclipse.jetty.http.HttpStatus;
import org.mongojack.JacksonMongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.halt;

public class PostController {


    public static final int DEFAULT_POST_LOAD_LIMIT = 25;
    public static final int POST_LOAD_MAX_LIMIT = 100;

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

        if(title == null || bodyText == null || authorId == null || dropId == null || author == null){
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.OK_200, "invalid request").toJSON());
        }
        postModel = new PostModel();
        postModel.setAuthorId(authorId);
        postModel.setBodyText(bodyText);
        postModel.setTitle(title);
        postModel = new PostModel(title, bodyText, authorId, dropId);
        postModel.author = author;
        mongoCollection.save(postModel);

        return new GeneralResponse(HttpStatus.OK_200, "added");
    });

    public static Route listPosts = ((request, response) -> {
        String dropName = request.params("drop");
        String listAfterId = request.queryParamOrDefault("startAfter", "*");
        String limitRaw = request.queryParamOrDefault("limit", "");
        PostListingModel listingModel;
        List<PostModel> postModels;
        int limit;

        // sanity check inputs
        if (dropName == null) {
            halt(HttpStatus.NOT_FOUND_404, new GeneralResponse(HttpStatus.NOT_FOUND_404,
                    "how did you get here?").toJSON());
        }

        if (limitRaw.isEmpty()) {
            limit = DEFAULT_POST_LOAD_LIMIT;
        } else {
            try {
                limit = Integer.parseInt(limitRaw);
            } catch (NumberFormatException e) {
                limit = DEFAULT_POST_LOAD_LIMIT;
            }
        }

        // input validate limit
        if (limit > POST_LOAD_MAX_LIMIT) {
            limit = POST_LOAD_MAX_LIMIT;
        } else if (limit < 0) {
            limit = 0;
        }

        postModels = loadPosts(dropName, listAfterId, limit);

        // package results
        listingModel = new PostListingModel();
        listingModel.count = postModels.size();

        if (postModels.size() > 1) {
            listingModel.lastId = postModels.get(postModels.size() - 1)._id.toString();
            listingModel.dropId = postModels.get(0).dropId;
        }

        listingModel.posts = postModels;

        return listingModel;
    });

    private static List<PostModel> loadPosts(String dropTitle, String afterId, int limit) {
        Logger log = LoggerFactory.getLogger("PostLoader");
        JacksonMongoCollection<PostModel> collection = new CollectionFactory<PostModel>(Main.services.dbService(), PostModel.class)
                .getCollection();
        JacksonMongoCollection<DropModel> dropCollection = new CollectionFactory<DropModel>(Main.services.dbService(), DropModel.class).getCollection();
        DropModel drop;
        String dropId;

        FindIterable<PostModel> iterable;
        List<PostModel> models = new ArrayList<>();

        // retrieve drop ID
        drop = dropCollection.findOne(Filters.eq("title", dropTitle));
        if (drop == null) {
            log.info("Drop doesn't exist, returning blank list");
            return models;
        }
        dropId = drop._id.toString();

        // run query to get listing
        if (afterId.equals("*")) {
            iterable = collection.find(Filters.eq("dropId", dropId));
        } else {
            try {
                // this query uses the "gt" filter to pull in id's greater than the "afterId"
                iterable = collection.find(Filters.and(Filters.eq("dropId", dropId), Filters.gt("_id", new ObjectId(afterId))));
            } catch (IllegalArgumentException e) {
                return models;
            }
        }
        iterable.limit(limit);
        iterable.into(models);

        return models;
    }
}
