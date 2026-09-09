package chess;

public class CustomTesting {
    public static void main(String[] args) {
        ChessBoard myBoard = new ChessBoard();
        myBoard.printBoard();
        myBoard.addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        System.out.print("\n");
        myBoard.printBoard();
    }
}
