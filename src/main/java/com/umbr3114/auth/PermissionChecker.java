package com.umbr3114.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

public class PermissionChecker {

    private static final Logger log = LoggerFactory.getLogger("PermissionChecker");

    private PermissionCheckProvider checker;

    public PermissionChecker(Request sparkRequest, PermissionCheckProvider checker) {
        this.checker = checker;
        SessionManager sessionManager = new SparkSessionManager(sparkRequest);
        checker.setSessionManager(sessionManager);
    }

    PermissionChecker(PermissionCheckProvider checker) {
        this.checker = checker;
    }


    public boolean verify() {
        if (checker.isAdmin()) {
            return true;
        } else if(checker.isOwner()) {
            return true;
        } else return checker.isModerator(); // worst case scenario (this is an expensive check for posts and comments)
    }

}
