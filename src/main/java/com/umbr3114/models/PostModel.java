package com.umbr3114.models;

import org.bson.types.ObjectId;
import org.mongojack.MongoCollection;

@MongoCollection(name = "posts")
public class PostModel {

    public ObjectId _id;
    public String title;
    public String bodyText;
    public String authorId; // anonymous?
    public String dropId;
    //public List<CommentModel> comments;

    public PostModel(String theTitle, String text, String user, String dropId) {
        this.title = theTitle;
        this.bodyText = text;
        this.authorId = user;
        this.dropId = dropId;
    }

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
}
