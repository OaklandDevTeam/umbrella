package com.umbr3114.data;

import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;

public class CollectionFactory<T> {
    JacksonMongoCollection<T> collection;
    MongoDatabase database;
    Class modelClass;

    public CollectionFactory(MongoDatabase database, Class modelClass) {
        this.modelClass = modelClass;
        this.database = database;
    }

    public JacksonMongoCollection<T> getCollection() {

        return (JacksonMongoCollection<T>) JacksonMongoCollection
                .builder()
                .build(database, modelClass, UuidRepresentation.STANDARD);
    }
}
