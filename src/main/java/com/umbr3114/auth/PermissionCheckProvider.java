package com.umbr3114.auth;

import com.umbr3114.models.DropModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PermissionCheckProvider {
    private SessionManager sessionManager;
    private static final Logger log = LoggerFactory.getLogger("PermissionChecker");

    boolean isAdmin() {
        return sessionManager.isAdmin();
    }

    boolean isOwner() {
        String ownerId = provideOwnerId();
        String authId = sessionManager.getCurrentUserId();

        if (ownerId == null) {
            log.warn("owner id provided was null");
            return false;
        }

        if (authId == null) {
            log.warn("Somehow this code was reached before an AuthCheck.");
            return false;
        }

        return authId.equals(ownerId);
    }

    boolean isModerator() {
        DropModel dropModel = provideDropModel();
        DropModel.ModeratorModel searchModModel = new DropModel.ModeratorModel(null, sessionManager.getCurrentUserId());

        if (dropModel == null) {
            log.warn("Drop model provided was null");
            return false;
        }

        if (dropModel.moderators == null) {
            return false;
        }

        return dropModel.moderators.contains(searchModModel);
    }

    void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public DropModel provideDropModel() {
        return null;
    }

    public String provideOwnerId() {
        return null;
    }
}
