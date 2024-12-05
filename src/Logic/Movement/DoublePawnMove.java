package Logic.Movement;

import Logic.Board;

public class DoublePawnMove extends Move{
    public DoublePawnMove(Position fromPos, Position toPos) {
        super(fromPos, toPos);
    }

    @Override
    public void execute(Board board) {
        board.lastDoublePawnMove = toPos;
        new NormalMove(fromPos, toPos).execute(board);
    }
}
