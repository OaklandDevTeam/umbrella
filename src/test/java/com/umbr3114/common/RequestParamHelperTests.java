package com.umbr3114.common;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
public class RequestParamHelperTests {

    @Mock
    ParamDeserializationStrategy fakeStrategy;
    @Mock
    Request fakeSparkRequest;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_choosesCorrectStrategy_JSON() {
        RequestParamHelper helper;
        // mocks
        when(fakeSparkRequest.headers("Content-Type")).thenReturn(RequestParamHelper.REQUEST_JSON_POST);
        when(fakeSparkRequest.body()).thenReturn("{}");
        
        helper = new RequestParamHelper(fakeSparkRequest);

        Assert.assertTrue(helper.getDeserializationStrategy() instanceof JSONParamDeserializationStrategy);
    }

    @Test
    public void test_choosesCorrectStrategy_FORM() {
        RequestParamHelper helper;
        // mocks
        when(fakeSparkRequest.headers("Content-Type")).thenReturn(RequestParamHelper.REQUEST_FORM_ENCODED);

        helper = new RequestParamHelper(fakeSparkRequest);

        Assert.assertTrue(helper.getDeserializationStrategy() instanceof URLFormDeserializationStrategy);
    }

    @Test
    public void test_choosesCorrectStrategy_unsupportedContentType_throwsException() {
        boolean throwsException = false;
        RequestParamHelper helper;

        when(fakeSparkRequest.headers("Content-Type")).thenReturn("WRONG");

        try {
            helper = new RequestParamHelper(fakeSparkRequest);
        } catch (UnsupportedContentTypeException e) {
            throwsException = true;
        }
        Assert.assertTrue(throwsException);
    }

    @Test
    public void test_hasKey_mapHasKeyReturnsTrue() {
        RequestParamHelper helper;
        Map<String, String> testMap = new HashMap<>();
        String testKey = "key";
        testMap.put(testKey, "item");
        // setup fakes
        when(fakeStrategy.deserializeToMap()).thenReturn(testMap);
        // create object
        helper = new RequestParamHelper(fakeSparkRequest, fakeStrategy);

        // test something!
        Assert.assertTrue(helper.hasKey(testKey));
    }

    @Test
    public void test_hasKey_mapDoesNotHaveKeyReturnsFalse() {
        RequestParamHelper helper;
        Map<String, String> testMap = new HashMap<>();
        String testKey = "key";
        testMap.put("SomeOtherKey", "item");
        // setup fakes
        when(fakeStrategy.deserializeToMap()).thenReturn(testMap);
        // create object
        helper = new RequestParamHelper(fakeSparkRequest, fakeStrategy);

        // test something!
        Assert.assertFalse(helper.hasKey(testKey));
    }

    @Test
    public void test_hasKey_mapHasKeyButEmptyStringReturnsFalse() {
        RequestParamHelper helper;
        Map<String, String> testMap = new HashMap<>();
        String testKey = "key";
        testMap.put(testKey, "");
        // setup fakes
        when(fakeStrategy.deserializeToMap()).thenReturn(testMap);
        // create object
        helper = new RequestParamHelper(fakeSparkRequest, fakeStrategy);

        // test something!
        Assert.assertFalse(helper.hasKey(testKey));
    }

    @Test
    public void test_valueOf_mapHasKeyReturnsValue() {
        RequestParamHelper helper;
        Map<String, String> testMap = new HashMap<>();
        String testKey = "key";
        String testValue = "item";
        String result;

        testMap.put(testKey, testValue);
        // setup fakes
        when(fakeStrategy.deserializeToMap()).thenReturn(testMap);
        // create object
        helper = new RequestParamHelper(fakeSparkRequest, fakeStrategy);

        // test something!
        result = helper.valueOf(testKey);
        Assert.assertEquals(testValue, result);
    }

    @Test
    public void test_valueOf_mapDoesNotHaveKeyReturnsNull() {
        RequestParamHelper helper;
        Map<String, String> testMap = new HashMap<>();
        String testKey = "key";
        String testValue = "item";
        String result;

        testMap.put(testKey, testValue);
        // setup fakes
        when(fakeStrategy.deserializeToMap()).thenReturn(testMap);
        // create object
        helper = new RequestParamHelper(fakeSparkRequest, fakeStrategy);

        // test something!
        result = helper.valueOf("NOTINTHEMAP");
        Assert.assertNull(result);
    }

    @Test
    public void test_valueOf_mapHasKeyButEmptyStringReturnsNull() {
        RequestParamHelper helper;
        Map<String, String> testMap = new HashMap<>();
        String testKey = "key";
        String result;

        testMap.put(testKey, "");
        // setup fakes
        when(fakeStrategy.deserializeToMap()).thenReturn(testMap);
        // create object
        helper = new RequestParamHelper(fakeSparkRequest, fakeStrategy);

        // test something!
        result = helper.valueOf(testKey);

        Assert.assertNull(result);
    }


    // Nice template method for other tests in this class
    @Ignore
    public void test_something() {
        RequestParamHelper helper;
        Map<String, String> testMap = new HashMap<>();

        // setup fakes
        when(fakeStrategy.deserializeToMap()).thenReturn(testMap);


        // create object
        helper = new RequestParamHelper(fakeSparkRequest, fakeStrategy);

        // test something!
    }


}
