package com.umbr3114.common;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.umbr3114.Main;
import jdk.nashorn.internal.ir.RuntimeNode;
import spark.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates logic that handles deserialization of JSON parameter string
 */
class JSONParamDeserializationStrategy implements ParamDeserializationStrategy {
    private String requestBody;
    private Request sparkRequest;
    private ObjectReader jsonReader;

    JSONParamDeserializationStrategy(Request sparkRequest, ObjectMapper objectMapper) {
        this.sparkRequest = sparkRequest;
        this.jsonReader = createObjectReader(objectMapper);
    }

    public JSONParamDeserializationStrategy(Request sparkRequest) {
        this.sparkRequest = sparkRequest;
        this.jsonReader = createObjectReader(Main.services.jsonMapper());
    }

    private ObjectReader createObjectReader(ObjectMapper mapper) {
        // Thank you https://www.baeldung.com/jackson-map
        TypeReference<HashMap<String, String>> typeReference;
        typeReference = new TypeReference<HashMap<String, String>>() { };
        return mapper.readerFor(typeReference);
    }

    @Override
    public Map<String, String> deserializeToMap() {
        HashMap<String, String> paramMap;

        try {
            paramMap = jsonReader.readValue(sparkRequest.bodyAsBytes());
        } catch (IOException | NullPointerException e) {
            // return an empty param map
            paramMap = new HashMap<>();
        }
        return paramMap;
    }
}
