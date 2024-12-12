package Piece;

import Logic.Board;
import Move.Move;
import Position.Direction;
import Position.Position;

import java.util.List;

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


    public Queen(Player color, Position position) {
        super(PieceType.queen, color, position);
    }

    @Override
    public List<Move> getMoves(Board board, Position curPos){
        return generateMovesFromDirs(board, curPos, directions);
    }


}
