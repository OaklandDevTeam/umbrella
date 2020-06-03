package com.umbr3114.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class UserViewModel {

    public String username;
    @JsonProperty("registration_date")
    public Date registrationDate;
    @JsonProperty("post_count")
    public long postCount;
}
