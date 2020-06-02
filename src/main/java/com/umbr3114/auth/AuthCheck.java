package com.umbr3114.auth;

import com.umbr3114.common.GeneralResponse;
import org.eclipse.jetty.http.HttpStatus;
import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

public class AuthCheck implements Filter {

    @Override
    public void handle(Request request, Response response) throws Exception {
        SessionManager sessionManager = new SparkSessionManager(request, response);
        if (!sessionManager.isAuthorized()) {
            halt(HttpStatus.FORBIDDEN_403, new GeneralResponse(HttpStatus.FORBIDDEN_403, "Unauthorized").toJSON());
        }
    }
}
