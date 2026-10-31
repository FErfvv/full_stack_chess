package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.ColorTakenException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.NoGameException;
import kotlin.NotImplementedError;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.requests.JoinGameRequest;
import model.responses.GameListResponseData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DataBaseDataAccess implements DataAccess {

    public DataBaseDataAccess() throws DataAccessException {
        configureDatabase();
    }

    public void clear() {
        throw new NotImplementedError();
    }

    @Override
    public void createUser(UserData currentUser) {

    }

    @Override
    public UserData getUser(String username) {
        throw new NotImplementedError();
    }

    @Override
    public GameData createGame(String gameName) {
        throw new NotImplementedError();
    }

    @Override
    public GameData getGame(String gameID) {
        throw new NotImplementedError();
    }

    @Override
    public List<GameListResponseData> listGames() {
        throw new NotImplementedError();
    }

    @Override
    public void updateGame(JoinGameRequest joinGameRequest, UserData user) throws NoGameException, ColorTakenException, BadRequestException {
        throw new NotImplementedError();
    }

    @Override
    public void createAuth(AuthData authToken) {
        throw new NotImplementedError();
    }

    @Override
    public String getAuth(String authToken) {
        throw new NotImplementedError();
    }

    @Override
    public boolean deleteAuth(AuthData authData) {
        throw new NotImplementedError();
    }


    private final String[][] createStatements = {
            {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            },
            {
            """
            CREATE TABLE IF NOT EXISTS  authData (
              `authToken` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            },
            {
            """
            CREATE TABLE IF NOT EXISTS  gameList (
              `id` int NOT NULL AUTO_INCREMENT,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(id)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            },
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String[] table : createStatements) {
                for (String statement: table) {
                    try (var preparedStatement = conn.prepareStatement(statement)) {
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
