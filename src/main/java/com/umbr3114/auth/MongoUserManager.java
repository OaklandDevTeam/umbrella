package com.umbr3114.auth;

import org.mongojack.JacksonMongoCollection;

/**
 * This class is responsible for logging in a user, registering a user, and logging a user out.
 * It depends on a MongoCollection, SessionManager and PasswordHasher
 */
public class MongoUserManager extends AbstractUserManager {

    private CredentialVerifier credentialVerifier;
    private JacksonMongoCollection<UserModel> userCollection;

    public MongoUserManager(JacksonMongoCollection<UserModel> userCollection,
                            SessionManager sessionManager,
                            PasswordHasher hasher) {
        super(sessionManager, hasher);
        this.userCollection = userCollection;
        credentialVerifier = new MongoCollectionVerifier(userCollection, hasher);
    }

    public MongoUserManager(JacksonMongoCollection<UserModel> userCollection,
                            SessionManager sessionManager,
                            PasswordHasher hasher,
                            CredentialVerifier credentialVerifier) {
        super(sessionManager, hasher);
        this.userCollection = userCollection;
        this.credentialVerifier = credentialVerifier;
    }

    @Override
    public boolean login(String user, String password) {
        if (!credentialVerifier.verify(user, password)) {
            return false;
        }

        getSessionManager().startSession(
                credentialVerifier.getUserModel().getUserIdString(),
                credentialVerifier.getUserModel().getUsername()
        );
        return true;
    }

    @Override
    public boolean register(UserModel registrationModel) {
        // TODO handle the case where there is another user of the same username here
        // hash it, send it
        registrationModel.setPassword(getHasher().hash(registrationModel.getPassword()));
        userCollection.save(registrationModel);

        return true;
    }

    @Override
    public boolean logout() {
        getSessionManager().invalidateSession();
        return false;
    }

}
