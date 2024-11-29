package Logic.Piece;

import Logic.Board;
import Logic.Movement.Direction;
import Logic.Movement.Move;
import Logic.Movement.Position;

import java.util.Map;

public class King extends Piece {
    private static final Direction[] directions = {
        new Direction(0, 1),
        new Direction(0, -1),

        new Direction(1, 0),
        new Direction(-1, 0),

        new Direction(1, -1),
        new Direction(-1, 1),

        new Direction(1, 1),
        new Direction(-1, -1),
    };

    public King(PieceColor color) {
        super(PieceType.king, color);
    }

    @Override
    public Map<Position, Move> getLegalMoves(Board board, Position curPos) {
        return generateMoveFromDirs(board, curPos, directions);
    }
}
