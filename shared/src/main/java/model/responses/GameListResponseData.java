package model.responses;

import chess.ChessGame;

public record GameListResponseData(int gameID, String whiteUsername, String blackUsername, String gameName) {
}
