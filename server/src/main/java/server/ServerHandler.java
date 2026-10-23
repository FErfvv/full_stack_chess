package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import dataaccess.exceptions.*;
import model.AuthData;
import model.requests.*;
import model.responses.GameListResponseData;
import service.AdminService;
import service.UserService;

import java.util.List;

public class ServerHandler {
    private static final DataAccess MY_DATA_ACCESS = new MemoryDataAccess();
    private static final UserService USER_SERVICE = new UserService(MY_DATA_ACCESS);
    private static final AdminService ADMIN_SERVICE = new AdminService(MY_DATA_ACCESS);
    private static final Gson GSON = new Gson();

    public static AuthData register(String registerData) throws UsernameAlreadyTakenException, BadRequestException {
        return USER_SERVICE.register((RegisterRequest) checkRequest(RegisterRequest.class, registerData));
    }

    public static AuthData login(String loginData) throws BadRequestException, UnauthorizedException {
        return USER_SERVICE.login((LoginRequest) checkRequest(LoginRequest.class, loginData));
    }

    public static void clearDb() {
        ADMIN_SERVICE.clearDb();
    }

    public static void logout(String authToken) throws BadRequestException, UnauthorizedException, DataAccessException {
        if (authToken.isEmpty()) {
            throw new BadRequestException();
        }
        USER_SERVICE.logout(new AuthRequest(authToken));
    }

    public static int createGame(String authToken, String gameName) throws BadRequestException, UnauthorizedException {
        if (authToken.isEmpty()) {
            throw new BadRequestException();
        }
        return USER_SERVICE.createGame(new AuthRequest(authToken), (NewGameRequest) checkRequest(NewGameRequest.class, gameName));
    }

    public static List<GameListResponseData> listGames(String authToken) throws BadRequestException, UnauthorizedException {
        if (authToken.isEmpty()) {
            throw new BadRequestException();
        }
        return USER_SERVICE.listGames(new AuthRequest(authToken));
    }

    public static void addUserToGame(String authToken, String joinGameData)
            throws BadRequestException,
            ColorTakenException,
            UnauthorizedException,
            NoGameException {
        if (authToken.isEmpty()) {
            throw new BadRequestException();
        }
        USER_SERVICE.addUserToGame(new AuthRequest(authToken), (JoinGameRequest) checkRequest(JoinGameRequest.class, joinGameData));
    }

    public static RequestInterface checkRequest(Class<? extends RequestInterface> requestType, String requestData) throws BadRequestException {
        if (requestData.isEmpty()) {
            throw new BadRequestException();
        }
        RequestInterface myData = GSON.fromJson(requestData, requestType);
        if (!myData.isValidRequest()) {
            throw new BadRequestException();
        }
        return myData;
    }


}
