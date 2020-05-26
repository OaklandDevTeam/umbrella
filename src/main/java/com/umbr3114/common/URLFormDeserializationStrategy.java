package com.umbr3114.common;

import spark.Request;

import java.util.HashMap;
import java.util.Map;

class URLFormDeserializationStrategy implements ParamDeserializationStrategy {

    Request request;
    public URLFormDeserializationStrategy(Request sparkRequest) {
        request = sparkRequest;
    }

    @Override
    public Map<String, String> deserializeToMap() {
        Map<String, String> paramMap;

        paramMap = new HashMap<>(request.queryParams().size());

        for(String paramKey: request.queryParams()) {
            paramMap.put(paramKey, request.queryParams(paramKey));
        }
        return paramMap;
    }


}
