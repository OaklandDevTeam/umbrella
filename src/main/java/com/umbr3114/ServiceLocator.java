package com.umbr3114;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.umbr3114.common.DatabaseConnectionDetails;
import com.umbr3114.data.MongoClientFactory;
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
        DatabaseConnectionDetails mongoConnectionDetails;

        try {
             mongoConnectionDetails = new DatabaseConnectionDetails();
        } catch (IllegalArgumentException e) {
            log.error("Database credentials are missing - cannot continue!");
            throw new IllegalStateException();
        }
        mongoClient = MongoClientFactory.create(
                mongoConnectionDetails.getUsername(),
                mongoConnectionDetails.getPassword(),
                mongoConnectionDetails.getHost()
        );

        dbService = mongoClient.getDatabase("umbrella-data");
    }

    public MongoClient mongoClient() {
        return mongoClient;
    }

    public MongoDatabase dbService() {
        return dbService;
    }
}
