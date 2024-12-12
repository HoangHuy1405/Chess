package Move;

import Logic.Board;
import Position.Position;

public class DoublePawnMove extends Move{
    public DoublePawnMove(Position fromPos, Position toPos) {
        super(fromPos, toPos, MoveType.DoublePawn);
    }

    @Override
    public void execute(Board board) {
        board.lastDoublePawnMove = toPos;
        new NormalMove(fromPos, toPos).execute(board);
    }
}
