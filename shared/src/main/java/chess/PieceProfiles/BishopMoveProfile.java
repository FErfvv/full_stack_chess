package chess.PieceProfiles;

public class BishopMoveProfile extends PieceProfileTemplate {
    public BishopMoveProfile() {
        super(true, new int [][] {{1, 1}, {1,-1}, {-1, -1}, {-1, 1}});
    }
}
