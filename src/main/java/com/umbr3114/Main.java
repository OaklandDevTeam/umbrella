package com.umbr3114;

import com.umbr3114.auth.AuthCheck;
import com.umbr3114.auth.AuthRoutes;
import com.umbr3114.common.JsonResponse;
import com.umbr3114.controllers.DropController;
import com.umbr3114.controllers.PostController;
import com.umbr3114.controllers.SubscriptionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

public class Main {
    public static ServiceLocator services;
    public static Logger log = LoggerFactory.getLogger("ApplicationMain");

    public static void main(String[] args) {
        services = ServiceLocator.getService();

        /*
         * Set port and static HTML folder.
         */

        if(System.getenv("UMBRELLA_DEBUG") != null) {
            port(8080);
        } else {
            port(80);
        }
        staticFileLocation("/static");

        // allow API calls from any origin. This should be temporary until a proper strategy for production is implemented
        before("/*", (request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        after("/*", (request, response) -> response.type("application/json"));


        /*
         * Protect any route with this before declaration and an AuthCheck object
         * just match the route in the before and Route declaration
         */
        before("/protectedRoute", new AuthCheck());
        get("/protectedRoute", AuthRoutes.protectedRoute);


        /*
         * Auth endpoints
         */

        post("/register", AuthRoutes.registerUser, new JsonResponse());
        post("/login", AuthRoutes.loginUser, new JsonResponse());
        get("/logout", AuthRoutes.logoutUser, new JsonResponse());
        before("/user/info", new AuthCheck());
        get("/user/info", AuthRoutes.userInfo, new JsonResponse());

        before("/drops/delete", new AuthCheck());
        before("/drops/update", new AuthCheck());
        put("/drops/update", DropController.updateDrop, new JsonResponse());
        delete("/drops/delete", DropController.deleteDrop, new JsonResponse());

        before("/drops/create", new AuthCheck());
        post("/drops/create", DropController.addDrop, new JsonResponse());
        get("/drops/list", DropController.listDrops, new JsonResponse());
        before("/drops/managemod", new AuthCheck());
        post("/drops/managemod", DropController.manageModerator, new JsonResponse());
        delete("/drops/managemod", DropController.manageModerator, new JsonResponse());

        //endpoint to show the data for a specific drop
        get("/drops/:dropid", DropController.viewADrop, new JsonResponse());


        /*
         * to protect the subscription routes
         */
        before("/user/subscribe", new AuthCheck());
        before("/user/unsubscribe", new AuthCheck());
        before("/user/subscribed", new AuthCheck());

        /*
         * subscription endpoints
         */
        post("/user/subscribe", SubscriptionController.subscribe, new JsonResponse());
        post("/user/unsubscribe", SubscriptionController.unsubscribe, new JsonResponse());
        get("/user/subscribed", SubscriptionController.subscribed, new JsonResponse());

        /*
         * Post management routes
         */
        before("/posts/create", new AuthCheck());
        post("/posts/create", PostController.savePosts, new JsonResponse());
        get("/posts/:drop/list", PostController.listPosts, new JsonResponse());

        /*
         * endpoints to modify/delete posts
         */
        before("/posts/modify", new AuthCheck());
        before("/posts/delete", new AuthCheck());
        put("/posts/modify",PostController.modifyPosts,new JsonResponse());
        delete("/posts/delete",PostController.deletePosts,new JsonResponse());

        //endpoint to show the data for a specific post
        get("/posts/:postid", PostController.viewAPost, new JsonResponse());

        /*
         * Application should be running now
         */
        log.info(
                "\n\n\n========================================================================================\n"
                + "\n" +
                "   __  __                __                       __   __        \n" +
                "  / / / /  ____ ___     / /_     _____   ___     / /  / /  ____ _\n" +
                " / / / /  / __ `__ \\   / __ \\   / ___/  / _ \\   / /  / /  / __ `/\n" +
                "/ /_/ /  / / / / / /  / /_/ /  / /     /  __/  / /  / /  / /_/ / \n" +
                "\\____/  /_/ /_/ /_/  /_.___/  /_/      \\___/  /_/  /_/   \\__,_/  \n" +
                "                                                                 \n" +
                "\n========================================================================================\n"
        );
        log.info("Application started\n\n");
    }
}
