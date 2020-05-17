package com.umbr3114;

import com.umbr3114.auth.AuthCheck;
import com.umbr3114.auth.AuthRoutes;

import static spark.Spark.*;

public class Main {
    public static ServiceLocator services;
    public static void main(String[] args) {
        services = ServiceLocator.getService();

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
