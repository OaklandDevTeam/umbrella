package com.umbr3114.models;

import org.bson.types.ObjectId;
import org.mongojack.MongoCollection;

@MongoCollection(name = "drops")
public class DropModel {
    public String title;
    public String topic;
    public ObjectId _id;
    public String owner;


}
