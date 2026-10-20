package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.exceptions.UsernameAlreadyTakenException;
import jdk.jshell.spi.ExecutionControl;
import model.AuthToken;
import model.UserData;
import model.requests.AuthRequest;
import model.requests.LoginRequest;
import model.requests.RegisterRequest;

import java.util.UUID;

public class UserService {
    DataAccess myDataAccess = new MemoryDataAccess();

    public AuthToken register(RegisterRequest registerRequest) throws UsernameAlreadyTakenException {
        UserData currentUser = myDataAccess.getUser(registerRequest.username());
        if (currentUser != null) {
            throw new UsernameAlreadyTakenException(String.format("The Username: %s has already been taken", currentUser.username()));
        }
        currentUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        myDataAccess.createUser(currentUser);
        AuthToken myAuthData = new AuthToken(currentUser.username(), generateToken());
        myDataAccess.createAuth(myAuthData);
        return myAuthData;
    }

    public AuthToken login(LoginRequest loginRequest) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("login has not been implemented");
    }

    public void logout(AuthRequest logoutRequest) throws ExecutionControl.NotImplementedException {
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
