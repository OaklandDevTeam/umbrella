package com.umbr3114.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mongojack.MongoCollection;
import org.mongojack.ObjectId;

/*
 * store the data about a specific drop
 */
@MongoCollection(name = "dropView")
public class DropViewModel {

    public String title;
    public String topic;
    @JsonProperty("drop_id")
    public String dropId;
    public String owner;
    public String ownerName;
    public long numberPosts;


}
