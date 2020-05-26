package com.umbr3114.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class JSONParamDeserializationStrategy implements ParamDeserializationStrategy {
    private String requestBody;

    public JSONParamDeserializationStrategy(String reqBody) {
        this.requestBody = reqBody;
    }

    @Override
    public Map<String, String> deserializeToMap() {
        HashMap<String, String> paramMap;
        // Thank you https://www.baeldung.com/jackson-map
        TypeReference<HashMap<String, String>> typeReference;
        ObjectMapper mapper;

        typeReference = new TypeReference<HashMap<String, String>>() { };
        mapper = new ObjectMapper();

        try {
            paramMap = mapper.readValue(requestBody, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return paramMap;
    }
}
