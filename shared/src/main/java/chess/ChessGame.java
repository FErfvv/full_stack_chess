package chess;

import chess.PieceProfiles.PawnMoveProfile;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    TeamColor teamTurn;

    CheckInfoTracker tracker;


    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) {
            return null;
        } else {
            Collection<ChessMove> moves = board.getPiece(startPosition).pieceMoves(board,startPosition);
            Collection<ChessMove> validMoves = new ArrayList<>();
            ChessPiece originalPiece = board.getPiece(startPosition);
            for (ChessMove move: moves) {
                if (canMakeMove(move)) {
                    validMoves.add(move);
                }
            }
            return validMoves;
        }
    }



    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (canMakeMove(move)) {
            board.movePiece(move);
            teamTurn = (teamTurn == TeamColor.BLACK) ? TeamColor.WHITE : TeamColor.BLACK;
        } else {
            throw new InvalidMoveException();
        }
    }

    public boolean canMakeMove(ChessMove move) {
        ChessPiece pieceToMove = board.getPiece(move.getStartPosition());
        if (pieceToMove == null || teamTurn != pieceToMove.getTeamColor()) {
            return false;
        }
        System.out.println("currently moving: " + pieceToMove.getPieceType());
        System.out.println(board);
        Collection<ChessMove> possibleMoves = pieceToMove.pieceMoves(board,move.getStartPosition());
        if (possibleMoves.contains(move)) {
            ChessPiece targetPiece = board.getPiece(move.getEndPosition());
            board.movePiece(move);
            if (isInCheck(teamTurn) || isInCheckmate(teamTurn)) {
                board.movePiece(new ChessMove(move.getEndPosition(),move.getStartPosition(),pieceToMove.getPieceType()));
                board.addPiece(move.getEndPosition(),targetPiece);
                return false;
            } else {
                board.movePiece(new ChessMove(move.getEndPosition(),move.getStartPosition(),pieceToMove.getPieceType()));
                board.addPiece(move.getEndPosition(),targetPiece);
                return true;
            }
        } else {
            return false;
        }
    }

    public void undoMove(ChessMove move, ChessPiece startingPiece, ChessPiece endingPiece) {
        board.movePiece(new ChessMove(move.getEndPosition(),move.getStartPosition(), startingPiece.getPieceType()));
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        tracker = setupCheckmateInfoTracker(teamColor);
        return tracker.isKingIsAttacked() && tracker.getAttackedPositions().size() < tracker.getKingFuturePos().size() + 1;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        tracker = setupCheckmateInfoTracker(teamColor);
        /**
         * If the king is being attacked, and it can't move to safe space, and it's being attacked
         * by multiple pieces, it's an automatic checkmate
         */
        if (tracker.isKingIsAttacked()
                && tracker.getAttackedPositions().size() == tracker.getKingFuturePos().size() + 1
                && tracker.getPosOfPiecesAttackingKing().size() > 1) {
            return true;
        } else if (tracker.getPosOfPiecesAttackingKing().size() == 1) {
            // If the piece that is attacking can be attacked, return false, else return true.
            return (!canBeAttacked(teamColor,tracker.getPosOfPiecesAttackingKing().getFirst()));
        }

        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        tracker = setupCheckmateInfoTracker(teamColor);
        return !tracker.isKingIsAttacked() && tracker.getAttackedPositions().size() == tracker.getKingFuturePos().size() && !tracker.getAttackedPositions().isEmpty();
    }

    public CheckInfoTracker setupCheckmateInfoTracker(TeamColor teamColor) {
        ChessPosition kingPos = findKingPosition(teamColor);
        CheckInfoTracker infoTracker = new CheckInfoTracker();
        infoTracker.setKingFuturePos(getAllEndPositions(kingPos, infoTracker));
        return getAttackingPieces(teamColor,infoTracker, kingPos);
    }

    private CheckInfoTracker getAttackingPieces(TeamColor teamColor, CheckInfoTracker infoTracker, ChessPosition kingPos) {

        for (int row = 1; row <= ChessBoard.BOARD_HEIGHT; row++) {
            for (int col = 1; col <= ChessBoard.BOARD_WIDTH; col++) {
                // Iterates through the board and finds pieces from the opposing team
                ChessPosition currentPos = new ChessPosition(row, col);
                ChessPiece currentPiece = board.getPiece(currentPos);
                if (currentPiece == null || currentPiece.getTeamColor() == teamColor ) {
                    continue;
                }
                // Gets all the possible movements from that piece
                Collection<ChessPosition> attackingEndPositions = getAllEndPositions(currentPos, infoTracker);
                for (ChessPosition attackedPosition : attackingEndPositions) {
                    // Checks to see if the spaces around the king are being attacked
                    if (infoTracker.getKingFuturePos().contains(attackedPosition)) {
                        // Logic to prevent the positions and pieces from being added twice
                        if (!infoTracker.getAttackingPieces().contains(currentPos)) {
                            infoTracker.addAttackingPiece(currentPos);
                        }
                        if (!infoTracker.getAttackedPositions().contains(attackedPosition)) {
                            infoTracker.addAttackedPos(attackedPosition);
                        }
                    // checks to see if the king is being attacked.
                    } else if (attackedPosition.equals(kingPos)) {
                        if (!infoTracker.getAttackedPositions().contains(attackedPosition)) {
                            infoTracker.addAttackedPos(attackedPosition);
                        }
                        infoTracker.setKingIsAttacked(true);
                        // if there is a piece that is attacking the king, it saves that position to a list
                        infoTracker.addToListOfPosAttackingKing(currentPos);
                        if (!infoTracker.getAttackingPieces().contains(currentPos)) {
                            infoTracker.addAttackingPiece(currentPos);
                        }
                    }
                }
            }
        }

        return infoTracker;
    }

    private boolean canBeAttacked(TeamColor teamColor, ChessPosition piecePos) {
        for (int row = 1; row <= ChessBoard.BOARD_HEIGHT; row++) {
            for (int col = 1; col <= ChessBoard.BOARD_WIDTH; col++) {
                // Iterates through the board and finds pieces from the opposing team
                ChessPosition currentPos = new ChessPosition(row, col);
                ChessPiece currentPiece = board.getPiece(currentPos);
                // TODO: need to create more tests to see if there are situations where king does need to be checked
                if (currentPiece == null || currentPiece.getTeamColor() != teamColor || currentPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    continue;
                }
                // Gets all the possible movements from that piece
                Collection<ChessMove> moves = board.getPiece(piecePos).pieceMoves(board,currentPos);
                for (ChessMove move: moves) {
                    if (move.getEndPosition().equals(piecePos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition findKingPosition(TeamColor teamColor) {
        ChessPosition kingPos = null;
        for (int row = 1; row <= ChessBoard.BOARD_HEIGHT; row++) {
            for (int col = 1; col <= ChessBoard.BOARD_WIDTH; col++) {
                ChessPiece currentPiece = board.getPiece(new ChessPosition(row,col));
                if (currentPiece == null) {
                    continue;
                }
                if (currentPiece.getPieceType() == ChessPiece.PieceType.KING
                        && currentPiece.getTeamColor() == teamColor) {
                    kingPos = new ChessPosition(row,col);

                }
            }
        }
        if (kingPos == null || board.getPiece(kingPos) == null) {
            throw new NullPointerException("No King was found on the board");
        }
        return kingPos;
    }

    private Collection<ChessPosition> getAllEndPositions(ChessPosition piecePosition, CheckInfoTracker infoTracker) {

        Collection<ChessMove> moves = board.getPiece(piecePosition).checkIfAttackingKing(board,piecePosition,infoTracker);

        Collection<ChessPosition> possiblePositions = new ArrayList<>();
        for (ChessMove move: moves) {
            possiblePositions.add(move.getEndPosition());
        }
        return possiblePositions;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
