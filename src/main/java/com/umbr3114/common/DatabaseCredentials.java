package com.umbr3114.common;

public class DatabaseCredentials {

    private String dbUsername;
    private String dbPassword;

    public DatabaseCredentials(String user, String password) {

        if ((user == null || user.isEmpty()) && (password == null || password.isEmpty())){
            throw new IllegalArgumentException();
        }

        this.dbUsername = user;
        this.dbPassword = password;
    }

    public String getPassword() {
        return dbPassword;
    }

    public String getUsername() {
        return dbUsername;
    }

    public static DatabaseCredentials fromEnvironment() {
        return new DatabaseCredentials(System.getenv("MONGO_USER"), System.getenv("MONGO_PASS"));
    }


}
