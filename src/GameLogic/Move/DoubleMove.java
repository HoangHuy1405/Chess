package GameLogic.Move;

import GameLogic.Board;

public class DoubleMove extends Move {
    public DoubleMove(int fromPos, int toPos) {
        super(fromPos, toPos);
    }

    @Override
    public void execute(Board board){
        super.execute(board);
        board.lastDoubleMove = Math.abs(toPos + fromPos)/2;
    }
}
