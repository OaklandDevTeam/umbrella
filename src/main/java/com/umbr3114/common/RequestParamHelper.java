package com.umbr3114.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.util.Map;

/**
 * RequestParamHelper is a utility class to easily extract parameters out of Spark POST requests.
 * The main motivation for this class is when a JSON object is sent as a body of parameters in a POST request.
 * This class allows you to easily retrieve parameters in a key/value fashion
 */
public class RequestParamHelper {

    public static final String REQUEST_FORM_ENCODED = "application/x-www-form-urlencoded";
    public static final String REQUEST_JSON_POST = "application/json";

    private Request sparkRequest;
    private final Logger log = LoggerFactory.getLogger("RequestParamHelper");
    private ParamDeserializationStrategy deserializationStrategy;
    private Map<String, String> paramMap;

    /**
     * Default constructor
     * @param sparkRequest Request object from Spark route. Typically the request variable
     */
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

    /**
     * This constructor is package-private and is therefore not intended for use outside of this package, within the rest of the application.
     * This constructor is mainly for unit testing purposes.
     * @param sparkRequest Spark Requst object
     * @param deserializationStrategy Typically a Mocked ParamDeserializationStrategy
     */
    RequestParamHelper(Request sparkRequest, ParamDeserializationStrategy deserializationStrategy) {
        this.sparkRequest = sparkRequest;
        this.deserializationStrategy = deserializationStrategy;

        paramMap = deserializationStrategy.deserializeToMap();
    }

    /**
     * Searches for a specific parameter key in the request to determine if it exists
     * @param key String key name that is expected in the request
     * @return true if key exists, false if key is missing, false if key is found but String value is empty.
     */
    public boolean hasKey(String key) {
        if (paramMap.containsKey(key)) return !paramMap.get(key).isEmpty();
        return false;
    }

    /**
     * Returns the value of a specific parameter key in the request. Always returns String
     * @param key String name that is expected in the Request
     * @return String value of the request parameter, null if key is missing, null if key exists but String value is empty
     */
    public String valueOf(String key) {
        if (!hasKey(key)) return null;

        return paramMap.get(key);
    }

    /**
     * Determines the Strategy logic based on the Content-Type of the request
     * Deserializing key/value pairs is vastly different if it is a URLFormEncoded request or a JSON body request.
     * Instead of complicating this class with the logic, it is extracted to multiple Strategy classes
     * This also allows this class to easily support other Content-Types in the future (or deal with GET params)
     */
    private void createDeserializationStrategy() {
        String contentType = sparkRequest.contentType();
        if (contentType == null) {
            contentType = "bad";
        }

        switch (contentType) {
            case REQUEST_FORM_ENCODED:
                deserializationStrategy = new URLFormDeserializationStrategy(sparkRequest);
                break;
            case REQUEST_JSON_POST:
                deserializationStrategy = new JSONParamDeserializationStrategy(sparkRequest);
                break;
            default:
                log.debug("unsupported content type for request");
                throw new UnsupportedContentTypeException();
        }
    }

    public ParamDeserializationStrategy getDeserializationStrategy() {
        return deserializationStrategy;
    }
}
