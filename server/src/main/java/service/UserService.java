package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import jdk.jshell.spi.ExecutionControl;
import model.AuthToken;
import model.requests.AuthRequest;
import model.requests.LoginRequest;
import model.requests.RegisterRequest;

public class UserService {
    DataAccess myDataAccess = new MemoryDataAccess();

    public AuthToken register(RegisterRequest registerRequest) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("register has not been implemented");
    }

    public AuthToken login(LoginRequest loginRequest) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("login has not been implemented");
    }

    public void logout(AuthRequest logoutRequest) throws ExecutionControl.NotImplementedException {
    }
}
