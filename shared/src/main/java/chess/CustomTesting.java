package chess;

public class CustomTesting {
    public static void main(String[] args) {
        ChessBoard myBoard = new ChessBoard();

        ChessPiece myRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPosition myRookPosition = new ChessPosition(3,3);
        myBoard.addPiece(myRookPosition,myRook);
        myBoard.toString();
        myRook.pieceMoves(myBoard,myRookPosition);
        myBoard.toString();
    }
}
