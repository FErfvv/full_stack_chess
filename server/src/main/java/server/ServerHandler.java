package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.UsernameAlreadyTakenException;
import model.AuthToken;
import model.UserData;
import model.requests.RegisterRequest;
import service.UserService;

public class ServerHandler {
    private static final DataAccess myDataAccess = new MemoryDataAccess();
    private static final UserService userService = new UserService(myDataAccess);

    public static AuthToken register(String registerData) throws UsernameAlreadyTakenException, BadRequestException {
        AuthToken registerResponse = null;
        Gson gson = new Gson();
        RegisterRequest myData = gson.fromJson(registerData, RegisterRequest.class);
        if (myData.password() == null || myData.username() == null || myData.email() == null) {
            throw new BadRequestException(String.format("%s is not a valid request", registerData));
        }
        registerResponse = userService.register(myData);
        return registerResponse;
    }
}
