package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.exceptions.*;
import model.AuthData;
import model.requests.*;
import model.responses.GameListResponseData;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    private static final DataAccess MEMORY_DATA_ACCESS = new MemoryDataAccess();
    private static final UserService USER_SERVICE = new UserService(MEMORY_DATA_ACCESS);
    private static AuthData authData;
    private static final String USERNAME = "john";
    private static final String PASSWORD = "password";
    private static int gameID;
    private static final String BAD_AUTH_TOKEN = "ThisISInvalid";

    @Test
    @Order(1)
    void registerNew() {
        RegisterRequest request = new RegisterRequest(USERNAME, PASSWORD, "jeff@gmail.com");
        assertDoesNotThrow(() -> authData = USER_SERVICE.register(request));
    }

    @Test
    @Order(2)
    void registerDuplicateUsername() throws UsernameAlreadyTakenException {
        RegisterRequest request = new RegisterRequest(USERNAME, PASSWORD, "jeff@gmail.com");
        assertThrows(UsernameAlreadyTakenException.class, () -> USER_SERVICE.register(request));
    }

    @Test
    @Order(3)
    void validLogout() throws UsernameAlreadyTakenException {
        AuthRequest authRequest = new AuthRequest(authData.authToken());
        assertThrows(UnauthorizedException.class, () -> USER_SERVICE.logout(new AuthRequest(BAD_AUTH_TOKEN)));
        assertDoesNotThrow(() -> USER_SERVICE.logout(authRequest));
    }

    @Test
    @Order(4)
    void loginSuccessful() {
        LoginRequest request = new LoginRequest(USERNAME, PASSWORD);
        assertDoesNotThrow(() -> authData = USER_SERVICE.login(request));
    }

    @Test
    @Order(5)
    void loginBadUsernamePassword() throws UnauthorizedException, DataAccessException {
        USER_SERVICE.logout(new AuthRequest(authData.authToken()));
        assertThrows(UnauthorizedException.class, () -> USER_SERVICE.login(new LoginRequest("james", PASSWORD)));
        assertThrows(UnauthorizedException.class, () -> USER_SERVICE.login(new LoginRequest(USERNAME, "notPassword")));
    }

    @Test
    @Order(6)
    void createGame() throws UnauthorizedException {
        authData = USER_SERVICE.login(new LoginRequest(USERNAME, PASSWORD));
        assertThrows(UnauthorizedException.class, () -> USER_SERVICE.createGame(new AuthRequest(BAD_AUTH_TOKEN), new NewGameRequest("My Game")));
        assertDoesNotThrow(() -> gameID = USER_SERVICE.createGame(new AuthRequest(authData.authToken()), new NewGameRequest("game1")));
    }

    @Test
    @Order(7)
    void listGames() throws UnauthorizedException {
        List<GameListResponseData> expectedGameList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            expectedGameList.add(new GameListResponseData(i, null, null, String.format("game%s", i)));
        }
        for (GameListResponseData game : expectedGameList) {
            if (game.gameID() == 1) {
                continue;
            }
            USER_SERVICE.createGame(new AuthRequest(authData.authToken()), new NewGameRequest(game.gameName()));
        }

        List<GameListResponseData> actualGameList = USER_SERVICE.listGames(new AuthRequest(authData.authToken()));
        assertEquals(actualGameList, expectedGameList);
        assertThrows(UnauthorizedException.class, () -> USER_SERVICE.listGames(new AuthRequest(BAD_AUTH_TOKEN)));
    }

    @Test
    @Order(8)
    void addUserToGame() {
        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE.name(), "1");
        JoinGameRequest invalidJoinGameRequest = new JoinGameRequest("white", "-1");
        assertDoesNotThrow(() -> USER_SERVICE.addUserToGame(new AuthRequest(authData.authToken()), joinGameRequest));
        assertThrows(ColorTakenException.class, () -> USER_SERVICE.addUserToGame(new AuthRequest(authData.authToken()), joinGameRequest));
        assertThrows(UnauthorizedException.class, () -> USER_SERVICE.addUserToGame(new AuthRequest(BAD_AUTH_TOKEN), joinGameRequest));
        assertThrows(NoGameException.class, () -> USER_SERVICE.addUserToGame(new AuthRequest(authData.authToken()), invalidJoinGameRequest));
    }
}