package com.umbr3114.auth;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mongojack.JacksonMongoCollection;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MongoUserManagerTests {

    @Mock
    public JacksonMongoCollection<UserModel> mockUserModelCollection;

    @Mock
    public SessionManager sessionManager;

    @Mock
    public PasswordHasher mockHasher;

    @Mock
    public CredentialVerifier verifier;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);


    }

    @Test
    public void test_login_alwaysFalseVerifier_returnsFalse() {
        MongoUserManager manager = createMongoUserManager();
        boolean result;

        when(verifier.verify("", "")).thenReturn(false);

        result = manager.login("", "");

        Assert.assertFalse(result);
    }

    @Test
    public void test_login_alwaysTrueVerifier_returnsTrue() {
        MongoUserManager manager = createMongoUserManager();
        boolean result;
        FakeUserModel model = new FakeUserModel("", "", "");
        model.setFakeIdString("test");

        when(verifier.verify("", "")).thenReturn(true);
        when(verifier.getUserModel()).thenReturn(model);

        result = manager.login("", "");

        Assert.assertTrue(result);
    }

    private MongoUserManager createMongoUserManager() {
        return new MongoUserManager(mockUserModelCollection, sessionManager, mockHasher, verifier);
    }

    public class FakeUserModel extends UserModel {
        private String fakeIdString;

        public FakeUserModel(String user, String email, String pass) {
            super(user, pass, email, false);
        }

        public void setFakeIdString(String fakeIdString) {
            this.fakeIdString = fakeIdString;
        }

        @Override
        public String getUserIdString() {
            return fakeIdString;
        }
    }

}
