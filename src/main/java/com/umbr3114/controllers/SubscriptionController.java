package com.umbr3114.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.umbr3114.ServiceLocator;
import com.umbr3114.common.GeneralResponse;
import com.umbr3114.common.RequestParamHelper;
import com.umbr3114.data.CollectionFactory;
import com.umbr3114.models.SubscriptionModel;
import org.eclipse.jetty.http.HttpStatus;
import org.mongojack.JacksonMongoCollection;
import spark.Filter;
import spark.Route;
import java.util.ArrayList;
import java.util.List;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static spark.Spark.halt;

/**
 * to get the username and dropId, then store it into a collection
 */
public class SubscriptionController {
    public static Route subscribe = ((request, response) -> {

        //to get the userId and dropId
        RequestParamHelper params = new RequestParamHelper(request);
        String userID;
        String dropID;
        //create a collection used to save the userId and dropId later on
        JacksonMongoCollection<SubscriptionModel> collection = new CollectionFactory<SubscriptionModel>
                              (ServiceLocator.getService().dbService(),SubscriptionModel.class).getCollection();

        SubscriptionModel subscription = null;
        //retrieve userId and dropId from the database
        userID = request.session().attribute("username");
        dropID = params.valueOf("dropId");

        if(dropID == null || userID == null){
            halt(HttpStatus.FORBIDDEN_403,
                    new GeneralResponse(HttpStatus.FORBIDDEN_403, "drop_id or user_id missing.")
                            .toJSON());
        }
        //save the userId and dropId to the collection
        subscription = new SubscriptionModel();
        subscription.userid = userID;
        subscription.dropid = dropID;
        collection.save(subscription);

        return new GeneralResponse(HttpStatus.OK_200, "subscribed");
    });

    /**
     * to find the username and dropId, then remove them from the collection
     */
    public static Route unsubscribe = ((request, response) -> {

        RequestParamHelper params = new RequestParamHelper(request);
        String userID = request.session().attribute("username");
        String dropID =  params.valueOf("dropid");
        DeleteResult result;   //to see how many Drops have been unsubscribed

        //to remove the dropId from the subscribed list
        JacksonMongoCollection<SubscriptionModel> removeDropCollection = new CollectionFactory<SubscriptionModel>
                (ServiceLocator.getService().dbService(),SubscriptionModel.class).getCollection();

        result = removeDropCollection.deleteOne(and(eq("userid",userID),eq("dropid",dropID)));
        if(result.getDeletedCount() == 0){
            halt(HttpStatus.FORBIDDEN_403,
                    new GeneralResponse(HttpStatus.FORBIDDEN_403, "drop_id doesn't exist.")
                            .toJSON());
        }
        return new GeneralResponse(HttpStatus.OK_200,"Unsubscribed");
    });
    /**
     * to list the Drops that subscribed by the user
     */
    public static Route subscribed = ((request, response) -> {
          //find a subscription record based on the username
          RequestParamHelper params = new RequestParamHelper(request);
          String userID = request.session().attribute("username");
          JacksonMongoCollection<SubscriptionModel> dropCollection = new CollectionFactory<SubscriptionModel>
                  (ServiceLocator.getService().dbService(),SubscriptionModel.class).getCollection();
          //to assign the result to a list and show the subscription info
          List<SubscriptionModel> list = new ArrayList<>();
          if(userID == null){
              halt(HttpStatus.FORBIDDEN_403,
                      new GeneralResponse(HttpStatus.FORBIDDEN_403, "No subscription record was found.")
                              .toJSON());
          }
          dropCollection.find(eq("userid",userID)).into(list);

          return list;
    });
}
