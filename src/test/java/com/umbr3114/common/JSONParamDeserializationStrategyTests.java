package com.umbr3114.common;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class JSONParamDeserializationStrategyTests {

    @Test
    public void test_deserializeToMap_deserializesBasicJSON() {
        Map<String, String> testMap = new HashMap<>();
        Map<String, String> resultMap;
        JSONParamDeserializationStrategy strategy;
        String basicJsonString = "{\"param1\":\"val1\", \"param2\":\"val2\", \"param3\":\"val3\"}";

        testMap.put("param1", "val1");
        testMap.put("param2", "val2");
        testMap.put("param3", "val3");

        strategy = new JSONParamDeserializationStrategy(basicJsonString);
        resultMap = strategy.deserializeToMap();
        Assert.assertEquals(testMap, resultMap);
    }

    @Test
    public void test_deserializeToMap_deserializeJsonWithNonStringParameter() {
        String jsonString = "{\"param1\":1, \"param2\":2, \"param3\":3}";
        Map<String, String> resultMap;
        Map<String, String> testMap = new HashMap<>();
        JSONParamDeserializationStrategy strategy = new JSONParamDeserializationStrategy(jsonString);

        testMap.put("param1", "1");
        testMap.put("param2", "2");
        testMap.put("param3", "3");

        resultMap = strategy.deserializeToMap();

        Assert.assertEquals(testMap, resultMap);


    }
}
