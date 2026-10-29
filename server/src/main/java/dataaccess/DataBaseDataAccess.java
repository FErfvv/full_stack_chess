package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.ColorTakenException;
import dataaccess.exceptions.NoGameException;
import kotlin.NotImplementedError;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.requests.JoinGameRequest;
import model.responses.GameListResponseData;

import java.util.List;

public class DataBaseDataAccess implements DataAccess {

    public void clear() {
        throw new NotImplementedError();
    }

    @Override
    public void createUser(UserData currentUser) {

    }

    @Override
    public UserData getUser(String username) {
        throw new NotImplementedError();
    }

    @Override
    public GameData createGame(String gameName) {
        throw new NotImplementedError();
    }

    @Override
    public GameData getGame(String gameID) {
        throw new NotImplementedError();
    }

    @Override
    public List<GameListResponseData> listGames() {
        throw new NotImplementedError();
    }

    @Override
    public void updateGame(JoinGameRequest joinGameRequest, UserData user) throws NoGameException, ColorTakenException, BadRequestException {
        throw new NotImplementedError();
    }

    @Override
    public void createAuth(AuthData authToken) {
        throw new NotImplementedError();
    }

    @Override
    public String getAuth(String authToken) {
        throw new NotImplementedError();
    }

    @Override
    public boolean deleteAuth(AuthData authData) {
        throw new NotImplementedError();
    }
}
