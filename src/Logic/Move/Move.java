package Logic.Move;

import Logic.Board;
import Logic.Position.Position;

public abstract class Move {
    protected MoveType type;
    protected Position fromPos;
    protected Position toPos;

    protected Move(Position fromPos, Position toPos, MoveType type) {
        this.fromPos = fromPos;
        this.toPos = toPos;
        this.type = type;
    }

    public abstract void execute(Board board);
}
