package com.umbr3114.auth;

import com.mongodb.client.model.Filters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mongojack.JacksonMongoCollection;

import static org.mockito.Mockito.when;

public class MongoCollectionVerifierTests {

    @Mock
    JacksonMongoCollection<UserModel> mongoCollection;

    @Mock
    PasswordHasher hasher;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_userCollectionReturnsNullUserModel_returnsFalse() {
        MongoCollectionVerifier verifier = createVerifier();
        String nullUser = "nullUser";
        boolean result;
        when(mongoCollection.findOne(Filters.eq("username", nullUser))).thenReturn(null);
        result = verifier.verify(nullUser, "");

        Assert.assertFalse(result);
    }



    private MongoCollectionVerifier createVerifier() {
        return new MongoCollectionVerifier(mongoCollection, hasher);
    }




}
