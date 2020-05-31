package com.umbr3114.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.mongojack.MongoCollection;

import java.util.List;

@MongoCollection(name = "users")
public class UserModel {

    public ObjectId _id;
    private String username;
    private String email;
    private String password;
    private List<UserRole> roles;

    public UserModel(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserModel() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    @BsonIgnore
    public String getUserIdString() {
        return _id.toString();
    }

    class UserRole {

    }
}
