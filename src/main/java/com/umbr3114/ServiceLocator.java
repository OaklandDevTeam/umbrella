package com.umbr3114;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceLocator {
    private static ServiceLocator instance;
    private static Logger log;

    private MongoClient mongoClient;

    private MongoDatabase dbService;

    private ServiceLocator(){
        initializeDbService();
    }

    public static ServiceLocator getService() {
        if (instance == null) {
            log = LoggerFactory.getLogger("Service Registry");
            instance = new ServiceLocator();
        }
        return instance;
    }

    private void initializeDbService() {
        MongoClientSettings mongoSettings;
        mongoSettings = MongoClientSettings.builder().
                applyConnectionString(new ConnectionString("mongodb+srv://umbrella-dev:SeYYB0kZy7AmhoaG@cluster0-urisd.gcp.mongodb.net/test?retryWrites=true&w=majority"))
                .retryWrites(true)
                .build();

        mongoClient = MongoClients.create(mongoSettings);

        dbService = mongoClient.getDatabase("umbrella-data");
    }

    public MongoClient mongoClient() {
        return mongoClient;
    }

    public MongoDatabase dbService() {
        return dbService;
    }
}
