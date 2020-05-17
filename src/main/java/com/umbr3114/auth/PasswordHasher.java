package com.umbr3114.auth;

public interface PasswordHasher {

    public boolean compare(String rawPassword, String hashedPassword);
    public String hash(String rawPassword);
}
