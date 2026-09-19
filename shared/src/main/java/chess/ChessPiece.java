package chess;

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
        if (myPiece.getPieceType() == PieceType.PAWN) {
            checkPawn(board,myPosition,myMoves);
        } else {
            getOpenSpaces(PROFILES.get(myPiece.getPieceType()),board, myPosition,myMoves);
        }

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

    private void addPromotionPieces(ChessPosition myPosition, ChessPosition endPosition, List<ChessMove> myMoves) {
        for (PieceType type : PieceType.values()) {
            if (type != PieceType.KING && type != PieceType.PAWN) {
                myMoves.add(new ChessMove(myPosition,endPosition,type));
            }

        }
    }

    private void checkPawn(ChessBoard board, ChessPosition myPosition, List<ChessMove> myMoves) {
        int direction = 1;
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
            direction = -1;
        }
        int rowMove1 = myPosition.getRow() + direction;
        if (rowMove1 > 0 && rowMove1 < 9) {
            ChessPosition movePos1 = new ChessPosition(rowMove1,myPosition.getColumn());
            ChessPiece move1 = board.getPiece(movePos1);
            if (move1 == null) {
                if (movePos1.getRow() == (direction == 1 ? 8 : 1)) {
                    addPromotionPieces(myPosition,movePos1,myMoves);
                } else {
                    myMoves.add(new ChessMove(myPosition,movePos1,null));
                }

                if (myPosition.getRow() == (direction == 1 ? 2 : 7)) {
                    int rowMove2 = myPosition.getRow() + (direction*2);
                    ChessPosition movePos2 = new ChessPosition(rowMove2,myPosition.getColumn());
                    ChessPiece move2 = board.getPiece(movePos2);
                    if (move2 == null) {
                        myMoves.add(new ChessMove(myPosition,movePos2,null));
                    }
                }

            }
            int colMoveLeft = myPosition.getColumn() - 1;
            int colMoveRight = myPosition.getColumn() + 1;
            if (colMoveLeft > 0 && colMoveLeft < 9) {
                ChessPosition movePosLeft = new ChessPosition(rowMove1,colMoveLeft);
                ChessPiece moveLeft = board.getPiece(movePosLeft);
                if (moveLeft != null && moveLeft.getTeamColor() != this.getTeamColor()) {
                    if (movePosLeft.getRow() == (direction == 1 ? 8 : 1)) {
                        addPromotionPieces(myPosition,movePosLeft,myMoves);
                    } else {
                        myMoves.add(new ChessMove(myPosition,movePosLeft,null));
                    }
                }

            }
            if (colMoveRight > 0 && colMoveRight < 9) {
                ChessPosition movePosRight = new ChessPosition(rowMove1,colMoveRight);
                ChessPiece moveRight = board.getPiece(movePosRight);
                if (moveRight != null && moveRight.getTeamColor() != this.getTeamColor()) {

                    if (movePosRight.getRow() == (direction == 1 ? 8 : 1)) {
                        addPromotionPieces(myPosition,movePosRight,myMoves);
                    } else {
                        myMoves.add(new ChessMove(myPosition,movePosRight,null));
                    }
                }
            }
        }


        ChessPosition captureLeft = new ChessPosition(myPosition.getRow() - 1,myPosition.getColumn()-1);
        ChessPosition captureRight = new ChessPosition(myPosition.getRow() + 1,myPosition.getColumn()-1);
        if (myPosition.getRow() == 6) {
            ChessPosition move2 = new ChessPosition(myPosition.getRow(),myPosition.getColumn()-2);


        }






    }
}
