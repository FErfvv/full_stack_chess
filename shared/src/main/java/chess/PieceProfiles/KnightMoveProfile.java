package chess.PieceProfiles;

public class KnightMoveProfile extends ProfileTemplate{
    public KnightMoveProfile() {
        super(false, new int [][] { {-1,2},{1,2},{2,1},{2,-1},{1,-2},{-1,-2},{-2,1},{-2,-1}});
    }
}