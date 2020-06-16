package com.umbr3114.data;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.mongojack.JacksonMongoCollection;
import org.mongojack.ObjectMapperConfigurer;

/**
 * This factory class creates JacksonMongoCollection objects.
 * See CollectionFactory usage notes in the Github wiki
 * @param <T>
 */
public class CollectionFactory<T> {
    JacksonMongoCollection<T> collection;
    MongoDatabase database;
    Class modelClass;

    // all instances of JacksonMongoCollection should reuse this object mapper
    private static final ObjectMapper mongoMapper = provideObjectMapper();

    /**
     * Constructs the collection factory
     * @param database
     * @param modelClass
     */
    public CollectionFactory(MongoDatabase database, Class modelClass) {
        this.modelClass = modelClass;
        this.database = database;
        provideObjectMapper();
    }

    /**
     * Creates and returns a JacksonMongoCollection object
     * @return
     */
    public JacksonMongoCollection<T> getCollection() {

        return (JacksonMongoCollection<T>) JacksonMongoCollection
                .builder()
                .withObjectMapper(mongoMapper)
                .build(database, modelClass, UuidRepresentation.STANDARD);
    }

    private static ObjectMapper provideObjectMapper() {
        ObjectMapper mapper;
         mapper = new ObjectMapper()
                 // fixes issue where database documents contain a property that a model does not contain
                 // and therefore crashes the route with an exception.
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ObjectMapperConfigurer.configureObjectMapper(mapper);
        return mapper;
    }
}
