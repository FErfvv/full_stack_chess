package dataaccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryDataAccess implements DataAccess {
    private final HashMap<String, UserData> users = new HashMap<>();
    private final List<String> authDataHashMap = new ArrayList<>();
    private final HashMap<String, GameData> gameList = new HashMap<>();

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
    public GameData createGame() {
        return null;
    }

    @Override
    public GameData getGame(String gameID) {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() {
        return null;
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
