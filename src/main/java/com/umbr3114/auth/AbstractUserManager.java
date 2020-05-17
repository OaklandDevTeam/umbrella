package com.umbr3114.auth;

public class AbstractUserManager implements UserManager{

    private PasswordHasher hasher;
    private SessionManager sessionManager;

    public AbstractUserManager(SessionManager sessionManager, PasswordHasher hasher) {
        this.hasher = hasher;
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean login(String user, String password) {
        return false;
    }

    @Override
    public boolean register(UserModel registrationModel) {
        return false;
    }

    @Override
    public boolean logout() {
        return false;
    }

    public PasswordHasher getHasher() {
        return hasher;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
