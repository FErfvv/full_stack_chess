package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.exceptions.UsernameAlreadyTakenException;
import model.AuthData;
import model.requests.LoginRequest;
import model.requests.RegisterRequest;
import model.requests.RequestInterface;
import service.AdminService;
import service.UserService;

public class ServerHandler {
    private static final DataAccess myDataAccess = new MemoryDataAccess();
    private static final UserService userService = new UserService(myDataAccess);
    private static final AdminService adminService = new AdminService(myDataAccess);
    private static final Gson gson = new Gson();

    public static AuthData register(String registerData) throws UsernameAlreadyTakenException, BadRequestException {
        return userService.register((RegisterRequest) checkRequest(RegisterRequest.class, registerData));
    }

    public static AuthData login(String loginData) throws BadRequestException, UnauthorizedException {
        return userService.login((LoginRequest) checkRequest(LoginRequest.class, loginData));
    }

    public static void clearDb() {
        adminService.clearDb();
    }

    public static RequestInterface checkRequest(Class<? extends RequestInterface> requestType, String registerData) throws BadRequestException {
        RequestInterface myData = gson.fromJson(registerData, requestType);
        if (!myData.isValidRequest()) {
            throw new BadRequestException(String.format("%s is not a valid request", registerData));
        }
        return myData;
    }
}
