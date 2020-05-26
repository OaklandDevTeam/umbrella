package com.umbr3114.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.util.Map;

public class RequestParamHelper {

    public static final String REQUEST_FORM_ENCODED = "application/x-www-form-urlencoded";
    public static final String REQUEST_JSON_POST = "application/json";

    private Request sparkRequest;
    private final Logger log = LoggerFactory.getLogger("RequestParamHelper");
    private ParamDeserializationStrategy deserializationStrategy;
    private Map<String, String> paramMap;

    public RequestParamHelper(Request sparkRequest) {
        this.sparkRequest = sparkRequest;

        if (sparkRequest == null) {
            log.error("Passed a null Spark Request object.");
            throw new IllegalArgumentException("Passed a null Spark Request object.");
        }

        // determine what type of request is going to be deserialized
        createDeserializationStrategy();

        // set the map
        paramMap = deserializationStrategy.deserializeToMap();
    }

    RequestParamHelper(Request sparkRequest, ParamDeserializationStrategy deserializationStrategy) {
        this.sparkRequest = sparkRequest;
        this.deserializationStrategy = deserializationStrategy;

        paramMap = deserializationStrategy.deserializeToMap();
    }

    public boolean hasKey(String key) {
        if (paramMap.containsKey(key)) return !paramMap.get(key).isEmpty();
        return false;
    }

    public String valueOf(String key) {
        if (!hasKey(key)) return null;

        return paramMap.get(key);
    }

    private void createDeserializationStrategy() {

        switch (sparkRequest.headers("Content-Type")) {
            case REQUEST_FORM_ENCODED:
                deserializationStrategy = new URLFormDeserializationStrategy(sparkRequest);
                break;
            case REQUEST_JSON_POST:
                deserializationStrategy = new JSONParamDeserializationStrategy(sparkRequest.body());
                break;
            default:
                log.error("Unsupported request Content-Type");
                throw new UnsupportedContentTypeException();
        }
    }

    public ParamDeserializationStrategy getDeserializationStrategy() {
        return deserializationStrategy;
    }
}
