package com.umbr3114.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DropListingModel {

    @JsonProperty("drop_id")
    public String dropId;
    public String title;
    public String topic;

    public DropListingModel(String id, String title, String topic) {
        this.dropId = id;
        this.title = title;
        this.topic = topic;
    }
}
