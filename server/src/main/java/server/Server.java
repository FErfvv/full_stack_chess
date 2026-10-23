package server;

import com.google.gson.Gson;
import dataaccess.exceptions.*;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import model.responses.GameListResponseData;

import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("/user", Server::register);
        javalin.post("/session", Server::login);
        javalin.delete("/session", Server::logout);
        javalin.post("/game", Server::createGame);
        javalin.delete("/db", Server::clear);
        javalin.get("/game", Server::listGames);
        javalin.exception(BasicResponseException.class, this::exceptionHandler);

        // Register your endpoints and exception handlers here.

    }

    private void exceptionHandler(BasicResponseException ex, Context ctx) {
        ctx.status(ex.getHttpCode());
        ctx.result(ex.getJsonMessage());
    }

    private static void register(Context ctx) throws BadRequestException, UsernameAlreadyTakenException {
        AuthData myData = ServerHandler.register(ctx.body());
        ctx.contentType("application/json");
        ctx.result(new Gson().toJson(Map.of("username", myData.username(), "authToken", myData.authToken())));
        ctx.status(200);
    }

    private static void login(Context ctx) throws UnauthorizedException, BadRequestException {
        AuthData myData = ServerHandler.login(ctx.body());
        ctx.contentType("application/json");
        ctx.result(new Gson().toJson(Map.of("username", myData.username(), "authToken", myData.authToken())));
        ctx.status(200);
    }

    private static void logout(Context ctx) throws UnauthorizedException, BadRequestException, DataAccessException {
        ServerHandler.logout(Objects.requireNonNull(ctx.header("Authorization")));
        ctx.status(200);
    }

    private static void createGame(Context ctx) throws UnauthorizedException, BadRequestException {
        int gameId = ServerHandler.createGame(Objects.requireNonNull(ctx.header("Authorization")), ctx.body());
        ctx.result(new Gson().toJson(Map.of("gameID", gameId)));
        ctx.status(200);
    }

    private static void listGames(Context ctx) throws UnauthorizedException, BadRequestException {
        List<GameListResponseData> gameDataList = ServerHandler.listGames(Objects.requireNonNull(ctx.header("Authorization")));
        ctx.result(new Gson().toJson(Map.of("games", gameDataList)));
        ctx.status(200);
    }

    private static void clear(Context ctx) {
        ServerHandler.clearDb();
        ctx.status(200);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }


}
