package service;

import dataaccess.DataAccess;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.exceptions.UsernameAlreadyTakenException;
import model.AuthData;
import model.UserData;
import model.requests.AuthRequest;
import model.requests.LoginRequest;
import model.requests.RegisterRequest;

import java.util.UUID;

public class UserService {
    private final DataAccess myDataAccess;

    public UserService(DataAccess myDataAccess) {
        this.myDataAccess = myDataAccess;
    }

    public AuthData register(RegisterRequest registerRequest) throws UsernameAlreadyTakenException {
        UserData currentUser = myDataAccess.getUser(registerRequest.username());
        if (currentUser != null) {
            throw new UsernameAlreadyTakenException(String.format("The Username: %s has already been taken", currentUser.username()));
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
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (!myDataAccess.deleteAuth(logoutRequest.authToken())) {
            throw new DataAccessException("Error while deleting auth token");
        }
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
