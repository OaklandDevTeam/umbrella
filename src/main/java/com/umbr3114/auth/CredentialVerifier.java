package com.umbr3114.auth;

interface CredentialVerifier {
    boolean verify(String user, String password);
    UserModel getUserModel();
}
