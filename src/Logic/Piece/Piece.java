package Logic.Piece;

import Logic.Board;
import Logic.Move;
import Logic.Movement.Position;

import java.util.List;

public abstract class Piece {
    protected PieceType type;
    protected PieceColor color;

    protected Piece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
    }

    abstract public List<Move> getLegalMoves(Board board, Position curPos);
}
