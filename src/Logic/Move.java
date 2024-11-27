package Logic;

import Logic.Movement.Position;
import Logic.Piece.Piece;

public class Move {
    private Position fromPos;
    private Position toPos;

    public Move(Position fromPos, Position toPos) {
        this.fromPos = fromPos;
        this.toPos = toPos;
    }

    public void move(Board board) {
        Piece piece = board.getPiece(fromPos);
        board.removePiece(fromPos);
        board.setPiece(piece, toPos);
    }
}
