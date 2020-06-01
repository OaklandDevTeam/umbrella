package com.umbr3114.controllers;

import com.umbr3114.ServiceLocator;
import com.umbr3114.auth.PermissionCheckProvider;
import com.umbr3114.common.RequestParamHelper;
import com.umbr3114.data.CollectionFactory;
import com.umbr3114.models.DropModel;
import com.umbr3114.models.PostModel;
import org.mongojack.JacksonMongoCollection;
import spark.Route;

public class PostController {
    public static Route savePosts = ((request, response) -> {
        JacksonMongoCollection<PostModel> mongoCollection =
                new CollectionFactory<PostModel>(ServiceLocator.getService().dbService(), PostModel.class).getCollection();
        PostModel postModel;
        RequestParamHelper params = new RequestParamHelper(request);

        String title = params.valueOf("title");
        String bodyText = params.valueOf("bodyText");
        String authorId = params.valueOf("authorId");
        String dropId = params.valueOf("drop_id");

        if(title == null || bodyText == null || authorId == null || dropId == null){
            return "bad";
        }
        postModel = new PostModel(title, bodyText, authorId, dropId);
        mongoCollection.save(postModel);

        return "ok";
    });
}
