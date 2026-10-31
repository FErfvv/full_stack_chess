package dataaccess.sql;

import dataaccess.DataBaseDataAccess;
import dataaccess.DatabaseManager;
import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DataBaseDataAccessTest {
    private DataBaseDataAccess dbAccess;
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
    }

    @Test
    void getGame() {
    }

    @Test
    void listGames() {
    }

    @Test
    void updateGame() {
    }

    @Test
    void createAuth() {
    }

    @Test
    void getAuth() {
    }

    @Test
    void deleteAuth() {
    }
}