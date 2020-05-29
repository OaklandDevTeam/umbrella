package com.umbr3114.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spark.Request;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class JSONParamDeserializationStrategyTests {

    @Mock
    Request fakeSparkRequest;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_deserializeToMap_deserializesBasicJSON() {
        Map<String, String> testMap = new HashMap<>();
        Map<String, String> resultMap;
        JSONParamDeserializationStrategy strategy;
        ObjectMapper mapper = new ObjectMapper();

        String basicJsonString = "{\"param1\":\"val1\", \"param2\":\"val2\", \"param3\":\"val3\"}";
        when(fakeSparkRequest.bodyAsBytes()).thenReturn(basicJsonString.getBytes());

        testMap.put("param1", "val1");
        testMap.put("param2", "val2");
        testMap.put("param3", "val3");

        strategy = new JSONParamDeserializationStrategy(fakeSparkRequest, mapper);
        resultMap = strategy.deserializeToMap();
        Assert.assertEquals(testMap, resultMap);
    }

    @Test
    public void test_deserializeToMap_deserializeJsonWithNonStringParameter() {
        String jsonString = "{\"param1\":1, \"param2\":2, \"param3\":3}";
        Map<String, String> resultMap;
        Map<String, String> testMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        when(fakeSparkRequest.bodyAsBytes()).thenReturn(jsonString.getBytes());

        JSONParamDeserializationStrategy strategy = new JSONParamDeserializationStrategy(fakeSparkRequest, mapper);
        testMap.put("param1", "1");
        testMap.put("param2", "2");
        testMap.put("param3", "3");

        resultMap = strategy.deserializeToMap();

        Assert.assertEquals(testMap, resultMap);
    }

    @Test
    public void test_deserializeToMap_malformedJSONReturnsEmptyMap() {
        Map<String, String> testMap = new HashMap<>();
        Map<String, String> resultMap;
        JSONParamDeserializationStrategy strategy;
        ObjectMapper mapper = new ObjectMapper();
        String basicJsonString = "{param1:\"val1\", \"param2\":\"val2\", \"param3\":\"val3\"}";

        when(fakeSparkRequest.bodyAsBytes()).thenReturn(basicJsonString.getBytes());

        strategy = new JSONParamDeserializationStrategy(fakeSparkRequest, mapper);
        resultMap = strategy.deserializeToMap();
        Assert.assertEquals(testMap, resultMap);
    }

    @Test
    public void test_deserializeToMap_nullBodyReturnsEmptyMap() {
        Map<String, String> testMap = new HashMap<>();
        Map<String, String> resultMap;
        JSONParamDeserializationStrategy strategy;
        ObjectMapper mapper = new ObjectMapper();

        when(fakeSparkRequest.bodyAsBytes()).thenReturn(null);

        strategy = new JSONParamDeserializationStrategy(fakeSparkRequest, mapper);
        resultMap = strategy.deserializeToMap();
        Assert.assertEquals(testMap, resultMap);
    }

}
