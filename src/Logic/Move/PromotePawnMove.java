package Logic.Move;

import Logic.Board;
import Logic.Piece.Piece;
import Logic.Position.Position;

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
