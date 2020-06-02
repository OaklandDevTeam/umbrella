package com.umbr3114.auth;

import com.umbr3114.models.DropModel;
import com.umbr3114.models.DropModel.ModeratorModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PermissionCheckerTests {


    public static final String TEST_ENTITY_ID = "fakeId";

    @Mock
    public SessionManager sessionManager;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_verifyDropModerator_returnTrue() {
        PermissionChecker checker = createChecker(createFakeFilledModList());

        when(sessionManager.isAdmin()).thenReturn(false);
        when(sessionManager.getCurrentUserId()).thenReturn("1");

        Assert.assertTrue(checker.verify());
    }

    @Test
    public void test_verifyCurrentUserIsOwner_returnTrue() {
        PermissionChecker checker = createChecker(createFakeFilledModList());

        when(sessionManager.isAdmin()).thenReturn(false);
        when(sessionManager.getCurrentUserId()).thenReturn(TEST_ENTITY_ID);

        Assert.assertTrue(checker.verify());
    }

    @Test
    public void test_verifyNullModList_returnFalse() {
        PermissionChecker checker = createChecker(createFakeNullModList());

        when(sessionManager.isAdmin()).thenReturn(false);
        when(sessionManager.getCurrentUserId()).thenReturn("3");

        Assert.assertFalse(checker.verify());
    }

    @Test
    public void test_verifyEmptyModList_returnFalse() {
        PermissionChecker checker = createChecker(createFakeEmptyModList());

        when(sessionManager.isAdmin()).thenReturn(false);
        when(sessionManager.getCurrentUserId()).thenReturn("3");

        Assert.assertFalse(checker.verify());
    }

    @Test
    public void test_verifyAllNullData_returnFalse() {
        PermissionChecker checker = createChecker(createFakeAllNull());

        when(sessionManager.isAdmin()).thenReturn(false);
        when(sessionManager.getCurrentUserId()).thenReturn("3");

        Assert.assertFalse(checker.verify());
    }

    @Test
    public void test_verifyDropModeratorNotInList_returnFalse() {
        PermissionChecker checker = createChecker(createFakeFilledModList());

        when(sessionManager.isAdmin()).thenReturn(false);
        when(sessionManager.getCurrentUserId()).thenReturn("40");

        Assert.assertFalse(checker.verify());

    }

    @Test
    public void test_verifySessionManagerReturnsNullUserId_returnFalse() {
        PermissionChecker checker = createChecker(createFakeFilledModList());

        when(sessionManager.isAdmin()).thenReturn(false);
        when(sessionManager.getCurrentUserId()).thenReturn(null);

        Assert.assertFalse(checker.verify());
    }


    public PermissionChecker createChecker(PermissionCheckProvider provider) {
        return new PermissionChecker(provider);
    }


    public class FakePermissionCheckProvider extends PermissionCheckProvider {

        String entityOwner;
        List<ModeratorModel> fakeMods;

        public FakePermissionCheckProvider(String entityOwner, List<ModeratorModel> modModels) {
            this.entityOwner = entityOwner;
            this.fakeMods = modModels;
            this.setSessionManager(sessionManager);
        }

        @Override
        public String provideOwnerId() {
            return entityOwner;
        }

        @Override
        public DropModel provideDropModel() {
            DropModel model = new DropModel();
            model.moderators = fakeMods;
            model.owner = entityOwner;
            model.topic = "";
            model.title = "";

            return model;
        }
    }

    private List<ModeratorModel> createFilledModeratorModel() {
        List<ModeratorModel> mods = new ArrayList<>();
        mods.add(new ModeratorModel("abc123", "1"));
        mods.add(new ModeratorModel("abc123", "2"));
        mods.add(new ModeratorModel("abc123", "3"));
        return mods;
    }

    private List<ModeratorModel> createEmptyModeratorModel() {
        return new ArrayList<ModeratorModel>();
    }

    private FakePermissionCheckProvider createFakeFilledModList() {
        return new FakePermissionCheckProvider(TEST_ENTITY_ID, createFilledModeratorModel());
    }

    private FakePermissionCheckProvider createFakeNullModList() {
        return new FakePermissionCheckProvider(TEST_ENTITY_ID, null);
    }

    private FakePermissionCheckProvider createFakeEmptyModList() {
        return new FakePermissionCheckProvider(TEST_ENTITY_ID, createEmptyModeratorModel());
    }

    private FakePermissionCheckProvider createFakeAllNull() {
        return new FakePermissionCheckProvider(null, null);
    }
}
