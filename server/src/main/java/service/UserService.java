package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import exceptions.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.requests.*;
import model.responses.GameListResponseData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.UUID;

public class UserService {
    private final DataAccess myDataAccess;

    public UserService(DataAccess myDataAccess) {
        this.myDataAccess = myDataAccess;
    }

    public AuthData register(RegisterRequest registerRequest) throws UsernameAlreadyTakenException, DataAccessException {
        UserData currentUser = myDataAccess.getUser(registerRequest.username());
        if (currentUser != null) {
            throw new UsernameAlreadyTakenException(currentUser.username());
        }
        String hashedPassword = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
        currentUser = new UserData(registerRequest.username(), hashedPassword, registerRequest.email());
        myDataAccess.createUser(currentUser);
        AuthData myAuthData = new AuthData(currentUser.username(), generateToken());
        myDataAccess.createAuth(myAuthData);
        return myAuthData;
    }

    public AuthData login(LoginRequest loginRequest) throws UnauthorizedException, DataAccessException {
        UserData currentUser = myDataAccess.getUser(loginRequest.username());
        if (currentUser == null) {
            throw new UnauthorizedException(String.format("Username '%s' does not exist", loginRequest.username()));
        }
        if (!BCrypt.checkpw(loginRequest.password(), currentUser.password())) {
            throw new UnauthorizedException(String.format("Password '%s' is incorrect", loginRequest.username()));
        }
        AuthData myAuthData = new AuthData(currentUser.username(), generateToken());
        myDataAccess.createAuth(myAuthData);
        return myAuthData;
    }

    public void logout(AuthRequest logoutRequest) throws UnauthorizedException, DataAccessException {
        String username = myDataAccess.getAuth(logoutRequest.authToken());
        if (username == null) {
            throw new UnauthorizedException("unauthorized");
        }
        if (!myDataAccess.deleteAuth(new AuthData(username, logoutRequest.authToken()))) {
            throw new DataAccessException("problem while deleting auth token");
        }
    }

    public int createGame(AuthRequest authRequest, NewGameRequest gameRequest) throws UnauthorizedException, DataAccessException {
        if (myDataAccess.getAuth(authRequest.authToken()) == null) {
            throw new UnauthorizedException("unauthorized");
        }
        GameData newGame = myDataAccess.createGame(gameRequest.gameName());
        return newGame.gameID();
    }

    public void addUserToGame(AuthRequest authRequest, JoinGameRequest joinGameRequest)
            throws UnauthorizedException,
            ColorTakenException,
            NoGameException,
            BadRequestException, DataAccessException {
        String username = myDataAccess.getAuth(authRequest.authToken());
        if (username == null) {
            throw new UnauthorizedException("unauthorized");
        }
        UserData user = myDataAccess.getUser(username);
        GameData targetGame = myDataAccess.getGame(joinGameRequest.gameID());
        if (targetGame == null) {
            throw new NoGameException();
        }
        boolean isBlack = joinGameRequest.playerColor().equals(ChessGame.TeamColor.BLACK.name());
        boolean isWhite = joinGameRequest.playerColor().equals(ChessGame.TeamColor.WHITE.name());
        if (!isBlack && !isWhite) {
            throw new BadRequestException();
        }
        if (isBlack && targetGame.blackUsername() == null) {
            targetGame.setBlackUsername(user.username());
        } else if (isWhite && targetGame.whiteUsername() == null) {
            targetGame.setWhiteUsername(user.username());
        }
        myDataAccess.updateGame(targetGame);
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public List<GameListResponseData> listGames(AuthRequest authRequest) throws UnauthorizedException, DataAccessException {
        if (myDataAccess.getAuth(authRequest.authToken()) == null) {
            throw new UnauthorizedException("unauthorized");
        }
        return myDataAccess.listGames();
    }
}
