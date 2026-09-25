package chess.PieceProfiles;

public class RookMoveProfile extends ProfileTemplate{
    public RookMoveProfile() {
        super(true, new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}});
    }
}
