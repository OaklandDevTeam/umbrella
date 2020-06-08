package com.umbr3114.controllers;

import com.mongodb.Function;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.*;
import com.umbr3114.ServiceLocator;
import com.umbr3114.auth.PermissionChecker;
import com.umbr3114.auth.SessionManager;
import com.umbr3114.auth.SparkSessionManager;
import com.umbr3114.auth.UserModel;
import com.umbr3114.common.RequestParamHelper;
import com.umbr3114.data.CollectionFactory;
import com.umbr3114.models.CommentListingModel;
import com.umbr3114.models.CommentModel;
import com.umbr3114.models.PostModel;
import org.mongojack.JacksonMongoCollection;
import org.bson.types.ObjectId;
import spark.Route;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;

public class CommentController {

    public static Route saveComments = ((request, response) -> {
        JacksonMongoCollection<CommentModel> mongoCollection =
                new CollectionFactory<CommentModel>(ServiceLocator.getService().dbService(), CommentModel.class).getCollection();
        CommentModel commentModel;
        SessionManager sessionManager = new SparkSessionManager(request);
        RequestParamHelper paramHelper = new RequestParamHelper(request);
        String bodyText = paramHelper.valueOf("bodyText");
        String authorId = sessionManager.getCurrentUserId();
        if((bodyText.isEmpty() || bodyText == null) && (authorId.isEmpty() || authorId == null)){
             return "bad";
        }
        commentModel = new CommentModel();
        commentModel.setAuthorId(authorId);
        commentModel.setBodyText(bodyText);
        mongoCollection.save(commentModel);
        return "ok";
    });

    public static Route getComments = ((request, response) -> {
        JacksonMongoCollection<CommentModel> mongoCollection =
                new CollectionFactory<CommentModel>(ServiceLocator.getService().dbService(), CommentModel.class).getCollection();
        RequestParamHelper paramHelper = new RequestParamHelper(request);
        String postId = paramHelper.valueOf("postId");
        String startAfterId = paramHelper.valueOf("startAfterId");
        List<CommentModel> commentModelList;
        if(postId == null){
            return "not ok";
        }
        commentModelList = getCommentList(postId, startAfterId);
        CommentListingModel commentList = new CommentListingModel();
        commentList.count = commentModelList.size();
        if (commentModelList.size() > 1){
            commentList.lastId = commentModelList.get(commentModelList.size()-1)._id.toString();
            commentList.postId = commentModelList.get(0).postId;
        }
        commentList.comments = commentModelList;
        return commentList;

    });

    private static List<CommentModel> getCommentList(String postId, String startAfterId){
        JacksonMongoCollection<CommentModel> mongoCollectionComments =
                new CollectionFactory<CommentModel>(ServiceLocator.getService().dbService(), CommentModel.class).getCollection();
        JacksonMongoCollection<PostModel> mongoCollectionPost =
                new CollectionFactory<PostModel>(ServiceLocator.getService().dbService(), PostModel.class).getCollection();
        FindIterable<CommentModel> iterable;
        PostModel posts = mongoCollectionPost.findOne(Filters.eq("postId", postId));
        List<CommentModel> commentModels = new ArrayList<>();
        if(posts == null){
            return commentModels;
        }else{
            iterable = mongoCollectionComments.find(Filters.and(Filters.eq("postId", postId), Filters.gt("_id", new ObjectId(startAfterId))));
        }
        iterable.limit(10);
        iterable.into(commentModels);
        return commentModels;
    }

    public static Route updateComments = ((request, response) -> {
        JacksonMongoCollection<CommentModel> mongoCollection =
                new CollectionFactory<CommentModel>(ServiceLocator.getService().dbService(), CommentModel.class).getCollection();
        SessionManager sessionManager = new SparkSessionManager(request);
        PermissionChecker permissionChecker;
        String commentId = request.queryParams("_id");
        String bodyText = request.queryParams("bodyText");
        if(sessionManager.getCurrentUserId().equals(mongoCollection.findOneById(commentId).getAuthorId())){
            mongoCollection.findOneById(commentId).setBodyText(bodyText);
            return " ok ";
        }

        return " not ok ";
    });

    public static Route destroyComments = ((request, response) -> {
        JacksonMongoCollection<CommentModel> mongoCollection =
                new CollectionFactory<CommentModel>(ServiceLocator.getService().dbService(), CommentModel.class).getCollection();
        JacksonMongoCollection<UserModel> mongoCollectionUser =
                new CollectionFactory<UserModel>(ServiceLocator.getService().dbService(), UserModel.class).getCollection();
        UserModel userModel;
        RequestParamHelper paramHelper = new RequestParamHelper(request);
        String user = paramHelper.valueOf("userId");
        userModel = mongoCollectionUser.findOne(Filters.eq("userId", user));
        String commentId = request.queryParams("_id");
        if(userModel.isAdmin()){
            mongoCollection.removeById(commentId);
            return " ok ";
        }
        return " not ok ";
    });
}
