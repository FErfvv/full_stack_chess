package dataaccess;

import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.ColorTakenException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.NoGameException;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.requests.AuthRequest;
import model.requests.JoinGameRequest;
import model.responses.GameListResponseData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface DataAccess {

    // A method for clearing all data from the database. This is used during testing.
    public void clear() throws DataAccessException;

    // Create a new user.
    public void createUser(UserData currentUser) throws DataAccessException;

    // Retrieve a user with the given username.
    public UserData getUser(String username) throws DataAccessException;

    // Create a new game.
    public GameData createGame(String gameName) throws DataAccessException;

    // Retrieve a specified game with the given game ID.
    public GameData getGame(String gameID) throws DataAccessException;

    // Retrieve all games.
    public List<GameListResponseData> listGames() throws DataAccessException;

    // This is used when players join a game or when a move is made.
    public void updateGame(JoinGameRequest joinGameRequest, UserData userData) throws NoGameException, ColorTakenException, BadRequestException, DataAccessException;

    // Create a new authorization.
    public void createAuth(AuthData authToken) throws DataAccessException;

    // Retrieve an authorization given an authToken.
    public String getAuth(String authToken) throws DataAccessException;

    // Delete an authorization so that it is no longer valid.
    public boolean deleteAuth(AuthData authData) throws DataAccessException;

}
