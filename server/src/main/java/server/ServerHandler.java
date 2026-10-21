package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.UsernameAlreadyTakenException;
import model.AuthToken;
import model.UserData;
import model.requests.RegisterRequest;
import model.requests.RequestInterface;
import service.UserService;

public class ServerHandler {
    private static final DataAccess myDataAccess = new MemoryDataAccess();
    private static final UserService userService = new UserService(myDataAccess);
    private static final Gson gson = new Gson();

    public static AuthToken register(String registerData) throws UsernameAlreadyTakenException, BadRequestException {
        AuthToken registerResponse = null;

        registerResponse = userService.register((RegisterRequest) checkRequest(RegisterRequest.class, registerData));
        return registerResponse;
    }

    public static void clearDb() {

    }

    public static RequestInterface checkRequest(Class<? extends RequestInterface> requestType, String registerData) throws BadRequestException {
        RequestInterface myData = gson.fromJson(registerData, requestType);
        if (!myData.isValidRequest()) {
            throw new BadRequestException(String.format("%s is not a valid request", registerData));
        }
        return myData;
    }
}
