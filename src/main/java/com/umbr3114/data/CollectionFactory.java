package com.umbr3114.data;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.umbr3114.Main;
import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;

/**
 * This factory class creates JacksonMongoCollection objects.
 * See CollectionFactory usage notes in the Github wiki
 * @param <T>
 */
public class CollectionFactory<T> {
    JacksonMongoCollection<T> collection;
    MongoDatabase database;
    Class modelClass;

    /**
     * Constructs the collection factory
     * @param database
     * @param modelClass
     */
    public CollectionFactory(MongoDatabase database, Class modelClass) {
        this.modelClass = modelClass;
        this.database = database;
    }

    /**
     * Creates and returns a JacksonMongoCollection object
     * @return
     */
    public JacksonMongoCollection<T> getCollection() {

        return (JacksonMongoCollection<T>) JacksonMongoCollection
                .builder()
                .build(database, modelClass, UuidRepresentation.STANDARD);
    }
}
