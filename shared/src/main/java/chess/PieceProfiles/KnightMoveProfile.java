package chess.PieceProfiles;

public class KnightMoveProfile extends PieceProfileTemplate {
    public KnightMoveProfile() {
        super(false, new int [][] { {-1,2},{1,2},{2,1},{2,-1},{1,-2},{-1,-2},{-2,1},{-2,-1}});
    }
}