package Logic.Movement;

import Logic.Board;

public abstract class Move {
    protected Position fromPos;
    protected Position toPos;

    protected Move(Position fromPos, Position toPos) {
        this.fromPos = fromPos;
        this.toPos = toPos;
    }

    public abstract void execute(Board board);
}
