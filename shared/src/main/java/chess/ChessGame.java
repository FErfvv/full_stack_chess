package chess;

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
    public ChessGame() {
        board = new ChessBoard();
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
            return board.getPiece(startPosition).pieceMoves(board,startPosition);
        }
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceToMove = board.getPiece(move.getStartPosition());
        if (pieceToMove == null || pieceToMove.getTeamColor() != teamTurn) {
            throw new InvalidMoveException();
        }
        Collection<ChessMove> possibleMoves = pieceToMove.pieceMoves(board,move.getStartPosition());
        if (possibleMoves.contains(move)) {
            board.movePiece(move);
            teamTurn = (teamTurn == TeamColor.BLACK) ? TeamColor.WHITE : TeamColor.BLACK;
        } else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        CheckInfoTracker tracker = setupCheckmateInfoTracker(teamColor);
        for (ChessPosition pos : tracker.getAttackedPositions() ) {
            System.out.println("Attacked Here: " + pos);
        }
        for (ChessPosition pos : tracker.getAttackingPieces() ) {
            System.out.println("Attacked By: " + pos);
        }

        return false;
    }

    public CheckInfoTracker setupCheckmateInfoTracker(TeamColor teamColor) {
        ChessPosition kingPos = findKingPosition(teamColor);
        CheckInfoTracker infoTracker = new CheckInfoTracker();
        infoTracker.setKingFuturePos(getAllEndPositions(kingPos));
        return getAttackingPieces(teamColor,infoTracker, kingPos);
    }

    private CheckInfoTracker getAttackingPieces(TeamColor teamColor, CheckInfoTracker infoTracker, ChessPosition kingPos) {

        for (int row = 1; row <= ChessBoard.BOARD_HEIGHT; row++) {
            for (int col = 1; col <= ChessBoard.BOARD_WIDTH; col++) {
                ChessPosition currentPos = new ChessPosition(row, col);
                ChessPiece currentPiece = board.getPiece(currentPos);
                if (currentPiece == null || currentPiece.getTeamColor() == teamColor ) {
                    continue;
                }

                Collection<ChessPosition> attackingEndPositions = getAllEndPositions(currentPos);
                for (ChessPosition position : attackingEndPositions) {
                    if (infoTracker.getKingFuturePos().contains(position)) {
                        if (!infoTracker.getAttackingPieces().contains(currentPos)) {
                            infoTracker.addAttackingPiece(currentPos);
                        }
                        infoTracker.addAttackedPos(position);
                    } else if (position.equals(kingPos)) {
                        infoTracker.addAttackedPos(kingPos);
                        infoTracker.setKingIsAttacked(true);
                        if (!infoTracker.getAttackingPieces().contains(currentPos)) {
                            infoTracker.addAttackingPiece(currentPos);
                        }
                    }
                }
            }
        }

        return infoTracker;
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

    private Collection<ChessPosition> getAllEndPositions(ChessPosition kingPos) {
        Collection<ChessMove> kingMoves = board.getPiece(kingPos).pieceMoves(board,kingPos);
        Collection<ChessPosition> kingPossiblePositions = new ArrayList<>();
        for (ChessMove move: kingMoves) {
            kingPossiblePositions.add(move.getEndPosition());
        }
        return kingPossiblePositions;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
