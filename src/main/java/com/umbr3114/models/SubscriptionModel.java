package com.umbr3114.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.mongojack.Id;
import org.mongojack.MongoCollection;

/**
 * create a table for storing the username and the subscribed dropId
 */
@MongoCollection(name = "dropSubscription")
public class SubscriptionModel {
    @JsonProperty("user_id")
    public String userid;
    @JsonProperty("drop_id")
    public String dropid;
    public String dropName;
    @Id
    @JsonIgnore
    public ObjectId _id;

}
