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
//        if (myPiece.getPieceType() == PieceType.PAWN) {
//            checkPawnMoves(board,myPosition,myMoves);
//        } else {
//            getOpenSpaces(PROFILES.get(myPiece.getPieceType()),board, myPosition,myMoves);
//        }

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

        while (col > 0 && col < board.BOARD_WIDTH + 1
                && row > 0 && row < board.BOARD_HEIGHT + 1
                && (board.getMyBoard()[row-1][col-1] == null
                || board.getMyBoard()[row-1][col-1].getTeamColor() != board.getPiece(myPosition).getTeamColor())) {

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
    public void checkPawnMoves(ChessBoard board, ChessPosition myPosition, List<ChessMove> myMoves) {
        String[] allMoves = {"left", "right", "forward1","forward2"};
        int direction = 1;
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
            direction = -1;
        }
        Map<String, int[]> pawnProfile = Map.ofEntries(
                Map.entry("left",new int[]{-1,1}),
                Map.entry("right",new int[]{1,1}),
                Map.entry("forward1",new int[]{0,1}),
                Map.entry("forward2",new int[]{0,2})
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
            ChessPosition targetPos = new ChessPosition(row,col);
            switch (move) {
                case "left","right":
                    if (board.getPiece(targetPos) != null && board.getPiece(targetPos).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        if (targetPos.getRow() == (direction == 1 ? 8 : 1)) {
                            addPromotionPieces(myPosition,targetPos,myMoves);
                        } else {
                            myMoves.add(new ChessMove(myPosition, targetPos, null));
                        }
                    }
                    break;
                case "forward1":
                    if (board.getPiece(targetPos) == null) {
                        if (targetPos.getRow() == (direction == 1 ? 8 : 1)) {
                            addPromotionPieces(myPosition,targetPos,myMoves);
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
                        myMoves.add(new ChessMove(myPosition,targetPos,null));
                    }
                    break;
            }
        }
    }

    public void addPromotionPieces(ChessPosition myPosition, ChessPosition targetPosition, List<ChessMove> myMoves) {
        for (PieceType type: PieceType.values()) {
            if (type != PieceType.PAWN && type != PieceType.KING) {
                myMoves.add(new ChessMove(myPosition,targetPosition,type));
            }
        }
    }

    public boolean isOnBoard(int row, int col) {
        return (row > 0 && row < 9 && col > 0 && col < 9);
    }
}
