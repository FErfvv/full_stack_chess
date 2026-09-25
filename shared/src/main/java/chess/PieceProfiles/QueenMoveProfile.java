package chess.PieceProfiles;

public class QueenMoveProfile extends PieceProfileTemplate {
    public QueenMoveProfile() {
        super(true, new int[][]{{0, 1}, {1, 1}, {1, 0}, {1,-1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}});
    }
}
