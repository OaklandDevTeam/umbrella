package com.umbr3114.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.mongojack.MongoCollection;

@MongoCollection(name = "comments")
public class CommentModel {
    public ObjectId _id;
    public String bodyText;
    public String authorId;
    @JsonProperty("post_id")
    public String postId;

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

    public String getPostId() { return postId; }

    public void setPostId(String postId) { this.postId = postId; }

    @JsonIgnore
    public String getIdString(){
        return _id.toString();
    }
    @JsonIgnore
    public String getPostIdString() { return postId; }
}
