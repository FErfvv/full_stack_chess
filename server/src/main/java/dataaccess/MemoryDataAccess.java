package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import model.responses.GameListResponseData;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryDataAccess implements DataAccess {
    private final HashMap<String, UserData> users = new HashMap<>();
    private final List<String> authDataHashMap = new ArrayList<>();
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
    public void updateGame(String id) {

    }

    @Override
    public void createAuth(String authToken) {
        authDataHashMap.add(authToken);
    }

    @Override
    public boolean getAuth(String authToken) {
        return authDataHashMap.contains(authToken);
    }

    @Override
    public boolean deleteAuth(String authToken) {
        return authDataHashMap.remove(authToken);
    }
}
