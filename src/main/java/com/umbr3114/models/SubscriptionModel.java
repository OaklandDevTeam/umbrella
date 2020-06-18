package com.umbr3114.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.mongojack.Id;
import org.mongojack.MongoCollection;

/**
 * create a table for storing the username and the subscribed dropId
 */
@MongoCollection(name = "dropSubscription")
public class SubscriptionModel {
    public String userid;
    public String dropid;
    public String dropName;
    @Id
    @JsonIgnore
    public ObjectId _id;

}
