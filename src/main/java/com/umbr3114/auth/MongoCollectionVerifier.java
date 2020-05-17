package com.umbr3114.auth;

import com.mongodb.client.model.Filters;
import org.mongojack.JacksonMongoCollection;

class MongoCollectionVerifier implements CredentialVerifier {

    private JacksonMongoCollection<UserModel> userCollection;
    private PasswordHasher hasher;
    private UserModel userModel;

    public MongoCollectionVerifier( JacksonMongoCollection<UserModel> mongoCollection, PasswordHasher hasher) {
        this.userCollection = mongoCollection;
        this.hasher = hasher;
    }

    private UserModel fetchUserModel(String user) {
        return userCollection.findOne(Filters.eq("username", user));
    }

    @Override
    public boolean verify(String user, String password) {
        userModel = fetchUserModel(user);
        if (userModel == null)
            return false;

        return hasher.compare(password, userModel.getPassword());
    }

    @Override
    public UserModel getUserModel() {
        return userModel;
    }
}
