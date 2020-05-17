package com.umbr3114.auth;

import org.mindrot.jbcrypt.BCrypt;

class BCryptPasswordHasher implements PasswordHasher {
    private String salt;

    public BCryptPasswordHasher() {
        this.salt = BCrypt.gensalt();
    }

    public BCryptPasswordHasher(String salt) {
        this.salt = salt;
    }

    public boolean compare(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }

    public String hash(String raw) {
        return BCrypt.hashpw(raw, salt);
    }
}
