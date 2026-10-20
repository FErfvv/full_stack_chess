package dataaccess;

import model.AuthToken;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
    private HashMap<String, UserData> users = new HashMap<>();

    public void clear() {

    }

    @Override
    public UserData createUser() {
        return null;
    }

    @Override
    public UserData getUser(String username) {
        return null;
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
    public AuthToken createAuth() {
        return null;
    }

    @Override
    public boolean getAuth(AuthToken authToken) {
        return false;
    }

    @Override
    public void deleteAuth(AuthToken authToken) {

    }
}
