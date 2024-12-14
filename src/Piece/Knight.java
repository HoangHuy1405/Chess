package Piece;

import Logic.Board;

import Move.*;
import Position.Direction;
import Position.Position;

import java.util.List;

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

    public Knight(PieceColor color, Position position) {
        super(PieceType.knight, color, position);
    }

    @Override
    public List<Move> getMoves(Board board, Position curPos) {
        return generateMoveFromDirs(board, curPos, directions);
    }

}
