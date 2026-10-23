package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.exceptions.*;
import model.AuthData;
import model.GameData;
import model.requests.*;
import model.responses.GameListResponseData;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    private static final DataAccess testDataAccess = new MemoryDataAccess();
    private static final UserService service = new UserService(testDataAccess);
    private static AuthData authData;
    private static final String username = "john";
    private static final String password = "password";
    private static int gameID;
    private static final String badAuthToken = "ThisISInvalid";

    @Test
    @Order(1)
    void registerNew() {
        RegisterRequest request = new RegisterRequest(username, password, "jeff@gmail.com");
        assertDoesNotThrow(() -> authData = service.register(request));
    }

    @Test
    @Order(2)
    void registerDuplicateUsername() throws UsernameAlreadyTakenException {
        RegisterRequest request = new RegisterRequest(username, password, "jeff@gmail.com");
        assertThrows(UsernameAlreadyTakenException.class, () -> service.register(request));
    }

    @Test
    @Order(3)
    void validLogout() throws UsernameAlreadyTakenException {
        AuthRequest authRequest = new AuthRequest(authData.authToken());
        assertThrows(UnauthorizedException.class, () -> service.logout(new AuthRequest(badAuthToken)));
        assertDoesNotThrow(() -> service.logout(authRequest));
    }

    @Test
    @Order(4)
    void loginSuccessful() {
        LoginRequest request = new LoginRequest(username, password);
        assertDoesNotThrow(() -> authData = service.login(request));
    }

    @Test
    @Order(5)
    void loginBadUsernamePassword() throws UnauthorizedException, DataAccessException {
        service.logout(new AuthRequest(authData.authToken()));
        assertThrows(UnauthorizedException.class, () -> service.login(new LoginRequest("james", password)));
        assertThrows(UnauthorizedException.class, () -> service.login(new LoginRequest(username, "notPassword")));
    }

    @Test
    @Order(6)
    void createGame() throws UnauthorizedException {
        authData = service.login(new LoginRequest(username, password));
        assertThrows(UnauthorizedException.class, () -> service.createGame(new AuthRequest(badAuthToken), new NewGameRequest("My Game")));
        assertDoesNotThrow(() -> gameID = service.createGame(new AuthRequest(authData.authToken()), new NewGameRequest("game1")));
    }

    @Test
    @Order(7)
    void listGames() throws UnauthorizedException {
        List<GameListResponseData> expectedGameList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            expectedGameList.add(new GameListResponseData(i, "", "", String.format("game%s", i)));
        }
        for (GameListResponseData game : expectedGameList) {
            if (game.gameID() == 1) {
                continue;
            }
            service.createGame(new AuthRequest(authData.authToken()), new NewGameRequest(game.gameName()));
        }

        List<GameListResponseData> actualGameList = service.listGames(new AuthRequest(authData.authToken()));
        assertEquals(actualGameList, expectedGameList);
    }

    @Test
    @Order(8)
    void addUserToGame() {
        JoinGameRequest joinGameRequest = new JoinGameRequest("white", "1");
        JoinGameRequest invalidJoinGameRequest = new JoinGameRequest("white", "-1");
        assertDoesNotThrow(() -> service.addUserToGame(new AuthRequest(authData.authToken()), joinGameRequest));
        assertThrows(ColorTakenException.class, () -> service.addUserToGame(new AuthRequest(authData.authToken()), joinGameRequest));
        assertThrows(UnauthorizedException.class, () -> service.addUserToGame(new AuthRequest(badAuthToken), joinGameRequest));
        assertThrows(NoGameException.class, () -> service.addUserToGame(new AuthRequest(authData.authToken()), invalidJoinGameRequest));

    }
}