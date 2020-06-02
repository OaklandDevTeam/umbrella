package com.umbr3114.controllers;

import com.umbr3114.ServiceLocator;
import com.umbr3114.data.CollectionFactory;
import com.umbr3114.models.PostModel;
import org.mongojack.JacksonMongoCollection;
import spark.Route;

public class PostController {
    public static Route savePosts = ((request, response) -> {
        JacksonMongoCollection<PostModel> mongoCollection =
                new CollectionFactory<PostModel>(ServiceLocator.getService().dbService(), PostModel.class).getCollection();

        PostModel postModel;
        String title = request.queryParams("title");
        String bodyText = request.queryParams("bodyText");
        String authorId = request.queryParams("authorId");
        if((title.isEmpty() || title == null) && (bodyText.isEmpty() || bodyText == null) && (authorId.isEmpty() || authorId == null)){
            return "bad";
        }
        postModel = new PostModel();
        postModel.setAuthorId(authorId);
        postModel.setBodyText(bodyText);
        postModel.setTitle(title);
        mongoCollection.save(postModel);

        return "ok";
    });
}
