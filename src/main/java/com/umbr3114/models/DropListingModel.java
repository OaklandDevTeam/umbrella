package com.umbr3114.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DropListingModel {

    @JsonProperty("drop_id")
    public String dropId;
    @JsonProperty("drop_title")
    public String title;

    public DropListingModel(String id, String title) {
        this.dropId = id;
        this.title = title;
    }
}
