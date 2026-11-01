package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccess;
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
import java.util.ArrayList;
import java.util.List;

public class DataBaseDataAccess implements DataAccess {

    public DataBaseDataAccess() throws DataAccessException {
        configureDatabase();
    }

    public void clear() throws DataAccessException {
        String[] statementList = {"TRUNCATE users", "TRUNCATE authData", "TRUNCATE gameList"};

        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : statementList) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Error while trying to create a User: %s", e.toString()));
        }


    }

    @Override
    public void createUser(UserData currentUser) throws DataAccessException {
        var statement = "INSERT INTO users (username, json) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, currentUser.username());
                String json = new Gson().toJson(currentUser);
                preparedStatement.setString(2, json);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Error while trying to create a User: %s", e.toString()));
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String statement = "SELECT json FROM users WHERE username=?";
        UserData user = null;
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (var response = preparedStatement.executeQuery()) {
                    while (response.next()) {
                        user = new Gson().fromJson(response.getString("json"), UserData.class);

                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Error while trying to get a User: %s", e.toString()));
        }
        return user;
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        GameData gameData;
        int gameID = -1;
        try (Connection conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement("SELECT COUNT(*) FROM gameList");
            var response = preparedStatement.executeQuery();
            while (response.next()) {
                gameID = response.getInt(1);
            }
            if (gameID != -1) {
                gameData = new GameData(gameID + 1, null, null, gameName, new ChessGame());
                String gameDataJson = new Gson().toJson(gameData);
                preparedStatement = conn.prepareStatement(String.format("INSERT INTO gameList (json) VALUES ('%s')", gameDataJson));
                preparedStatement.execute();
                return gameData;
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Error while trying to get a User: %s", e.toString()));
        }
        return null;
    }

    @Override
    public GameData getGame(String gameID) throws DataAccessException {
        String statement = "SELECT json FROM gameList WHERE id=?";
        GameData game = null;
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, gameID);
                try (var response = preparedStatement.executeQuery()) {
                    while (response.next()) {
                        game = new Gson().fromJson(response.getString("json"), GameData.class);

                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Error while trying to get a User: %s", e.toString()));
        }
        return game;
    }

    @Override
    public List<GameListResponseData> listGames() throws DataAccessException {
        String statement = "SELECT json FROM gameList";
        List<GameListResponseData> responseData = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (var response = preparedStatement.executeQuery()) {
                    while (response.next()) {
                        GameData gameData = new Gson().fromJson(response.getString("json"), GameData.class);
                        responseData.add(new GameListResponseData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName()));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Error while trying to get a User: %s", e.toString()));
        }
        return responseData;
    }

    @Override
    public void updateGame(JoinGameRequest joinGameRequest, UserData user) throws NoGameException, ColorTakenException, BadRequestException {
        throw new NotImplementedError();
    }

    @Override
    public void createAuth(AuthData authToken) throws DataAccessException {
        var statement = "INSERT INTO authData (authToken, json) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken.authToken());
                String json = new Gson().toJson(authToken);
                preparedStatement.setString(2, json);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Error while trying to save AuthData: %s", e.toString()));
        }
    }

    @Override
    public String getAuth(String authToken) throws DataAccessException {

        String statement = "SELECT json FROM authData WHERE authToken=?";
        String username = null;
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                try (var response = preparedStatement.executeQuery()) {
                    while (response.next()) {
                        AuthData authData = new Gson().fromJson(response.getString("json"), AuthData.class);
                        username = authData.username();
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Error while trying to get a User: %s", e.toString()));
        }
        return username;
    }

    @Override
    public boolean deleteAuth(AuthData authData) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM authData WHERE authToken=?")) {
                preparedStatement.setString(1, authData.authToken());
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Error while trying to get a User: %s", e.toString()));
        }
    }


    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  authData (
              `authToken` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  gameList (
              `id` int NOT NULL AUTO_INCREMENT,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(id)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
