package client;

import com.google.gson.Gson;
import exceptions.BasicResponseException;
import model.AuthData;
import model.requests.AuthRequest;
import model.requests.RegisterRequest;
import model.responses.ListGamesResponse;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CommandHandler {
    private static ServerFacade facade;
    private static String serverUrl;

    public CommandHandler(String url) {
        serverUrl = url;
        facade = new ServerFacade(serverUrl);
    }

    public void register(String[] params) throws BasicResponseException {
        facade.register(params);
    }

    public void login(String[] params) throws BasicResponseException {
        facade.login(params);
    }

    public void logout() throws BasicResponseException {
        facade.logout();
    }

    public void createGame(String[] params) throws BasicResponseException {
        int gameID = facade.createGame(params);
        System.out.println(gameID);
    }

    public ListGamesResponse listGames() throws BasicResponseException {
        return facade.listGames();
    }


}
