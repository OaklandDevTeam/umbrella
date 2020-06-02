package com.umbr3114.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.mongojack.MongoCollection;
import java.util.List;

@MongoCollection(name = "posts")
public class PostModel {
    //@JsonProperty("post_id")
    public ObjectId postId;
    public String title;
    public String bodyText;
    public String authorId; // anonymous?
    //public List<CommentModel> comments;
    public int startAfter;

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

    //public void addComment(CommentModel comment){
    //    comments.add(comment);
    //}
    @JsonIgnore
    public String getIdString() {
        return postId.toString();
    }
}

