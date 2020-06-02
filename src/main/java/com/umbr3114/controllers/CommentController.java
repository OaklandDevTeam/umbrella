package com.umbr3114.controllers;

import com.mongodb.Function;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.*;
import com.umbr3114.ServiceLocator;
import com.umbr3114.auth.UserModel;
import com.umbr3114.data.CollectionFactory;
import com.umbr3114.models.CommentModel;
import org.mongojack.JacksonMongoCollection;
import spark.Route;
import com.mongodb.client.model.Filters;

import java.util.Collection;

import static com.mongodb.client.model.Filters.and;

public class CommentController {
    public static int startAfter = 10;
    private CommentModel commentModel;
    private UserModel userModel;
    private static JacksonMongoCollection<UserModel> userCollection;

    public static Route saveComments = ((request, response) -> {
        JacksonMongoCollection<CommentModel> mongoCollection =
                new CollectionFactory<CommentModel>(ServiceLocator.getService().dbService(), CommentModel.class).getCollection();
        CommentModel commentModel;
        String bodyText = request.queryParams("bodyText");
        String authorId = request.queryParams("authorId");
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
        //CommentModel commentModel = new CommentModel();
        MongoIterable<CommentModel> mongoIterable = new MongoIterable<CommentModel>() {
            @Override
            public MongoCursor<CommentModel> iterator() {
                return null;
            }

            @Override
            public MongoCursor<CommentModel> cursor() {
                return null;
            }

            @Override
            public CommentModel first() {
                return null;
            }

            @Override
            public <U> MongoIterable<U> map(Function<CommentModel, U> mapper) {
                return null;
            }

            @Override
            public <A extends Collection<? super CommentModel>> A into(A target) {
                return null;
            }

            @Override
            public MongoIterable<CommentModel> batchSize(int batchSize) {
                return null;
            }
        };
        //return mongoIterable.find(Filters.eq("post_id")).batchSize(10);
        //return mongoCollection.findOne(Filters.eq("comments", commentModel));
        return mongoIterable.batchSize(startAfter);
    });

    public static Route updateComments = ((request, response) -> {
        JacksonMongoCollection<CommentModel> mongoCollection =
                new CollectionFactory<CommentModel>(ServiceLocator.getService().dbService(), CommentModel.class).getCollection();
        //private CommentModel fetchCommentModel(String postId) { return mongoCollection.findOne(Filters.eq("post_id", postId)); }
        //mongoCollection.updateById(Filters.eq("post_id", postId));
        //private UserModel fetchUserModel(String user) {
          //  return userCollection.findOne(Filters.eq("username", user));
        //}
        String commentId = request.queryParams("_id");
        String bodyText = request.queryParams("bodyText");
        if(request.queryParams("authorId").equals(mongoCollection.findOneById(commentId).getAuthorId())){
            mongoCollection.findOneById(commentId).setBodyText(bodyText);
            return " ok ";
        }

        return " not ok ";
    });

    public static Route destroyComments = ((request, response) -> {
        JacksonMongoCollection<CommentModel> mongoCollection =
                new CollectionFactory<CommentModel>(ServiceLocator.getService().dbService(), CommentModel.class).getCollection();
        String user = request.queryParams("user");
        String commentId = request.queryParams("_id");
        // TODO fix conditional and delete
        if(user.equals("dev")){
            //mongoCollection.deleteOne();
            return " ok ";
        }
        return " not ok ";
    });
}
