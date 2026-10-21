package server;

import com.google.gson.Gson;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.UsernameAlreadyTakenException;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthToken;
import model.UserData;
import org.w3c.dom.UserDataHandler;

import java.util.Map;


public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("/user", Server::register);
        javalin.delete("/db", ctx -> ctx.result("{}"));
        // Register your endpoints and exception handlers here.

    }

    private static void register(Context ctx) {
        AuthToken myData = null;
        Map<String, String> result = null;
        int status = 200;
        try {
            myData = ServerHandler.register(ctx.body());
        } catch (UsernameAlreadyTakenException e) {
            result = Map.of("message", "Error: username already taken");
            status = 403;
        } catch (BadRequestException e) {
            result = Map.of("message", "Error: bad request");
            status = 400;
        }

        if (myData != null) {
            result = Map.of("username", myData.username(), "authToken", myData.authToken());
        }
        ctx.contentType("application/json");
        ctx.result(new Gson().toJson(result));
        ctx.status(status);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
