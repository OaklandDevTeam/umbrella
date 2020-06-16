package com.umbr3114.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CommentListingModel {

    public String postId;
    public int count;
    public String lastId;
    public List<CommentModel> comments;
}