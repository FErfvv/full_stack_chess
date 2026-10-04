package chess;


public class CustomTesting {
    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        ChessBoard myBoard = new ChessBoard();


        ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece rook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

        ChessPosition blackKingPos = new ChessPosition(1,4);
        ChessPosition whiteKingPos = new ChessPosition(8,4);
        ChessPosition rookPos = new ChessPosition(6,5);

        myBoard.addPiece(blackKingPos,blackKing);
        myBoard.addPiece(whiteKingPos,whiteKing);
        myBoard.addPiece(rookPos,rook);

        game.setBoard(myBoard);
        game.isInCheckmate(ChessGame.TeamColor.WHITE);
        System.out.println(myBoard.toString());

    }
}
