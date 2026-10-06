package chess.pieceprofiles;

import chess.CheckInfoTracker;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.List;

import static chess.ChessBoard.isOnBoard;

public class PieceProfileTemplate {
    public boolean isContinuous;
    public int[][] movementProfile;

    public PieceProfileTemplate(boolean cont, int[][] prof) {
        isContinuous = cont;
        movementProfile = prof;
    }

    public void checkMoves(ChessBoard board, ChessPosition myPosition, List<ChessMove> myMoves, CheckInfoTracker checkTracker) {
        for (int[] direction: movementProfile) {
            move(direction,board, myPosition,myMoves, checkTracker);
        }
    }

    private void move(int[] direction, ChessBoard board, ChessPosition myPosition,List<ChessMove> myMoves, CheckInfoTracker checkTracker) {
        int deltaCol = direction[0];
        int deltaRow = direction[1];

        int col = myPosition.getColumn() + deltaCol;
        int row = myPosition.getRow() + deltaRow;
        ChessPosition targetPos;
        while (isOnBoard(row, col)) {
            targetPos = new ChessPosition(row, col);
            if (board.getPiece(targetPos) == null) {
                myMoves.add(new ChessMove(myPosition,targetPos,null));
            } else if (board.getPiece(targetPos).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                myMoves.add(new ChessMove(myPosition,targetPos,null));
                break;
            } else if (checkTracker != null && checkTracker.getKingFuturePos().contains(targetPos)) {
                myMoves.add(new ChessMove(myPosition,targetPos,null));
                break;
            } else {
                break;
            }

            if (!isContinuous) {
                break;
            }
            col += deltaCol;
            row += deltaRow;
        }
    }
}
