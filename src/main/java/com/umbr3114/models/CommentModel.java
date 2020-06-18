package com.umbr3114.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.mongojack.MongoCollection;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

@MongoCollection(name = "comments")
public class CommentModel {
    public ObjectId _id;
    public String bodyText;
    public String authorId;
    @JsonProperty("post_id")
    public String postId;
    public Date date;

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String getAuthorId() {
        return authorId;
    }

    public Date getDate() { return date; }

    public void setDate(Date date1) { this.date = date1; }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getPostId() { return postId; }

    public void setPostId(String postId) { this.postId = postId; }

    public String getIdString(){
        return _id.toString();
    }
    public void setIdString() { /* dummy method */}

    @JsonIgnore
    public String getPostIdString() { return postId; }
}

