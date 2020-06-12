package com.umbr3114.models;

import org.mongojack.MongoCollection;

import java.util.ArrayList;
import java.util.List;

@MongoCollection(name = "search")
public class SearchModel {
    public List<DropModel> dropResults = new ArrayList<>();
    public List<PostModel> postResults = new ArrayList<>();

}
