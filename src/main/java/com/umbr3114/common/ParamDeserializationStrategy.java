package com.umbr3114.common;

import java.util.Map;

interface ParamDeserializationStrategy {
    Map<String, String> deserializeToMap();
}
