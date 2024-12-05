package Logic.Piece;

import Logic.Board;
import Logic.Movement.Move;
import Logic.Movement.Direction;
import Logic.Movement.Position;

import java.util.Map;

public class Bishop extends Piece {
    private static final Direction[] directions = {
        new Direction(1, 1), // go to the North East
        new Direction(1, -1), // go to the North West
        new Direction(-1, 1), // go to the South East
        new Direction(-1, -1), // go to the South West
    };

    public Bishop(Player color) {
        super(PieceType.bishop, color);
    }

    @Override
    public Map<Position, Move> getMoves(Board board, Position curPos) {
        return generateMovesFromDirs(board, curPos, directions);
    }

}
