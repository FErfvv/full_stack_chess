package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import model.responses.GameListResponseData;
import model.responses.ListGamesResponse;

import static chess.ChessPiece.PieceType.*;

public class ClientGuiMap {


    public static String LoggedOutHelp() {
        return String.format("""
                        %sregister <USERNAME> <PASSWORD> <EMAIL>%s - to create an account
                        %slogin <USERNAME> <PASSWORD>%s - to play chess
                        %squit%s - playing chess
                        %shelp%s - with possible commands%s
                        """,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.RESET_TEXT_COLOR);
    }

    public static String LoggedInHelp() {
        return String.format("""
                        %screate <NAME>%s - a game
                        %slist%s - games
                        %sjoin <ID> [WHITE|BLACK]%s - a game
                        %sobserve <ID>%s - a game
                        %slogout%s - when you are done
                        %squit%s - playing chess
                        %shelp%s - with possible commands%s
                        """,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.RESET_TEXT_COLOR);
    }

    public static String LoggedOutMessage() {
        return String.format("%sThank you for playing! Come again soon.%s\n", EscapeSequences.SET_TEXT_COLOR_GREEN, EscapeSequences.RESET_TEXT_COLOR);
    }

    public static String LoggedInMessage(String name) {
        return String.format("%sSuccessfully logged in! Welcome, %s%s\n", EscapeSequences.SET_TEXT_COLOR_GREEN, name, EscapeSequences.RESET_TEXT_COLOR);
    }

    public static String RegisteredMessage(String name) {
        return String.format("%sSuccessfully registered! Welcome, %s%s\n", EscapeSequences.SET_TEXT_COLOR_GREEN, name, EscapeSequences.RESET_TEXT_COLOR);
    }

    public static String GameList(ListGamesResponse list) {
        StringBuilder myString = new StringBuilder();
        for (int i = 1; i < list.getGames().size(); i++) {
            GameListResponseData game = list.getGames().get(i);
            String gameString = String.format("""
                    Game %s: %s
                      - White: %s
                      - Black: %s
                    """, i, game.gameName(), game.whiteUsername(), game.blackUsername());
            myString.append(gameString);
        }
        return myString.toString();
    }

    public static String renderGame(ChessGame game, ChessGame.TeamColor team) {
        ChessPiece[][] board = game.getBoard().getMyBoard();
        String[][][] boardRenderInfo = new String[10][10][3];
        StringBuilder renderedBoard = new StringBuilder();
        for (int rowCoor = 0; rowCoor < boardRenderInfo.length; rowCoor++) {
            for (int colCoor = 0; colCoor < boardRenderInfo[rowCoor].length; colCoor++) {
                if (rowCoor == 0 || rowCoor == 9) {
                    boardRenderInfo[rowCoor][colCoor][0] = getCharFromNumber(colCoor);
                    boardRenderInfo[rowCoor][colCoor][1] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
                    boardRenderInfo[rowCoor][colCoor][2] = EscapeSequences.SET_TEXT_COLOR_BLACK;
                } else if (colCoor == 0 || colCoor == 9) {
                    boardRenderInfo[rowCoor][colCoor][0] = getNumber(colCoor);
                    boardRenderInfo[rowCoor][colCoor][1] = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
                    boardRenderInfo[rowCoor][colCoor][2] = EscapeSequences.SET_TEXT_COLOR_BLACK;
                } else {
                    addPiecesToRender(boardRenderInfo, board, rowCoor, colCoor);
                    boardRenderInfo[rowCoor][colCoor][1] = (rowCoor + colCoor) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_BLACK;
                }
                renderedBoard.append(renderCell(boardRenderInfo[rowCoor][colCoor]));
            }
            renderedBoard.append("\n");
        }
        return renderedBoard.toString();
    }

    private static String renderCell(String[] info) {
        return info[1] + info[2] + " " + info[0] + " " + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR;
    }

    private static void addPiecesToRender(String[][][] boardRenderInfo, ChessPiece[][] myBoard, int row, int col) {
        if (myBoard[row - 1][col - 1] == null) {
            boardRenderInfo[row][col][0] = "";
        } else {
            switch (myBoard[row - 1][col - 1].getPieceType()) {
                case PAWN:
                    boardRenderInfo[row][col][0] = "P";
                    break;
                case ROOK:
                    boardRenderInfo[row][col][0] = "R";
                    break;
                case KNIGHT:
                    boardRenderInfo[row][col][0] = "N";
                    break;
                case BISHOP:
                    boardRenderInfo[row][col][0] = "B";
                    break;
                case KING:
                    boardRenderInfo[row][col][0] = "K";
                    break;
                case QUEEN:
                    boardRenderInfo[row][col][0] = "Q";
                    break;
            }
            boardRenderInfo[row][col][2] =
                    myBoard[row - 1][col - 1].getTeamColor() == ChessGame.TeamColor.WHITE ?
                            EscapeSequences.SET_TEXT_COLOR_BLUE :
                            EscapeSequences.SET_TEXT_COLOR_RED;
        }
    }

    private static String getCharFromNumber(int i) {
        return i > 0 && i < 9 ? String.valueOf((char) (i + 64)) : null;
    }

    private static String getNumber(int i) {
        return i > 0 && i < 9 ? Integer.toString(i) : null;
    }

}
