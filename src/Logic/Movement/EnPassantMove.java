package Logic.Movement;

import Logic.Board;

public class EnPassantMove extends Move{
    private final Position lastDoublePawnMove;

    public EnPassantMove(Position fromPos, Position toPos, Position lastDoublePawnMove) {
        super(fromPos, toPos);
        this.lastDoublePawnMove = lastDoublePawnMove;
    }

    @Override
    public void execute(Board board) {
        board.removePiece(lastDoublePawnMove);
        new NormalMove(fromPos, toPos).execute(board);
    }
}
