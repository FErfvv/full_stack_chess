package chess.PieceProfiles;

public class KingMoveProfile extends ProfileTemplate{
    public KingMoveProfile() {
        super(false, new int[][]{{0, 1}, {1, 1}, {1, 0}, {1,-1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}});
    }
}
