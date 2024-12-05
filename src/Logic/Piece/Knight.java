package Logic.Piece;

import Logic.Board;
import Logic.Movement.*;

import java.util.Map;

public class Knight extends Piece {
    private static final Direction[] directions = {
        new Direction(2, 1),
        new Direction(1, 2),

        new Direction(-2, 1),
        new Direction(-1, 2),

        new Direction(2, -1),
        new Direction(1, -2),

        new Direction(-2, -1),
        new Direction(-1, -2),
    };

    public Knight(Player color) {
        super(PieceType.knight, color);
    }

    @Override
    public Map<Position, Move> getMoves(Board board, Position curPos) {
        return generateMoveFromDirs(board, curPos, directions);
    }

}
