package com.umbr3114.controllers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import com.umbr3114.ServiceLocator;
import com.umbr3114.auth.*;
import com.umbr3114.common.GeneralResponse;
import com.umbr3114.common.RequestParamHelper;
import com.umbr3114.data.CollectionFactory;
import com.umbr3114.models.CommentListingModel;
import com.umbr3114.models.CommentModel;
import com.umbr3114.models.DropModel;
import com.umbr3114.models.PostModel;
import org.eclipse.jetty.http.HttpStatus;
import org.mongojack.JacksonMongoCollection;
import org.bson.types.ObjectId;
import spark.Route;
import com.mongodb.client.model.Filters;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static spark.Spark.halt;

import java.text.SimpleDateFormat;

public class CommentController {
    public static final int listLimit = 100;
    public static final int LIMIT_DEFAULT = 20;
    public static Route saveComments = ((request, response) -> {
        JacksonMongoCollection<CommentModel> mongoCollection =
                new CollectionFactory<CommentModel>(ServiceLocator.getService().dbService(), CommentModel.class).getCollection();
        CommentModel commentModel;
        SessionManager sessionManager = new SparkSessionManager(request);
        RequestParamHelper paramHelper = new RequestParamHelper(request);

        String bodyText = paramHelper.valueOf("bodyText");
        String authorId = sessionManager.getCurrentUserId();
        String postId = paramHelper.valueOf("postId");

        if(bodyText == null || authorId == null || postId == null){
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400, "param missing").toJSON());
        }
        commentModel = new CommentModel();
        commentModel.setPostId(postId);
        commentModel.setAuthorId(authorId);
        commentModel.setBodyText(bodyText);
        Date date = new java.util.Date(System.currentTimeMillis());
        commentModel.setDate(date);
        mongoCollection.save(commentModel);
        return new GeneralResponse(HttpStatus.OK_200, "saved");
    });

    public static Route getComments = ((request, response) -> {
        RequestParamHelper paramHelper = new RequestParamHelper(request);
        String postId = paramHelper.valueOf("postId");
        String startAfterId = paramHelper.valueOf("startAfterId");
        String limit = paramHelper.valueOf("limit");
        List<CommentModel> commentModelList;

        int lim;
        if(postId == null){
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400, "post id missing").toJSON());
        }

        if (limit == null) {
            lim = LIMIT_DEFAULT;
        } else {
            try {
                lim = Integer.parseInt(limit);
            } catch (NumberFormatException e) {
                lim = LIMIT_DEFAULT;
            }
        }
        commentModelList = getAfterId(postId, startAfterId, lim);

        CommentListingModel commentList = new CommentListingModel();
        commentList.count = commentModelList.size();
        if (commentModelList.size() > 0){
            commentList.lastId = commentModelList.get(commentModelList.size()-1)._id.toString();
            commentList.postId = commentModelList.get(0).getPostIdString();
        }
        commentList.comments = commentModelList;
        return commentList;
    });

    private static List<CommentModel> getAfterId(String postId, String startAfterId, int limit){
        JacksonMongoCollection<CommentModel> mongoCollectionComments =
                new CollectionFactory<CommentModel>(ServiceLocator.getService().dbService(), CommentModel.class).getCollection();
        FindIterable<CommentModel> iterable;
        List<CommentModel> commentModels = new ArrayList<>();

        if (startAfterId == null) {
            iterable = mongoCollectionComments.find(Filters.eq("post_id", postId));
        } else {
            try {
                iterable = mongoCollectionComments.find(Filters.and(Filters.eq("post_id", postId),
                        Filters.gt("_id", new ObjectId(startAfterId))));
            } catch (IllegalArgumentException e) {
                return commentModels;
            }
        }
        if(limit > listLimit){
            limit = listLimit;
        }
        iterable.limit(limit);
        iterable.into(commentModels);
        return commentModels;
    }

    public static Route updateComments = ((request, response) -> {
        JacksonMongoCollection<CommentModel> mongoCollection =
                new CollectionFactory<CommentModel>(ServiceLocator.getService().dbService(), CommentModel.class).getCollection();
        RequestParamHelper paramHelper = new RequestParamHelper(request);
        CommentModel commentModel;
        PermissionChecker permissionChecker;

        String commentId = paramHelper.valueOf("comment_id");
        String bodyText = paramHelper.valueOf("bodyText");

        if(commentId == null){
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400, "missing id").toJSON());
        }
        if(bodyText == null){
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400, "missing text").toJSON());
        }

        commentModel = mongoCollection.findOne(eq("_id", new ObjectId(commentId)));
        if (commentModel == null) {
            halt(HttpStatus.NOT_FOUND_404, new GeneralResponse(HttpStatus.NOT_FOUND_404, "comment doesn't exist").toJSON());
        }

        // perform permission check
        permissionChecker = new PermissionChecker(request, new CommentPermissionCheckProvider(commentModel));

        if (!permissionChecker.verify()) {
            halt(HttpStatus.FORBIDDEN_403, new GeneralResponse(HttpStatus.FORBIDDEN_403, "unauthorized").toJSON());
        }

        commentModel.setBodyText(bodyText);
        mongoCollection.findOneAndReplace(eq("_id", new ObjectId(commentId)), commentModel);
        return new GeneralResponse(HttpStatus.OK_200, "updated");
    });

    public static Route destroyComments = ((request, response) -> {
        JacksonMongoCollection<CommentModel> mongoCollection =
                new CollectionFactory<CommentModel>(ServiceLocator.getService().dbService(), CommentModel.class).getCollection();
        RequestParamHelper paramHelper = new RequestParamHelper(request);
        PermissionChecker permissionChecker;

        String commentId = paramHelper.valueOf("comment_id");
        if(commentId == null){
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400, "missing id").toJSON());
        }

        CommentModel commentModel = mongoCollection.findOne(eq("_id", new ObjectId(commentId)));
        if(commentModel == null){
            halt(HttpStatus.NOT_FOUND_404, new GeneralResponse(HttpStatus.NOT_FOUND_404, "comment does not exist").toJSON());
        }

        // perform permission check
        permissionChecker = new PermissionChecker(request, new CommentPermissionCheckProvider(commentModel));

        if (!permissionChecker.verify()) {
            halt(HttpStatus.FORBIDDEN_403, new GeneralResponse(HttpStatus.FORBIDDEN_403, "unauthorized").toJSON());
        }
        mongoCollection.deleteOne(eq("_id", new ObjectId(commentId)));
        return new GeneralResponse(HttpStatus.OK_200, "deleted");
    });

    public static class CommentPermissionCheckProvider extends PermissionCheckProvider {

        String commentOwnerId;
        String postId;

        public CommentPermissionCheckProvider(CommentModel commentModel) {

            if (commentModel == null) {
                throw new IllegalArgumentException("Comment model cannot be null");
            }

            if (commentModel.authorId == null || commentModel.postId == null) {
                throw new IllegalArgumentException("comment model has a null owner or post, which cannot happen");
            }

            this.postId = commentModel.postId;
            this.commentOwnerId = commentModel.authorId;
        }

        @Override
        public DropModel provideDropModel() {
            DropModel model;
            String dropId;
            JacksonMongoCollection<DropModel> collection
                    = new CollectionFactory<DropModel>(ServiceLocator.getService().dbService(), DropModel.class)
                    .getCollection();
            dropId = getDropIdFromParentPostModel(postId);

            model = collection.findOne(Filters.eq("_id", new ObjectId(dropId)));
            return model;
        }

        @Override
        public String provideOwnerId() {
            return commentOwnerId;
        }

        public String getDropIdFromParentPostModel(String id) {
            JacksonMongoCollection<PostModel> collection
                    = new CollectionFactory<PostModel>(ServiceLocator.getService().dbService(), PostModel.class)
                    .getCollection();

            PostModel model;
            model = collection.findOne(Filters.eq("_id", new ObjectId(id))); // todo this might have an exception thrown if the id is malformed

            if (model == null) {
                return null;
            }
            return model.dropId;
        }
    }
}
