package Logic.Piece;

import Logic.Board;
import Logic.Movement.Move;
import Logic.Movement.Direction;
import Logic.Movement.Position;

import java.util.List;
import java.util.Map;

public class Queen extends Piece{
    private static final Direction[] directions = {
        new Direction(0,1),
        new Direction(0,-1),
        new Direction(1,0),
        new Direction(-1,0),
        new Direction(1,1),
        new Direction(1,-1),
        new Direction(-1,1),
        new Direction(-1,-1)
    };


    public Queen(PieceColor color) {
        super(PieceType.queen, color);
    }

    @Override
    public Map<Position, Move> getLegalMoves(Board board, Position curPos){
        return generateMovesFromDirs(board, curPos, directions);
    }


}
