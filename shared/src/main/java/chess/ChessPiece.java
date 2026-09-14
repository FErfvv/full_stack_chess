package chess;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

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
    private static final Map<String,int[]> deltaMoves = Map.ofEntries(
            entry("N", new int[] {0, 1}),
            entry("NE", new int[] {1, 1}),
            entry("E", new int[] {1, 0}),
            entry("SE", new int[] {1, -1}),
            entry("S", new int[] {0, -1}),
            entry("SW", new int[] {-1, -1}),
            entry("W", new int[] {-1, 0}),
            entry("NW", new int[] {-1, 1})
    );
    // Describes in which directions that peices can move
    // Index 0 describes whether it moves one space (0) or multiple spaces (1)
    private static final String[] rookProfile = {"1", "N", "E", "S", "W"};
    private static final String[] kingProfile = {"0","N", "NE", "E", "SE", "S", "SW", "W", "NW"};
    private static final String[] queenPofile = {"1","N", "NE", "E", "SE", "S", "SW", "W", "NW"};
    private static final String[] bishopProfile = {"1", "NE", "SE", "SW", "NW"};
    private static final String[] pawnProfile = {"0", "N"};

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
        throw new RuntimeException("Not implemented");
    }
}
