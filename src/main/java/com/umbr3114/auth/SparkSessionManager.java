package com.umbr3114.auth;

import spark.Request;
import spark.Response;
import spark.Session;

import java.util.Optional;

public class SparkSessionManager implements SessionManager {

    public static final int INACTIVE_SECONDS = 7200;

    private Request request;
    private Response response;

    public SparkSessionManager(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    // todo could do additional permissions checking here if desired
    public boolean isAuthorized() {
        Optional<Boolean> isAuthed = Optional.ofNullable(request.session().attribute("auth"));

        return isAuthed.map(Boolean::booleanValue).orElse(false);

    }

    @Override
    public void invalidateSession() {
        request.session().invalidate();
    }

    @Override
    public void startSession(String userId, String userName) {
        Session sparkSession = request.session(true);
        sparkSession.maxInactiveInterval(INACTIVE_SECONDS);
        sparkSession.attribute("userId", userId);
        sparkSession.attribute("username", userName);
        sparkSession.attribute("auth", true);
    }
}
