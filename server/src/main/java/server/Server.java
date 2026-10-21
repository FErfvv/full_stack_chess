package server;

import com.google.gson.Gson;
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
        AuthToken myData = ServerHandler.register(ctx.body());
        var result = Map.of("username", "", "authToken", "");
        ctx.result(new Gson().toJson(result));

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
