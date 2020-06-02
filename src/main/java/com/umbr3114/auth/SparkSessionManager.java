package com.umbr3114.auth;

import spark.Request;
import spark.Response;
import spark.Session;

public class SparkSessionManager implements SessionManager {

    public static final int INACTIVE_SECONDS = 7200;

    private Request request;
    private Response response;

    public SparkSessionManager(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public SparkSessionManager(Request request) {
        this.request = request;
    }

    public boolean isAuthorized() {
        return request.session().attributes().contains("userId");
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

    @Override
    public void startSession(UserModel userModel) {
        Session sparkSession;

        if (userModel == null) {
            throw new IllegalArgumentException("passed null UserModel");
        }

        sparkSession = request.session(true);
        sparkSession.maxInactiveInterval(INACTIVE_SECONDS);
        sparkSession.attribute("userId", userModel.getUserIdString());
        sparkSession.attribute("username", userModel.getUsername());
        sparkSession.attribute("isAdmin", userModel.isAdmin());

    }

    @Override
    public boolean isAdmin() {
        return request.session().attribute("isAdmin");
    }

    @Override
    public String getCurrentUsername() {
        return request.session().attribute("username");
    }

    @Override
    public String getCurrentUserId() {
        return request.session().attribute("userId");
    }
}
