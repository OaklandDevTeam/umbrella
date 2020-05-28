package com.umbr3114.auth;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AuthRoutesTests {

    public void test_validateUsername_template() {
        boolean result;
        String targetUsername = "";

        result = AuthRoutes.validateUsername(targetUsername);
        Assert.assertFalse(result);
    }

    @Test
    public void test_validateUsername_containsSpaceReturnsFalse() {
        boolean result;
        String targetUsername = "sdfg sdfg";

        result = AuthRoutes.validateUsername(targetUsername);
        Assert.assertFalse(result);
    }

    @Test
    public void test_validateUsername_nullReturnsFalse() {
        boolean result;
        String targetUsername = null;

        result = AuthRoutes.validateUsername(targetUsername);
        Assert.assertFalse(result);
    }

    @Test
    public void test_validateUsername_regularUsernameReturnsTrue() {
        boolean result;
        String targetUsername = "username";

        result = AuthRoutes.validateUsername(targetUsername);
        Assert.assertTrue(result);
    }
}
