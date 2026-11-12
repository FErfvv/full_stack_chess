package client;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptions.BasicResponseException;
import model.AuthData;
import model.requests.AuthRequest;
import model.requests.RegisterRequest;
import model.responses.GameListResponseData;
import model.responses.ListGamesResponse;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CommandHandler {
    private static ServerFacade facade;
    private static String serverUrl;
    private ListGamesResponse gameList;
    private int gameID;
    private ChessGame currentGame;

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
        gameID = facade.createGame(params);
    }

    public ListGamesResponse listGames() throws BasicResponseException {
        gameList = facade.listGames();
        return gameList;
    }

    public ChessGame joinGame(String[] params) throws BasicResponseException {
        int gameId;
        try {
            gameId = Integer.parseInt(params[0]);
        } catch (Exception e) {
            throw new BasicResponseException("{'message':'Error: the game ID needs to be a valid integer'}", 400);
        }
        gameList = facade.listGames();
        if (gameList != null && gameList.getGames().size() >= gameId && gameId > 0) {
            gameId = gameList.getGames().get(gameId - 1).gameID();
        }

        String playerColor = params[1].toUpperCase();
        facade.joinGame(playerColor, Integer.toString(gameId));
        currentGame = new ChessGame();
        return currentGame;
    }

    public GameListResponseData observeGame(String[] params) throws BasicResponseException {
        int gameId = Integer.parseInt(params[0]);
        gameList = facade.listGames();
        if (gameList == null) {
            throw new BasicResponseException("{ 'message': 'Error: there are currently no games to observe.'}", 403);
        }
        if (gameList.getGames().size() >= gameId && gameId > 0) {
            gameId = gameList.getGames().get(gameId - 1).gameID();
        } else {
            throw new BasicResponseException("{ 'message': 'Error: requested game does not exist.'} ", 403);
        }

        return gameList.getGames().get(gameId - 1);
    }


}
