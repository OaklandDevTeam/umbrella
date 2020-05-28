package com.umbr3114.common;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates logic that handles deserialization of JSON parameter string
 */
class JSONParamDeserializationStrategy implements ParamDeserializationStrategy {
    private String requestBody;

    /**
     * Default Constructor
     * @param reqBody JSON String that is to be deserialized
     */
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
            // return an empty param map
            return new HashMap<>();
        }

        return paramMap;
    }
}
