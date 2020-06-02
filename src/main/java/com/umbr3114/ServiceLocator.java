package com.umbr3114;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoSecurityException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.umbr3114.common.DatabaseConnectionDetails;
import com.umbr3114.data.MongoClientFactory;
import com.umbr3114.errors.DatabaseConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NameNotFoundException;

public class ServiceLocator {
    private static ServiceLocator instance;
    private static Logger log;

    private MongoClient mongoClient;
    private MongoDatabase dbService;
    private ObjectMapper jsonMapper;

    private ServiceLocator(){
        initializeDbService();
        initJsonMapper();
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

<<<<<<< HEAD
        try {
             mongoConnectionDetails = new DatabaseConnectionDetails();
        } catch (IllegalArgumentException e) {
            log.error("Database credentials are missing - cannot continue!");
            throw new IllegalStateException();
        }
=======
        mongoConnectionDetails = new DatabaseConnectionDetails();

>>>>>>> master
        mongoClient = MongoClientFactory.create(
                mongoConnectionDetails.getUsername(),
                mongoConnectionDetails.getPassword(),
                mongoConnectionDetails.getHost()
        );

        try {
            // perform read operation on database to ensure we have a connection
            dbService().listCollectionNames().first();
        } catch (MongoSecurityException e) {
            log.error("Failed to establish database connection with provided credentials");
            throw new DatabaseConnectionException("Failed to establish database connection with provided credentials");
        }
    }

    private void initJsonMapper() {
        jsonMapper = new ObjectMapper();
    }

    public MongoClient mongoClient() {
        return mongoClient;
    }

    public MongoDatabase dbService() {
        return mongoClient.getDatabase("umbrella-data");
    }

    public ObjectMapper jsonMapper() {
        return jsonMapper;
    }
}
