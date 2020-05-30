package com.umbr3114.auth;

import com.mongodb.client.MongoDatabase;
import com.umbr3114.ServiceLocator;
import com.umbr3114.common.GeneralResponse;
import com.umbr3114.common.RequestParamHelper;
import com.umbr3114.data.CollectionFactory;
import org.eclipse.jetty.http.HttpStatus;
import org.mongojack.JacksonMongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import java.util.regex.Pattern;

import static spark.Spark.halt;

public class AuthRoutes {

    public static final int USERNAME_LENGTH_LIMIT = 20;
    public static final int PASSWORD_LENGTH_LIMIT = 30;
    public static final int EMAIL_LENGTH_LIMIT = 50;
    public static final String EMAIL_VALIDIATION_PATTERN = "^[^\\s@]+@[^\\s@]+.[^\\s@]+$";

    public static Route registerUser = ((request, response) -> {
        JacksonMongoCollection<UserModel> userCollection = createUserCollection();
        RequestParamHelper params = new RequestParamHelper(request);
        UserModel user;
        String userName = params.valueOf("username");
        String passwordRaw = params.valueOf("password");
        String email = params.valueOf("email");

        UserManager userManager = new MongoUserManager(userCollection,
                new SparkSessionManager(request, response),
                new BCryptPasswordHasher());

        // input validation checks
        if (!(validateUsername(userName) &&
                validateRawPassword(passwordRaw) &&
                validateEmail(email))) {
            halt(HttpStatus.BAD_REQUEST_400,
                    new GeneralResponse(HttpStatus.BAD_REQUEST_400, "invalid registration form")
                            .toJSON()
            );
        }

        user = new UserModel(userName, email, passwordRaw);

        try {
            userManager.register(user);
        } catch(DuplicateUserException e) {
            halt(HttpStatus.BAD_REQUEST_400,
                    new GeneralResponse(HttpStatus.BAD_REQUEST_400, "duplicate username registration attempt").toJSON());
        }

        return new GeneralResponse(HttpStatus.OK_200, user.getUsername());
    });

    public static Route loginUser = ((request, response) -> {
        Logger log = LoggerFactory.getLogger("AuthRoutes-Login");
        RequestParamHelper params = new RequestParamHelper(request);
        String username = params.valueOf("username");
        String rawPassword = params.valueOf("password");
        UserManager userManager;

        if (!(validateUsername(username) && validateRawPassword(rawPassword))) {
            // error out
            halt(HttpStatus.BAD_REQUEST_400,
                    new GeneralResponse(HttpStatus.BAD_REQUEST_400, "bad credentials")
                            .toJSON());
        }

        userManager = new MongoUserManager(createUserCollection(),
                new SparkSessionManager(request, response),
                new BCryptPasswordHasher());

        if (!userManager.login(username, rawPassword)) {
            log.info(String.format("Login failed for user: %s", username));
            halt(HttpStatus.FORBIDDEN_403,
                    new GeneralResponse(HttpStatus.FORBIDDEN_403, "cannot authenticate")
                    .toJSON());
        }

        return new GeneralResponse(HttpStatus.OK_200, username);
    });


    public static Route logoutUser = ((request, response) -> {
        SessionManager sessionManager = new SparkSessionManager(request, response);
        sessionManager.invalidateSession();
        return new GeneralResponse(HttpStatus.OK_200, "successful");
    });


    public static Route protectedRoute = ((request, response) -> {
        return "Now authenticated!";
    });

    private static JacksonMongoCollection<UserModel> createUserCollection() {
        MongoDatabase db = ServiceLocator.getService().dbService();
        CollectionFactory<UserModel> userModelCollectionFactory;

        userModelCollectionFactory = new CollectionFactory<>(db, UserModel.class);
        return userModelCollectionFactory.getCollection();
    }

    public static boolean validateUsername(String user) {
        if (user == null) // if statements have to be broken up because of null check
            return false;
        if (user.contains(" "))
            return false;

        return user.length() <= USERNAME_LENGTH_LIMIT;
    }

    public static boolean validateEmail(String email) {
        Pattern matchPattern = Pattern.compile(EMAIL_VALIDIATION_PATTERN);

        if (email == null)
            return false;
        if (email.length() > EMAIL_LENGTH_LIMIT)
            return false;
        return matchPattern.matcher(email).matches();
    }

    public static boolean validateRawPassword(String password) {

        if (password == null)
            return false;
        return !(password.length() > PASSWORD_LENGTH_LIMIT);
    }
}
