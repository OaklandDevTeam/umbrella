# Permission Checker

Users for the most part have 3 types of priveleged roles:
* Admins
* Entity owner (Users whom own a Drop, post or comment)
* Drop moderators

PermissionChecker essentially allows you to verify if the currently logged in user belongs to any of those 3 roles.

> **NOTE**: If your route still needs to be protected behind the login page, your route should still use the `AuthCheck` before filter.

# Usage

To use the PermissionCheck class, you need to pass it an instance of `request` and `PermissionCheckProvider`. PermissionCheckProvider is an abstract class, so you need to extend it with your own implementation. You need to override the `provideDropModel` and `provideOwnerId` methods.

```java
    public class YourProvider extends PermissionCheckProvider {
        @Override
        public DropModel provideDropModel() {
            DropModel model;

            // code to retrieve the drop model from your context..
            // Posts have a parent Drop (Posts belong to Drops)
            // Comments belong to Posts which Belong to drops

            return model;
        }

        @Override
        public String provideOwnerId() {
            String ownerId;
            // code to retrieve owner from DropModel, PostModel, CommentModel.
            return super.provideOwnerId();
        }
    }
```

Here is an implementation of this class for Drops:
```java
    public static class DropPermissionCheckProvider extends PermissionCheckProvider {
        private DropModel model;

        public DropPermissionCheckProvider(JacksonMongoCollection<DropModel> collection, String dropId) {
            model = collection.findOne(Filters.eq("_id", new ObjectId(dropId)));
        }

        public DropPermissionCheckProvider(DropModel model) {
            this.model = model;
        }

        @Override
        public DropModel provideDropModel() {
            return model;
        }

        @Override
        public String provideOwnerId() {
            if (model == null)
                return null;
            return model.owner;
        }
    }
```
* `provideDropModel` is supposed to return a DropModel that is specific to the model you are working with. so.. Comments belong to Posts, which belong to Drops. Posts belong to Drops
* `provideOwnerId` is supposed to return the owner ID (the ID of the user) that created the specific drop, post or comment.

This is how you would then use PermissionChecker in your route function
```java
    public static Route yourRoute = ((request, response) -> {
        PermissionChecker yourPermissionChecker;

        // .. some setup code

        yourPermissionChecker = new PermissionChecker(request, new YourPermissionCheckProvider());


        if(!yourPermissionChecker.verify()) {
            // The user does not have permission to call your route.
            // throw your error here
        }
        
        return "Okay!";
    });
```

Here is how it is used to protect Drops:
```java
    public static Route manageModerator = ((request, response) -> {
        String dropId;
        PermissionChecker permissionChecker;
        JacksonMongoCollection<DropModel> collection;
        collection = new CollectionFactory<DropModel>(Main.services.dbService(), DropModel.class).getCollection();

        // .. Other code and variables

        // check we have permission
        permissionChecker = new PermissionChecker(request, new DropPermissionCheckProvider(collection, dropId));
        if (!permissionChecker.verify()) {
            halt(HttpStatus.FORBIDDEN_403, new GeneralResponse(HttpStatus.FORBIDDEN_403,
                    "incorrect permissions").toJSON());
        }

        // ..
        // .. More code for this route
        // ..

        return new GeneralResponse(HttpStatus.OK_200, "successful");
    });
```


To see how it fully works, you can look at [DropController.java](https://github.com/OaklandDevTeam/umbrella/blob/master/src/main/java/com/umbr3114/controllers/DropController.java)