package Logic.Piece;

import Logic.Board;
import Logic.Movement.Move;
import Logic.Movement.Direction;
import Logic.Movement.Position;

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


    public Queen(Player color) {
        super(PieceType.queen, color);
    }

    @Override
    public Map<Position, Move> getMoves(Board board, Position curPos){
        return generateMovesFromDirs(board, curPos, directions);
    }


}
