package com.umbr3114.auth;

public interface SessionManager {

    boolean isAuthorized();
    void invalidateSession();
    void startSession(String userId, String userName);
}
