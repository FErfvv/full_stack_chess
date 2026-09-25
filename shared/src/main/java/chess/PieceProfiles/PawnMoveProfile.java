package chess.PieceProfiles;

import chess.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PawnMoveProfile extends PieceProfileTemplate {
    public PawnMoveProfile() {
        super(false, new int [][] {{-1,1}, {1,1}, {0, 1}, {0, 2}});
    }

    @Override
    public void checkMoves(ChessBoard board, ChessPosition myPosition, List<ChessMove> myMoves) {
        String[] allMoves = {"left", "right", "forward1","forward2"};
        int direction = 1;
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
            direction = -1;
        }
        Map<String, int[]> pawnProfile = Map.ofEntries(
                Map.entry("left",super.MOVEMENT_PROFILE[0]),
                Map.entry("right",super.MOVEMENT_PROFILE[1]),
                Map.entry("forward1",super.MOVEMENT_PROFILE[2]),
                Map.entry("forward2",super.MOVEMENT_PROFILE[3])
        );
        List<String> movesOnMap = new ArrayList<>();
        for (String move: allMoves) {
            int col = myPosition.getColumn() + (pawnProfile.get(move)[0] * direction);
            int row = myPosition.getRow() + (pawnProfile.get(move)[1] * direction);
            if (isOnBoard(row, col)) {
                movesOnMap.add(move);
            }
        }
        boolean canJump = false;
        for (String move: movesOnMap) {
            int col = myPosition.getColumn() + (pawnProfile.get(move)[0] * direction);
            int row = myPosition.getRow() + (pawnProfile.get(move)[1] * direction);
            ChessPosition targetPos = new ChessPosition(row, col);
            switch (move) {
                case "left", "right":
                    if (board.getPiece(targetPos) != null && board.getPiece(targetPos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        if (targetPos.getRow() == (direction == 1 ? 8 : 1)) {
                            addPromotionPieces(myPosition, targetPos, myMoves);
                        } else {
                            myMoves.add(new ChessMove(myPosition, targetPos, null));
                        }
                    }
                    break;
                case "forward1":
                    if (board.getPiece(targetPos) == null) {
                        if (targetPos.getRow() == (direction == 1 ? 8 : 1)) {
                            addPromotionPieces(myPosition, targetPos, myMoves);
                        } else {
                            myMoves.add(new ChessMove(myPosition, targetPos, null));
                        }
                        if (myPosition.getRow() == (direction == 1 ? 2 : 7)) {
                            canJump = true;
                        }

                    }
                    break;
                case "forward2":
                    if (board.getPiece(targetPos) == null && canJump) {
                        myMoves.add(new ChessMove(myPosition, targetPos, null));
                    }
                    break;
            }
        }
    }

    public void addPromotionPieces(ChessPosition myPosition, ChessPosition targetPosition, List<ChessMove> myMoves) {
        for (ChessPiece.PieceType type: ChessPiece.PieceType.values()) {
            if (type != ChessPiece.PieceType.PAWN && type != ChessPiece.PieceType.KING) {
                myMoves.add(new ChessMove(myPosition,targetPosition,type));
            }
        }
    }
}
