package com.umbr3114.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umbr3114.Main;
import com.umbr3114.ServiceLocator;

public class AuthResponseModel {

    public String message;
    public int status;

    public AuthResponseModel(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public String toJSON() {
        String jsonMessage;
        try {
            jsonMessage =  Main.services.jsonMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            jsonMessage = "";
        }
        return jsonMessage;
    }
}
