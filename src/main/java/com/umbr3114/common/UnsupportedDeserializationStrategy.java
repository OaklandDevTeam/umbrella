package com.umbr3114.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class UnsupportedDeserializationStrategy implements ParamDeserializationStrategy {

    public UnsupportedDeserializationStrategy() {
        Logger log = LoggerFactory.getLogger("ParameterHelper");

        log.warn("Unsupported Content-Type requested. Using an empty map for this request");
    }

    @Override
    public Map<String, String> deserializeToMap() {
        return new HashMap<>();
    }
}
