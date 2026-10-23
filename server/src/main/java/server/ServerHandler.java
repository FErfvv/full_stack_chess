package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.exceptions.*;
import model.AuthData;
import model.GameData;
import model.requests.*;
import model.responses.GameListResponseData;
import service.AdminService;
import service.UserService;

import java.util.List;

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
            throw new BadRequestException();
        }
        userService.logout(new AuthRequest(authToken));
    }

    public static int createGame(String authToken, String gameName) throws BadRequestException, UnauthorizedException {
        if (authToken.isEmpty()) {
            throw new BadRequestException();
        }
        return userService.createGame(new AuthRequest(authToken), (NewGameRequest) checkRequest(NewGameRequest.class, gameName));
    }

    public static List<GameListResponseData> listGames(String authToken) throws BadRequestException, UnauthorizedException {
        if (authToken.isEmpty()) {
            throw new BadRequestException();
        }
        return userService.listGames(new AuthRequest(authToken));
    }

    public static void addUserToGame(String authToken, String joinGameData) throws BadRequestException, ColorTakenException, UnauthorizedException, NoGameException {
        if (authToken.isEmpty()) {
            throw new BadRequestException();
        }
        userService.addUserToGame(new AuthRequest(authToken), (JoinGameRequest) checkRequest(JoinGameRequest.class, joinGameData));
    }

    public static RequestInterface checkRequest(Class<? extends RequestInterface> requestType, String requestData) throws BadRequestException {
        if (requestData.isEmpty()) {
            throw new BadRequestException();
        }
        RequestInterface myData = gson.fromJson(requestData, requestType);
        if (!myData.isValidRequest()) {
            throw new BadRequestException();
        }
        return myData;
    }


}
