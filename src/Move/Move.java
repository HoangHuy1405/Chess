package Move;

import Logic.Board;
import Position.Position;

public abstract class Move {
    protected MoveType type;
    protected Position fromPos;
    protected Position toPos;
    protected int moveScore;

    protected Move(Position fromPos, Position toPos, MoveType type) {
        this.fromPos = fromPos;
        this.toPos = toPos;
        this.type = type;
    }

    public abstract void execute(Board board);

    public MoveType getType() {
        return type;
    }
    public Position getFromPos() {
        return fromPos;
    }
    public Position getToPos() {
        return toPos;
    }
    public int getMoveScore() {
        return moveScore;
    }
    public void setMoveScore(int moveScore) {
        this.moveScore = moveScore;
    }

    @Override
    public String toString() {
        return "Move{" +
                "type=" + type +
                ", fromPos=" + fromPos +
                ", toPos=" + toPos +
                '}';
    }
}
