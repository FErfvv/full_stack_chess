package service;

import dataaccess.DataAccess;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.exceptions.UsernameAlreadyTakenException;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.requests.AuthRequest;
import model.requests.LoginRequest;
import model.requests.NewGameRequest;
import model.requests.RegisterRequest;
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
        myDataAccess.createAuth(myAuthData.authToken());
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
        myDataAccess.createAuth(myAuthData.authToken());
        return myAuthData;
    }

    public void logout(AuthRequest logoutRequest) throws UnauthorizedException, DataAccessException {
        if (!myDataAccess.getAuth(logoutRequest.authToken())) {
            throw new UnauthorizedException("unauthorized");
        }
        if (!myDataAccess.deleteAuth(logoutRequest.authToken())) {
            throw new DataAccessException("problem while deleting auth token");
        }
    }

    public int createGame(AuthRequest authRequest, NewGameRequest gameRequest) throws UnauthorizedException {
        if (!myDataAccess.getAuth(authRequest.authToken())) {
            throw new UnauthorizedException("unauthorized");
        }
        GameData newGame = myDataAccess.createGame(gameRequest.gameName());
        return newGame.gameID();
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public List<GameListResponseData> listGames(AuthRequest authRequest) throws UnauthorizedException {
        if (!myDataAccess.getAuth(authRequest.authToken())) {
            throw new UnauthorizedException("unauthorized");
        }
        return myDataAccess.listGames();
    }
}
