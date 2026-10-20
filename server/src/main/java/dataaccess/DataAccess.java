package dataaccess;

import model.AuthToken;
import model.GameData;
import model.UserData;

import java.util.ArrayList;

public interface DataAccess {

    // A method for clearing all data from the database. This is used during testing.
    public void clear();

    // Create a new user.
    public void createUser(UserData currentUser);

    // Retrieve a user with the given username.
    public UserData getUser(String username);

    // Create a new game.
    public GameData createGame();

    // Retrieve a specified game with the given game ID.
    public GameData getGame(String gameID);

    // Retrieve all games.
    public ArrayList<GameData> listGames();

    // This is used when players join a game or when a move is made.
    public void updateGame(String id);

    // Create a new authorization.
    public void createAuth(AuthToken authToken);

    // Retrieve an authorization given an authToken.
    public boolean getAuth(AuthToken authToken);

    // Delete an authorization so that it is no longer valid.
    public void deleteAuth(AuthToken authToken);

}
