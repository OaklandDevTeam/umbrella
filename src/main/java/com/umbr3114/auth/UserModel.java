package com.umbr3114.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.mongojack.MongoCollection;

import java.util.Date;

@MongoCollection(name = "users")
public class UserModel {

    public ObjectId _id;
    private String username;
    private String email;
    private String password;
    private boolean isAdmin;
    private boolean isBanned;
    private Date registrationDate;

    public UserModel(String username, String email, String password, boolean admin) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isAdmin = admin;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    @JsonIgnore
    @BsonIgnore
    public String getUserIdString() {
        return _id.toString();
    }
}
