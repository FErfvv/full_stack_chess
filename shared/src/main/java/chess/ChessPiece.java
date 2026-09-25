package chess;

import chess.PieceProfiles.*;

import java.util.*;

import static java.util.Map.entry;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    // Describes in which directions that peices can move
    // Index 0 describes whether it moves one space (0) or multiple spaces (1)
    private static final Map<Enum, int[][]> PROFILES = Map.ofEntries(
            entry(PieceType.ROOK,new int[][] {{1}, {0, 1}, {1, 0}, {0, -1}, {-1, 0}}),
            entry(PieceType.KING, new int [][] {{0},{0, 1}, {1, 1}, {1, 0}, {1,-1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}}),
            entry(PieceType.QUEEN, new int [][] {{1},{0, 1}, {1, 1}, {1, 0}, {1,-1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}}),
            entry(PieceType.BISHOP, new int [][] {{1}, {1, 1}, {1,-1}, {-1, -1}, {-1, 1}}),
            entry(PieceType.KNIGHT, new int [][] {{0}, {-1,2},{1,2},{2,1},{2,-1},{1,-2},{-1,-2},{-2,1},{-2,-1}})
    );

    private static final Map<Enum, ProfileTemplate> PIECE_PROFILES = Map.ofEntries(
            entry(PieceType.ROOK,new RookMoveProfile()),
            entry(PieceType.KING, new KingMoveProfile()),
            entry(PieceType.QUEEN, new QueenMoveProfile()),
            entry(PieceType.BISHOP, new BishopMoveProfile()),
            entry(PieceType.KNIGHT, new KnightMoveProfile()),
            entry(PieceType.PAWN, new PawnMoveProfile())
    );

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece myPiece = board.getPiece(myPosition);
        List<ChessMove> myMoves = new ArrayList<>();
        PIECE_PROFILES.get(myPiece.getPieceType()).checkMoves(board,myPosition,myMoves);
        return myMoves;
    }
}
