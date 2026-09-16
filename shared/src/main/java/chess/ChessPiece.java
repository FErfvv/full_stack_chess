package chess;

import java.text.CollationElementIterator;
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
    private static final Map<Enum, int[][]> profiles = Map.ofEntries(
            entry(PieceType.ROOK,new int[][] {{1}, {0, 1}, {1, 0}, {0, -1}, {-1, 0}}),
            entry(PieceType.KING, new int [][] {{0},{0, 1}, {1, 1}, {1, 0}, {1,-1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}}),
            entry(PieceType.QUEEN, new int [][] {{1},{0, 1}, {1, 1}, {1, 0}, {1,-1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}}),
            entry(PieceType.BISHOP, new int [][] {{1}, {1, 1}, {1,-1}, {-1, -1}, {-1, 1}}),
            entry(PieceType.KNIGHT, new int [][] {{0}, })
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
        getOpenSpaces(profiles.get(myPiece.getPieceType()),board, myPosition,myMoves);
        return myMoves;
    }

    private void getOpenSpaces(int[][] profile, ChessBoard board, ChessPosition myPosition, List<ChessMove> myMoves) {
        int run = profile[0][0];
        int[][] profileMoves = Arrays.copyOfRange(profile,1,profile.length);
        for (int[] direction: profileMoves) {
            move(direction,run,board, myPosition,myMoves);
        }
    }

    private void move(int[] direction, int run, ChessBoard board, ChessPosition myPosition,List<ChessMove> myMoves) {
        int deltaCol = direction[0];
        int deltaRow = direction[1];
        System.out.println("Delta Column: " + deltaCol);
        System.out.println("Delta Row: " + deltaRow);
        int col = myPosition.getColumn() + deltaCol;
        int row = myPosition.getRow() + deltaRow;

        while (col > 0 && col < board.BOARD_WIDTH + 1 && row > 0 && row < board.BOARD_HEIGHT + 1 && (board.getMyBoard()[row-1][col-1] == null || board.getMyBoard()[row-1][col-1].getTeamColor() != board.getPiece(myPosition).getTeamColor())) {

            myMoves.add(new ChessMove(myPosition,new ChessPosition(row,col), null));
            if (run == 0){
                break;
            }
            if (board.getMyBoard()[row-1][col-1] != null) {
                break;
            }
            col += deltaCol;
            row += deltaRow;
        }
    }

}
