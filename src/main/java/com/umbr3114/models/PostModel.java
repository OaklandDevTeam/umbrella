package com.umbr3114.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.mongojack.MongoCollection;

@MongoCollection(name = "posts")
public class PostModel {

    public ObjectId _id;
    public String title;
    public String bodyText;
    public String authorId; // anonymous?
    public String author;
    public String dropId;
    public String idString;
    //public List<CommentModel> comments;

    public PostModel(String theTitle, String text, String user, String dropId) {
        this.title = theTitle;
        this.bodyText = text;
        this.authorId = user;
        this.dropId = dropId;
    }

    public PostModel() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getIdString() {
        return _id.toString();
    }

    public void setIdString() { /*dummy method*/}
}
