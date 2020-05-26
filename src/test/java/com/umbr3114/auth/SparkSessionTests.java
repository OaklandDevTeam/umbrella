package com.umbr3114.auth;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spark.Request;
import spark.Response;
import spark.Session;

public class SparkSessionTests {

    @Mock
    Request mockRequest;

    @Mock
    Response mockResponse;

    @Mock
    Session mockSession;



    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }


    private SparkSessionManager createSparkSessionManager() {
        return new SparkSessionManager(mockRequest, mockResponse);
    }
}
