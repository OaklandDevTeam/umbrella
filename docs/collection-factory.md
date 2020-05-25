# CollectionFactory usage notes

To access the underlying Mongo Database, you need a reference to a MongoCollection object.

This project uses a library, called MongoJack, which allows you to "save"/"load" Objects to/from the database. MongoJack comes with a class called JacksonMongoCollection.

The Class CollectionFactory is a class that makes it easier to construct JacksonMongoCollection objects.

## Example usage

> You might notice this syntax: `ClassName<SomeOtherClass>()`
> 
> If you haven't seen this syntax before, it is called a Java Generic. [Tutorial on generics](https://www.tutorialspoint.com/java/java_generics.htm)

```java
public class UsersController {

    public void saveUser(UserModel user) {
        JacksonMongoCollection<UserModel> collection;

        collection = createUserCollectionObject();

        // .. ..
        // do stuff for saving user

        collection.insertOne(user);
    }

    private JacksonMongoCollection<UserModel> createUserCollectionObject() {
        CollectionFactory<UserModel> collectionFactory;
        MongoDatabase db = Main.services.dbService();

        // create factory object
        collectionFactory = new CollectionFactory<UserModel>(db, UserModel.class);

        return collectionFactory.getCollection();
    }
}
```

The `createUserCollectionObject()` method shows you how the CollectionFactory is used

> UserModel class (notice the @MongoCollection annotation)

```java
@MongoCollection(name = "users")
public class UserModel {

    public ObjectId _id;
    public String userName;
    public String email;

}
```

Reference [this tutorial](https://mongojack.org/tutorial.html) on how to use MongoJack

For more information on Mongo, the documentation is [here](https://docs.mongodb.com/manual/crud/)

For more information on the Mongo Java Driver, the documentation is [here](https://mongodb.github.io/mongo-java-driver/4.0/driver/tutorials/databases-collections/)