package Logic.Piece;

import Logic.Board;
import Logic.Movement.Move;
import Logic.Movement.Direction;
import Logic.Movement.Position;

import java.util.Map;

public class Rook extends Piece {
    private static final Direction[] directions = {
        new Direction(0, 1), //go right
        new Direction(1, 0), //go forward
        new Direction(0, -1), // go left
        new Direction(-1, 0), // go backward
    };

    public Rook(Player color){
        super(PieceType.rook, color);

    }

    @Override
    public Map<Position, Move> getMoves(Board board, Position curPos){
        return generateMovesFromDirs(board, curPos, directions);
    }

    private boolean isSamePieceColor(Piece piece){
        return this.color == piece.color;
    }
}
