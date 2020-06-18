package com.umbr3114.models;

import org.mongojack.MongoCollection;
import org.mongojack.ObjectId;

/*
 * store the data about a specific drop
 */
@MongoCollection(name = "dropView")
public class DropViewModel {

    public String title;
    public String topic;
    public String dropId;
    public String owner;
    public String ownerName;
    public long numberPosts;


}
