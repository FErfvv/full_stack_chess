package server;

import com.google.gson.Gson;
import dataaccess.exceptions.UsernameAlreadyTakenException;
import model.AuthToken;
import model.UserData;
import model.requests.RegisterRequest;
import service.UserService;

public class ServerHandler {

    private static UserService userService = new UserService();

    public static AuthToken register(String registerData) throws UsernameAlreadyTakenException {
        AuthToken registerResponse = null;
        Gson gson = new Gson();
        RegisterRequest myData = gson.fromJson(registerData, RegisterRequest.class);
        registerResponse = userService.register(myData);
        return registerResponse;
    }
}
