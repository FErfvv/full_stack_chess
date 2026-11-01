package dataaccess.sql;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.UserData;
import model.requests.JoinGameRequest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DataBaseDataAccessTest {
    private static DataBaseDataAccess dbAccess;
    private static AuthData authData;
    private static final String USERNAME = "john";
    private static final String PASSWORD = "password";
    private static int gameID;
    private static final String BAD_AUTH_TOKEN = "ThisISInvalid";

    @BeforeEach
    @Test
    void setUp() throws DataAccessException {
        assertDoesNotThrow(() -> dbAccess = new DataBaseDataAccess());
    }

    @Test
    void clear() {
        assertDoesNotThrow(() -> dbAccess.clear());
    }

    @Test
    @Order(1)
    void createUser() {
        assertDoesNotThrow(() -> dbAccess.createUser(new UserData(USERNAME, PASSWORD, "john@gmail.com")));
    }

    @Test
    @Order(2)
    void getUser() {
        assertDoesNotThrow(() -> dbAccess.getUser(USERNAME));
    }

    @Test
    void createGame() {
        assertDoesNotThrow(() -> dbAccess.createGame("BestGameEver"));
    }

    @Test
    void getGame() {
        assertDoesNotThrow(() -> dbAccess.getGame("1"));
    }

    @Test
    void listGames() {
        assertDoesNotThrow(() -> dbAccess.listGames());
    }

    @Test
    void updateGame() throws DataAccessException {
        dbAccess.createGame("newGame");
        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE.name(), "1");
        assertDoesNotThrow(() -> dbAccess.updateGame(joinGameRequest, new UserData(USERNAME, PASSWORD, "john@gmail.com")));
    }

    @Test
    void createAuth() {
        assertDoesNotThrow(() -> dbAccess.createAuth(new AuthData("authTest", "CreatAuth")));
    }

    @Test
    void getAuth() {
        assertDoesNotThrow(() -> dbAccess.getAuth("authToken"));
    }

    @Test
    void deleteAuth() {
        assertDoesNotThrow(() -> dbAccess.deleteAuth(new AuthData(USERNAME, "authToken")));
    }

    @AfterAll
    static void afterAll() throws DataAccessException {
        dbAccess.clear();
    }
}