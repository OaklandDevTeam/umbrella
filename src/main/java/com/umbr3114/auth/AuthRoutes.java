package com.umbr3114.auth;

import com.mongodb.client.MongoDatabase;
import com.umbr3114.ServiceLocator;
import com.umbr3114.data.CollectionFactory;
import org.eclipse.jetty.http.HttpStatus;
import org.mongojack.JacksonMongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import java.util.Optional;

import static spark.Spark.halt;

public class AuthRoutes {

    public static Route registerUser = ((request, response) -> {
        JacksonMongoCollection<UserModel> userCollection = createUserCollection();
        UserModel user;
        Optional<String> userName = Optional.ofNullable(request.queryParams("username"));
        Optional<String> passwordRaw = Optional.ofNullable(request.queryParams("password"));
        Optional<String> email = Optional.ofNullable(request.queryParams("email"));

        UserManager userManager = new MongoUserManager(userCollection,
                new SparkSessionManager(request, response),
                new BCryptPasswordHasher());

        if (!(userName.isPresent() && passwordRaw.isPresent() && email.isPresent())) {
            halt(HttpStatus.BAD_REQUEST_400,
                    new AuthResponseModel(HttpStatus.BAD_REQUEST_400, "Invalid registration form")
                            .toJSON()
            );
        }

        user = new UserModel(userName.get(), email.get(), passwordRaw.get());
        userManager.register(user);

        return new AuthResponseModel(HttpStatus.OK_200, user.getUsername()).toJSON();
    });

    public static Route loginUser = ((request, response) -> {
        Logger log = LoggerFactory.getLogger("AuthRoutes-Login");
        Optional<String> username = Optional.ofNullable(request.queryParams("username"));
        Optional<String> rawPassword = Optional.ofNullable(request.queryParams("password"));

        UserManager userManager;

        if (!(username.isPresent() && rawPassword.isPresent())) {
            // error out
            halt(HttpStatus.BAD_REQUEST_400,
                    new AuthResponseModel(HttpStatus.BAD_REQUEST_400, "Bad Credentials")
                            .toJSON());
        }

        userManager = new MongoUserManager(createUserCollection(),
                new SparkSessionManager(request, response),
                new BCryptPasswordHasher());

        if (!userManager.login(username.get(), rawPassword.get())) {
            log.info(String.format("Login failed for user: %s", username.get()));
            halt(HttpStatus.FORBIDDEN_403,
                    new AuthResponseModel(HttpStatus.FORBIDDEN_403, "Cannot Authenticate")
                    .toJSON());
        }

        return new AuthResponseModel(HttpStatus.OK_200, username.get())
                .toJSON();
    });


    public static Route logoutUser = ((request, response) -> {
        SessionManager sessionManager = new SparkSessionManager(request, response);
        sessionManager.invalidateSession();
        return new AuthResponseModel(HttpStatus.OK_200, "successful").toJSON();
    });

    /**
     * @deprecated
     */
    public static Route protectedRoute = ((request, response) -> {
        return "Now authenticated!";
    });

    /**
     * Helper to create a mongo collection for a UserModel
     * @return
     */
    private static JacksonMongoCollection<UserModel> createUserCollection() {
        MongoDatabase db = ServiceLocator.getService().dbService();
        CollectionFactory<UserModel> userModelCollectionFactory;

        userModelCollectionFactory = new CollectionFactory<>(db, UserModel.class);
        return userModelCollectionFactory.getCollection();
    }
}
