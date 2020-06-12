package com.umbr3114.models;

import org.mongojack.MongoCollection;
import org.mongojack.ObjectId;

/*
 * store the data about a specific drop
 */
@MongoCollection(name = "dropView")
public class DropViewModel {

    public String drop_title;
    public String drop_topic;
    public String drop_id;
    public String owner_id;
    public String owner_name;
    public long number_posts;


}
