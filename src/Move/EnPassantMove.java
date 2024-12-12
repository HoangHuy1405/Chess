package Move;

import Logic.Board;
import Position.Position;

public class EnPassantMove extends Move{
    private final Position lastDoublePawnMove;

    public EnPassantMove(Position fromPos, Position toPos, Position lastDoublePawnMove) {
        super(fromPos, toPos, MoveType.EnPassant);
        this.lastDoublePawnMove = lastDoublePawnMove;
    }

    @Override
    public void execute(Board board) {
        board.removePiece(lastDoublePawnMove);
        new NormalMove(fromPos, toPos).execute(board);
    }
}
