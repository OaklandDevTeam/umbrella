package com.umbr3114.controllers;

import com.mongodb.MongoWriteException;
import com.mongodb.client.model.Filters;
import com.umbr3114.Main;
import com.umbr3114.ServiceLocator;
import com.umbr3114.auth.PermissionChecker;
import com.umbr3114.auth.PermissionCheckProvider;
import com.umbr3114.common.GeneralResponse;
import com.umbr3114.common.RequestParamHelper;
import com.umbr3114.data.CollectionFactory;
import com.umbr3114.models.DropListingModel;
import com.umbr3114.models.DropModel;
import org.bson.types.ObjectId;
import org.eclipse.jetty.http.HttpStatus;
import org.mongojack.JacksonMongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.halt;

public class DropController {

    public static Route addDrop = ((request, response) -> {
        String user;
        String title;
        String topic;
        JacksonMongoCollection<DropModel> dropCollection;
        DropModel drop;
        RequestParamHelper params = new RequestParamHelper(request);

        user = params.valueOf("owner");
        title = params.valueOf("title");
        topic = params.valueOf("topic");


        if (user == null || user.isEmpty() || user.contains(" "))
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.OK_200, "invalid parameters").toJSON());

        if(title == null || title.isEmpty() || title.contains(" "))
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.OK_200, "invalid parameters").toJSON());

        if(topic == null || topic.isEmpty())
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.OK_200, "invalid parameters").toJSON());


        drop = new DropModel();
        drop.owner = user;
        drop.title = title;
        drop.topic = topic;

        dropCollection = new CollectionFactory<DropModel>(ServiceLocator.getService().dbService(), DropModel.class).getCollection();

        try {
            dropCollection.save(drop);
        } catch (MongoWriteException e) {
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400, "drop already exists").toJSON());
        }

        return new GeneralResponse(HttpStatus.OK_200, "successful");


    });

    public static Route manageModerator = ((request, response) -> {
        String targetUserId; // ID of the user being blessed to moderator
        String targetUserName; // name of the user being blessed to moderator
        String dropId;
        String requestType = request.requestMethod();

        PermissionChecker permissionChecker;
        JacksonMongoCollection<DropModel> collection;
        RequestParamHelper params = new RequestParamHelper(request);
        collection = new CollectionFactory<DropModel>(Main.services.dbService(), DropModel.class).getCollection();

        targetUserId = params.valueOf("user_id");
        targetUserName = params.valueOf("username");
        dropId = params.valueOf("drop_id");

        // null checks
        if (targetUserId == null || dropId == null) {
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400,
                    "incorrect params").toJSON());
        }

        // check we have permission
        permissionChecker = new PermissionChecker(request, new DropPermissionCheckProvider(collection, dropId));
        if (!permissionChecker.verify()) {
            halt(HttpStatus.FORBIDDEN_403, new GeneralResponse(HttpStatus.FORBIDDEN_403,
                    "incorrect permissions").toJSON());
        }

        if (requestType.equals("POST") && targetUserName == null) {
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400,
                    "incorrect params").toJSON());
        }

        try {
            switch (requestType) {
                case "POST":
                    addModerator(collection, targetUserId, targetUserName, dropId);
                    break;
                case "DELETE":
                    removeModerator(collection, targetUserId, dropId);
                    break;
                default:
                    halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400, "unsupported request").toJSON());
            }
        } catch(IllegalArgumentException e) {
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400,
                    e.getMessage()).toJSON());
        }

        return new GeneralResponse(HttpStatus.OK_200, "successful");
    });

    public static Route listDrops = ((request, response) -> {

        JacksonMongoCollection<DropModel> collection = new CollectionFactory<DropModel>(Main.services.dbService(), DropModel.class)
                .getCollection();
        List<DropListingModel> dropModels = new ArrayList<>();
        collection.find().forEach(model -> {
            dropModels.add(new DropListingModel(model._id.toString(), model.title, model.topic));
        });

        return dropModels;
    });

    /**
     * This method adds a moderator to a specific drop record
     * @param collection The collection object for this request
     * @param userId The target userId
     * @param username The target username
     * @param dropId The target drop
     */
    static void addModerator(JacksonMongoCollection<DropModel> collection, String userId, String username, String dropId) {
        DropModel.ModeratorModel modModel = new DropModel.ModeratorModel(username, userId);
        DropModel dropModel;

        dropModel = collection.findOne(Filters.eq("_id", new ObjectId(dropId)));

        if (dropModel == null) {
            throw new IllegalArgumentException("drop does not exist");
        }

        if (dropModel.moderators == null) {
            dropModel.moderators = new ArrayList<>();
        } else {
            // check for duplicate
            if (dropModel.moderators.contains(modModel))
                throw new IllegalArgumentException("moderator already in list");
        }
        dropModel.moderators.add(modModel);

        collection.findOneAndReplace(Filters.eq("_id", new ObjectId(dropId)), dropModel);
    }

    /**
     * This method removes a moderator to a specific drop record
     * @param collection The collection object for this request
     * @param userId The target userId
     * @param dropId The target username
     */
    static void removeModerator(JacksonMongoCollection<DropModel> collection, String userId, String dropId) {
        Logger log = LoggerFactory.getLogger("ModManager");
        DropModel.ModeratorModel modModel;
        DropModel dropModel;

        // here a username is not required since it is not compared
        modModel = new DropModel.ModeratorModel(null, userId);

        dropModel = collection.findOne(Filters.eq("_id", new ObjectId(dropId)));

        if (dropModel == null)
            throw new IllegalArgumentException("drop does not exist");

        if (dropModel.moderators == null)
            return;

        if (!dropModel.moderators.remove(modModel)) {
            log.info("Moderator not in list of moderators");
        }
        collection.findOneAndReplace(Filters.eq("_id", new ObjectId(dropId)), dropModel);
    }

    public static class DropPermissionCheckProvider extends PermissionCheckProvider {
        private DropModel model;

        public DropPermissionCheckProvider(JacksonMongoCollection<DropModel> collection, String dropId) {
            model = collection.findOne(Filters.eq("_id", new ObjectId(dropId)));
        }

        public DropPermissionCheckProvider(DropModel model) {
            this.model = model;
        }

        @Override
        public DropModel provideDropModel() {
            return model;
        }

        @Override
        public String provideOwnerId() {
            if (model == null)
                return null;
            return model.owner;
        }
    }
}
