package service;

import dataaccess.DataAccess;
import dataaccess.exceptions.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.requests.*;
import model.responses.GameListResponseData;

import java.util.List;
import java.util.UUID;

public class UserService {
    private final DataAccess myDataAccess;

    public UserService(DataAccess myDataAccess) {
        this.myDataAccess = myDataAccess;
    }

    public AuthData register(RegisterRequest registerRequest) throws UsernameAlreadyTakenException {
        UserData currentUser = myDataAccess.getUser(registerRequest.username());
        if (currentUser != null) {
            throw new UsernameAlreadyTakenException(currentUser.username());
        }
        currentUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        myDataAccess.createUser(currentUser);
        AuthData myAuthData = new AuthData(currentUser.username(), generateToken());
        myDataAccess.createAuth(myAuthData);
        return myAuthData;
    }

    public AuthData login(LoginRequest loginRequest) throws UnauthorizedException {
        UserData currentUser = myDataAccess.getUser(loginRequest.username());
        if (currentUser == null) {
            throw new UnauthorizedException(String.format("Username '%s' does not exist", loginRequest.username()));
        }
        if (!currentUser.password().equals(loginRequest.password())) {
            throw new UnauthorizedException(String.format("Password '%s' is incorrect", loginRequest.username()));
        }
        AuthData myAuthData = new AuthData(currentUser.username(), generateToken());
        myDataAccess.createAuth(myAuthData);
        return myAuthData;
    }

    public void logout(AuthRequest logoutRequest) throws UnauthorizedException, DataAccessException {
        String username = myDataAccess.getAuth(logoutRequest.authToken());
        if (username == null) {
            throw new UnauthorizedException("unauthorized");
        }
        if (!myDataAccess.deleteAuth(new AuthData(username, logoutRequest.authToken()))) {
            throw new DataAccessException("problem while deleting auth token");
        }
    }

    public int createGame(AuthRequest authRequest, NewGameRequest gameRequest) throws UnauthorizedException {
        if (myDataAccess.getAuth(authRequest.authToken()) == null) {
            throw new UnauthorizedException("unauthorized");
        }
        GameData newGame = myDataAccess.createGame(gameRequest.gameName());
        return newGame.gameID();
    }

    public void addUserToGame(AuthRequest authRequest, JoinGameRequest joinGameRequest)
            throws UnauthorizedException,
            ColorTakenException,
            NoGameException,
            BadRequestException {
        String username = myDataAccess.getAuth(authRequest.authToken());
        if (username == null) {
            throw new UnauthorizedException("unauthorized");
        }
        UserData user = myDataAccess.getUser(username);
        myDataAccess.updateGame(joinGameRequest, user);
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public List<GameListResponseData> listGames(AuthRequest authRequest) throws UnauthorizedException {
        if (myDataAccess.getAuth(authRequest.authToken()) == null) {
            throw new UnauthorizedException("unauthorized");
        }
        return myDataAccess.listGames();
    }
}
