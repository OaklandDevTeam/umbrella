package com.umbr3114.controllers;

import com.mongodb.MongoWriteException;
import com.mongodb.client.model.Filters;
import com.umbr3114.Main;
import com.umbr3114.ServiceLocator;
import com.umbr3114.auth.PermissionChecker;
import com.umbr3114.auth.PermissionCheckProvider;
import com.umbr3114.auth.SessionManager;
import com.umbr3114.auth.SparkSessionManager;
import com.umbr3114.common.GeneralResponse;
import com.umbr3114.common.RequestParamHelper;
import com.umbr3114.data.CollectionFactory;
import com.umbr3114.models.DropListingModel;
import com.umbr3114.models.DropModel;
import com.umbr3114.models.DropViewModel;
import com.umbr3114.models.PostModel;
import org.bson.types.ObjectId;
import org.eclipse.jetty.http.HttpStatus;
import org.mongojack.JacksonMongoCollection;
import spark.Route;
import static com.mongodb.client.model.Filters.eq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.halt;

public class DropController {

    public static Route addDrop = ((request, response) -> {
        String user;
        String title;
        String topic;
        String userName;

        JacksonMongoCollection<DropModel> dropCollection;
        DropModel drop;
        RequestParamHelper params = new RequestParamHelper(request);
        SessionManager sessionManager = new SparkSessionManager(request);

        user = sessionManager.getCurrentUserId();
        userName = sessionManager.getCurrentUsername();
        title = params.valueOf("title");
        topic = params.valueOf("topic");


        if (user == null)
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.OK_200, "invalid parameters").toJSON());

        if(title == null || title.isEmpty() || title.contains(" "))
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.OK_200, "invalid parameters").toJSON());

        if(topic == null || topic.isEmpty())
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.OK_200, "invalid parameters").toJSON());


        drop = new DropModel();
        drop.owner = user;
        drop.title = title;
        drop.topic = topic;
        drop.ownerName = userName;

        dropCollection = new CollectionFactory<DropModel>(ServiceLocator.getService().dbService(), DropModel.class).getCollection();

        try {
            dropCollection.save(drop);
        } catch (MongoWriteException e) {
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400, "drop already exists").toJSON());
        }

        try {
            dropCollection.save(drop);
        } catch (MongoWriteException e) {
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400, "drop already exists").toJSON());
        }

        return "okay";
    });

    public static Route updateDrop = ((request, response) -> {
        String dropId;
        String topic;
        DropModel dropmodel;
        PermissionChecker checker;
        RequestParamHelper helper = new RequestParamHelper(request);
        JacksonMongoCollection<DropModel> dropCollection;

        dropCollection = new CollectionFactory<DropModel>(ServiceLocator.getService().dbService(), DropModel.class).getCollection();
        dropId = helper.valueOf("drop_id");
        topic = helper.valueOf("topic");

        if (dropId == null){
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400, "drop_id missing").toJSON());}
        if (topic == null){
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400, "updated topic missing").toJSON());}


        dropmodel = dropCollection.findOne(eq("_id", new ObjectId(dropId)));
        if (dropmodel == null){
          halt(HttpStatus.NOT_FOUND_404, new GeneralResponse(HttpStatus.NOT_FOUND_404,"Drop not found").toJSON());}


        checker = new PermissionChecker(request, new DropPermissionCheckProvider(dropmodel));
        if(!checker.verify()){
            halt(HttpStatus.UNAUTHORIZED_401, new GeneralResponse(HttpStatus.UNAUTHORIZED_401,"unauthorized").toJSON());}


        dropmodel.topic = topic;
        dropCollection.findOneAndReplace(eq("_id", new ObjectId(dropId)), dropmodel);
        return new GeneralResponse(HttpStatus.OK_200, "success");


    });

    public static Route deleteDrop = ((request, response) -> {
        String dropId;
        DropModel targetModel;
        PermissionChecker permissionChecker;
        RequestParamHelper helper = new RequestParamHelper(request);
        JacksonMongoCollection<DropModel> dropCollection;
        dropId = helper.valueOf("drop_id");

        if (dropId == null) {
            halt(HttpStatus.BAD_REQUEST_400, new GeneralResponse(HttpStatus.BAD_REQUEST_400,"missing drop").toJSON());
        }

       dropCollection = new CollectionFactory<DropModel>(ServiceLocator.getService().dbService(), DropModel.class).getCollection();
       targetModel = dropCollection.findOne(eq("_id", dropId));

       if (targetModel == null) {
           halt(HttpStatus.NOT_FOUND_404, new GeneralResponse(HttpStatus.NOT_FOUND_404,"drop doesnt exist").toJSON());
       }

       permissionChecker = new PermissionChecker(request, new DropPermissionCheckProvider(targetModel));

       if (!permissionChecker.verify()) {
           halt(HttpStatus.UNAUTHORIZED_401, new GeneralResponse(HttpStatus.UNAUTHORIZED_401,"Not authorized").toJSON());
       }

       // all checks passed
       dropCollection.deleteOne(eq("_id", new ObjectId(dropId)));

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
    /*
     * retrieve data about a specific drop
     */
    public static Route viewADrop = ((request, response) -> {

        String dropId;
        long numPosts;
        DropModel drop = null;
        DropViewModel dropViewModel = null;

        dropId = request.params("dropid");
        if(dropId ==  null){
            halt(HttpStatus.FORBIDDEN_403, new GeneralResponse(HttpStatus.FORBIDDEN_403,
                    "drop doesn't exist.").toJSON());
        }
        //to get the number of posts in the drop
        JacksonMongoCollection<PostModel> postCollection = new CollectionFactory<PostModel>
                (ServiceLocator.getService().dbService(), PostModel.class).getCollection();
        numPosts = postCollection.countDocuments(eq("drop_id",dropId));

        //to get info about the drop from the DropModel
        JacksonMongoCollection<DropModel> collection = new CollectionFactory<DropModel>(Main.services.dbService(),
                DropModel.class).getCollection();

        drop = collection.findOne(eq("_id", new ObjectId(dropId)));
        if(drop ==  null){
            halt(HttpStatus.FORBIDDEN_403, new GeneralResponse(HttpStatus.FORBIDDEN_403,
                    "drop doesn't exist").toJSON());
        }
        dropViewModel = new DropViewModel();
        dropViewModel.number_posts = numPosts;
        dropViewModel.drop_title = drop.title;
        dropViewModel.drop_topic = drop.topic;
        dropViewModel.drop_id = dropId;
        dropViewModel.owner_id = drop.owner;
        dropViewModel.owner_name = drop.ownerName;

        return dropViewModel;
    });
}
