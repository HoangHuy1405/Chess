package Piece;

import Logic.Board;
import Move.Move;
import Position.Direction;
import Position.Position;

import java.util.List;


public class Rook extends Piece {
    private static final Direction[] directions = {
        new Direction(0, 1), //go right
        new Direction(1, 0), //go forward
        new Direction(0, -1), // go left
        new Direction(-1, 0), // go backward
    };

    public Rook(PieceColor color, Position position){
        super(PieceType.rook, color, position);
    }

    @Override
    public List<Move> getMoves(Board board, Position curPos){
        return generateMovesFromDirs(board, curPos, directions);
    }

    private boolean isSamePieceColor(Piece piece){
        return this.color == piece.color;
    }
}
