package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.ColorTakenException;
import dataaccess.exceptions.NoGameException;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.requests.JoinGameRequest;
import model.responses.GameListResponseData;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MemoryDataAccess implements DataAccess {
    private final HashMap<String, UserData> users = new HashMap<>();
    private final List<AuthData> authDataHashMap = new ArrayList<>();
    private final List<GameData> gameList = new ArrayList<>();

    public void clear() {
        users.clear();
        authDataHashMap.clear();
        gameList.clear();
    }

    @Override
    public void createUser(UserData currentUser) {
        users.put(currentUser.username(), currentUser);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public GameData createGame(String gameName) {
        int gameId = gameList.size() + 1;
        GameData newGame = new GameData(gameId, "", "", gameName, new ChessGame());
        gameList.add(newGame);
        return newGame;
    }

    @Override
    public GameData getGame(String gameID) {
        return null;
    }

    @Override
    public List<GameListResponseData> listGames() {
        List<GameListResponseData> responseData = new ArrayList<>();
        for (GameData game : gameList) {
            responseData.add(new GameListResponseData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return responseData;
    }

    @Override
    public void updateGame(JoinGameRequest joinGameRequest, UserData user) throws NoGameException, ColorTakenException {
        GameData targetGame = null;
        for (GameData game : gameList) {
            if (game.gameID() == Integer.parseInt(joinGameRequest.gameID())) {
                targetGame = game;
                break;
            }
        }
        if (targetGame == null) {
            throw new NoGameException();
        }
        if (joinGameRequest.playerColor().equals("black") && targetGame.blackUsername().isEmpty()) {
            targetGame.setBlackUsername(user.username());
        } else if (joinGameRequest.playerColor().equals("white") && targetGame.whiteUsername().isEmpty()) {
            targetGame.setWhiteUsername(user.username());
        } else {
            throw new ColorTakenException();
        }
    }

    @Override
    public void createAuth(AuthData authToken) {
        authDataHashMap.add(authToken);
    }

    @Override
    public String getAuth(String authToken) {
        for (AuthData auth : authDataHashMap) {
            if (auth.authToken().equals(authToken)) {
                return auth.username();
            }
        }
        return null;
    }

    @Override
    public boolean deleteAuth(AuthData authData) {
        return authDataHashMap.remove(authData);
    }
}
