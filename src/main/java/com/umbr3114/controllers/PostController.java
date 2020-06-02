package com.umbr3114.controllers;

import com.umbr3114.ServiceLocator;
import com.umbr3114.common.GeneralResponse;
import com.umbr3114.auth.PermissionCheckProvider;
import com.umbr3114.common.RequestParamHelper;
import com.umbr3114.data.CollectionFactory;
import com.umbr3114.models.DropModel;
import com.umbr3114.models.PostModel;
import org.eclipse.jetty.http.HttpStatus;
import org.mongojack.JacksonMongoCollection;
import spark.Route;

import java.util.Set;

import static spark.Spark.halt;

public class PostController {


    public static final int DEFAULT_POST_LOAD_LIMIT = 15;

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

    public static Route listPosts = ((request, response) -> {
        String dropName = request.params("drop");
        String listAfterId = request.queryParamOrDefault("startAfter", "*");
        String limitRaw = request.queryParamOrDefault("limit", "");
        int limit;

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


        return "";
    });
}
