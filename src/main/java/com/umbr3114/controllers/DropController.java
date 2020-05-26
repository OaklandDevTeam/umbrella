package com.umbr3114.controllers;

import com.umbr3114.ServiceLocator;
import com.umbr3114.common.RequestParamHelper;
import com.umbr3114.data.CollectionFactory;
import com.umbr3114.models.DropModel;
import org.eclipse.jetty.http.HttpStatus;
import org.mongojack.JacksonMongoCollection;
import spark.Route;

import static spark.Spark.halt;

public class DropController {

    public static Route addDrop = ((request, response) -> {
        String user;
        String title;
        String topic;
        JacksonMongoCollection<DropModel> dropCollection;
        DropModel drop;

        user = request.queryParams("owner");
        title = request.queryParams("title");
        topic = request.queryParams("topic");


        if (user == null || user.isEmpty() || user.contains(" "))
             halt(HttpStatus.BAD_REQUEST_400,"Try Again");

        if(title == null || title.isEmpty() || title.contains(" "))
             halt(HttpStatus.BAD_REQUEST_400,"Try Again");

        if(topic == null || topic.isEmpty())
       halt(HttpStatus.BAD_REQUEST_400,"Try Again");


        drop = new DropModel();
        drop.owner = user;
        drop.title = title;
        drop.topic = topic;

        dropCollection = new CollectionFactory<DropModel>(ServiceLocator.getService().dbService(), DropModel.class).getCollection();
        dropCollection.save(drop);



        return "okay";


    });

}
