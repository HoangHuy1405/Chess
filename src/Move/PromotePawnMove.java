package Move;

import Logic.Board;
import Piece.Piece;
import Position.Position;

public class PromotePawnMove extends Move{
    private Piece piece;

    public PromotePawnMove(Position fromPos, Position toPos) {
        super(fromPos, toPos, MoveType.PawnPromote);
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    public void execute(Board board) {
        board.removePiece(fromPos);
        board.setPiece(piece, toPos);
    }
}
