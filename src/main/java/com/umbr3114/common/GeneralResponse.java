package com.umbr3114.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umbr3114.Main;

public class GeneralResponse {

    public String message;
    public int status;

    public GeneralResponse(int status, String message) {
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
