package chess.PieceProfiles;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Arrays;
import java.util.List;

public class ProfileTemplate {
    public static boolean IS_CONTINUOUS;
    public static int[][] MOVEMENT_PROFILE;

    public ProfileTemplate(boolean cont, int[][] prof) {
        IS_CONTINUOUS = cont;
        MOVEMENT_PROFILE = prof;
    }

    public void checkMoves(ChessBoard board, ChessPosition myPosition, List<ChessMove> myMoves) {
        for (int[] direction: MOVEMENT_PROFILE) {
            move(direction,board, myPosition,myMoves);
        }
    }

    private void move(int[] direction, ChessBoard board, ChessPosition myPosition,List<ChessMove> myMoves) {
        int deltaCol = direction[0];
        int deltaRow = direction[1];
        int col = myPosition.getColumn() + deltaCol;
        int row = myPosition.getRow() + deltaRow;

        while (isOnBoard(row,col)
                && (board.getMyBoard()[row-1][col-1] == null
                || board.getMyBoard()[row-1][col-1].getTeamColor() != board.getPiece(myPosition).getTeamColor())) {

            myMoves.add(new ChessMove(myPosition,new ChessPosition(row,col), null));
            if (IS_CONTINUOUS){
                break;
            }
            if (board.getMyBoard()[row-1][col-1] != null) {
                break;
            }
            col += deltaCol;
            row += deltaRow;
        }
    }

    public boolean isOnBoard(int row, int col) {
        return (row > 0 && row < ChessBoard.BOARD_HEIGHT + 1 && col > 0 && col < ChessBoard.BOARD_WIDTH + 1);
    }
}
