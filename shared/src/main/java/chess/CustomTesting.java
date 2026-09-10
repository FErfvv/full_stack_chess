package chess;

public class CustomTesting {
    public static void main(String[] args) {
        ChessBoard myBoard = new ChessBoard();
        myBoard.printBoard();
        myBoard.resetBoard();
        System.out.print("\n");
        myBoard.printBoard();
    }
}
