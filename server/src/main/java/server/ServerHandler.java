package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.exceptions.UsernameAlreadyTakenException;
import model.AuthData;
import model.requests.AuthRequest;
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

    public static void logout(String authToken) throws BadRequestException, UnauthorizedException, DataAccessException {
        if (authToken.isEmpty()) {
            throw new BadRequestException("Error: empty request");
        }
        userService.logout(new AuthRequest(authToken));
    }

    public static RequestInterface checkRequest(Class<? extends RequestInterface> requestType, String requestData) throws BadRequestException {
        if (requestData.isEmpty()) {
            throw new BadRequestException("Error: empty request");
        }
        RequestInterface myData = gson.fromJson(requestData, requestType);
        if (!myData.isValidRequest()) {
            throw new BadRequestException(String.format("%s is not a valid request", requestData));
        }
        return myData;
    }
}
