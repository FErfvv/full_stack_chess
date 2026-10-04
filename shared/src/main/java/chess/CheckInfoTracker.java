package chess;

import java.util.ArrayList;
import java.util.Collection;

public class CheckInfoTracker {
    private Collection<ChessPosition> attackedPositions;
    private Collection<ChessPosition> kingFuturePos;
    private Collection<ChessPosition> attackingPieces;
    private boolean kingIsAttacked = false;

    public void setKingIsAttacked(boolean kingIsAttacked) {
        this.kingIsAttacked = kingIsAttacked;
    }

    public void setKingFuturePos(Collection<ChessPosition> kingFuturePos) {
        this.kingFuturePos = kingFuturePos;
    }

    public Collection<ChessPosition> getKingFuturePos() {
        return kingFuturePos;
    }

    public boolean isKingIsAttacked() {
        return kingIsAttacked;
    }

    public CheckInfoTracker() {
        this.attackedPositions = new ArrayList<>();
        this.attackingPieces = new ArrayList<>();
        this.kingFuturePos = new ArrayList<>();
    }

    public void addAttackingPiece(ChessPosition attackPiecePos) {
        attackingPieces.add(attackPiecePos);
    }

    public void addAttackedPos(ChessPosition attackedPos) {
        attackedPositions.add(attackedPos);
    }


    public Collection<ChessPosition> getAttackingPieces() {
        return attackingPieces;
    }

    public Collection<ChessPosition> getAttackedPositions() {
        return attackedPositions;
    }

    public void setAttackedPositions(Collection<ChessPosition> attackedPositions) {
        this.attackedPositions = attackedPositions;
    }

    public void setAttackingPieces(Collection<ChessPosition> attackingPieces) {
        this.attackingPieces = attackingPieces;
    }


}
