package com.umbr3114.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoClientFactory {

    public static MongoClient create(String dbUser, String dbPassword, String dbHost) {
        String connectionString = String.format(
                "mongodb+srv://%s:%s@%s/umbrella-dev?retryWrites=true&w=majority",
                dbUser,
                dbPassword,
                dbHost
        );
        MongoClientSettings mongoSettings;
        mongoSettings = MongoClientSettings.builder().
                applyConnectionString(new ConnectionString(connectionString))
                .retryWrites(true)
                .build();
        return MongoClients.create(mongoSettings);
    }
}
