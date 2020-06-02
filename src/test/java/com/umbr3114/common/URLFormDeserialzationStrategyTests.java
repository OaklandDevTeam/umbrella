package com.umbr3114.common;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import spark.Request;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class URLFormDeserialzationStrategyTests {

    @Mock
    public Request fakeSparkRequest;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_derserializeToMap_deserializes() {
        URLFormDeserializationStrategy strategy;
        Map<String, String> testMap = new HashMap<>();
        Map<String, String> resultMap;

        testMap.put("param1", "val1");
        testMap.put("param2", "val2");
        testMap.put("param3", "val3");

        when(fakeSparkRequest.queryParams()).thenReturn(testMap.keySet());
        // this is a little hacky, but eh it works
        for(String key : testMap.keySet()) {
            when(fakeSparkRequest.queryParams(key)).thenReturn(testMap.get(key));
        }

        strategy = new URLFormDeserializationStrategy(fakeSparkRequest);
        resultMap = strategy.deserializeToMap();

        Assert.assertEquals(testMap, resultMap);
    }

    @Test
    public void test_deserializeToMap_queryParamsEmpty_returnsEmptyMap(){
        URLFormDeserializationStrategy strategy;
        Map<String, String> testMap = new HashMap<>();
        Map<String, String> resultMap;

        when(fakeSparkRequest.queryParams()).thenReturn(testMap.keySet());

        strategy = new URLFormDeserializationStrategy(fakeSparkRequest);
        resultMap = strategy.deserializeToMap();
        Assert.assertEquals(testMap, resultMap);
    }

}
