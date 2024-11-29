package Logic.Movement;

import Logic.Board;
import Logic.Piece.Piece;

public class NormalMove extends Move{
    public NormalMove(Position fromPos, Position toPos) {
        super(fromPos, toPos);
    }

    @Override
    public void execute(Board board) {
        Piece piece = board.getPiece(fromPos);
        board.setPiece(piece, toPos);
        board.removePiece(fromPos);

        if(!piece.hasMoved) piece.hasMoved = true;
    }
}
