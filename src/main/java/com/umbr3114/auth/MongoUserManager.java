package com.umbr3114.auth;

import com.mongodb.MongoWriteException;
import org.mongojack.JacksonMongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.time.Instant;

/**
 * This class is responsible for logging in a user, registering a user, and logging a user out.
 * It depends on a MongoCollection, SessionManager and PasswordHasher
 */
public class MongoUserManager extends AbstractUserManager {

    private CredentialVerifier credentialVerifier;
    private JacksonMongoCollection<UserModel> userCollection;
    private Logger log = LoggerFactory.getLogger("MongoUserManager");

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

        getSessionManager().startSession(credentialVerifier.getUserModel());
        return true;
    }
    @Override
    public boolean register(UserModel registrationModel) {
        // hash it, send it
        registrationModel.setPassword(getHasher().hash(registrationModel.getPassword()));
        registrationModel.setRegistrationDate(Date.from(Instant.now()));

        try {
            userCollection.save(registrationModel);
        } catch(MongoWriteException e) {
            log.error("User registration with duplicate username attempted");
            throw new DuplicateUserException();
        }

        return true;
    }

    @Override
    public boolean logout() {
        getSessionManager().invalidateSession();
        return false;
    }

}
