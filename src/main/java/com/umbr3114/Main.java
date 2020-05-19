package com.umbr3114;

import com.umbr3114.auth.AuthCheck;
import com.umbr3114.auth.AuthRoutes;

import static spark.Spark.*;

public class Main {
    public static ServiceLocator services;
    public static void main(String[] args) {
        services = ServiceLocator.getService();

        /*
         * Set port and static HTML folder.
         */
        port(80);
        staticFileLocation("/static");

        // allow API calls from any origin. This should be temporary until a proper strategy for production is implemented
        before("/*", (request, response) -> response.header("Access-Control-Allow-Origin", "*"));


        /*
         * Protect any route with this before declaration and an AuthCheck object
         * just match the route in the before and Route declaration
         */
        before("/protectedRoute", new AuthCheck());
        get("/protectedRoute", AuthRoutes.protectedRoute);


        /*
         * Auth endpoints
         */
        post("/register", AuthRoutes.registerUser);
        post("/login", AuthRoutes.loginUser);
        get("/logout", AuthRoutes.logoutUser);
    }
}
