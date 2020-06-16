package com.umbr3114.auth;

interface UserManager {

    boolean login(String user, String password);
    boolean register(UserModel registrationModel);
    boolean logout();
    boolean toggleBan(String userIdentity, boolean isBanned);
}
