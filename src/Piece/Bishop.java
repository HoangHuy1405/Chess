package Piece;

import Logic.Board;
import Move.Move;

import Position.Direction;
import Position.Position;

import java.util.List;

public class Bishop extends Piece {
    private static final Direction[] directions = {
        new Direction(1, 1), // go to the North East
        new Direction(1, -1), // go to the North West
        new Direction(-1, 1), // go to the South East
        new Direction(-1, -1), // go to the South West
    };

    public Bishop(Player color, Position position) {
        super(PieceType.bishop, color, position);
    }

    @Override
    public List<Move> getMoves(Board board, Position curPos) {
        return generateMovesFromDirs(board, curPos, directions);
    }

}
