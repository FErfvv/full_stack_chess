package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private static final int BOARD_HEIGHT = 8;
    private static final int BOARD_WIDTH = 8;
    private ChessPiece myBoard[][] = new ChessPiece[BOARD_HEIGHT][BOARD_WIDTH];

    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (position.getColumn()-1 >= 0 && position.getColumn()-1 <= BOARD_WIDTH - 1
            && position.getRow()-1 >= 0 && position.getRow()-1 <= BOARD_HEIGHT - 1) {
            if (myBoard[position.getRow()-1][position.getColumn()-1] == null){
                myBoard[position.getRow()-1][position.getColumn()-1] = piece;
            }
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }

    public void printBoard() {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            System.out.print("|");
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (myBoard[row][col] == null) {
                    System.out.print(" |");
                } else {
                    System.out.print("x|");
                }
            }
            System.out.print("\n");
        }
    }
}
