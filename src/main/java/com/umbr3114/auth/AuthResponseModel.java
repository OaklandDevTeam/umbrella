package com.umbr3114.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthResponseModel {

    public String message;
    public int status;

    public AuthResponseModel(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public String toJSON() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
