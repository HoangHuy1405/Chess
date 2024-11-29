package Logic.Piece;

import Logic.Board;
import Logic.Movement.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Piece {
    public boolean hasMoved;
    protected PieceType type;
    protected PieceColor color;

    protected Piece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
        hasMoved = false;
    }

    public PieceColor getColor() {
        return color;
    }
    public PieceType getType() {
        return type;
    }

    protected Map<Position, Move> generateMovesFromDirs(Board board, Position curPos, Direction[] directions){
        Map<Position, Move> moves = new HashMap<>();
        for(Direction d : directions){
            for(int i = 1; i <= 8; i++){
                Position finalPos = PositionCalculation.CalculateDestination(curPos, Direction.CalculateScalarDirection(d, i));
                if(board.isOutOfBoard(finalPos)) break;

                Piece piece = board.getPiece(finalPos);

                if (piece == null){
                    moves.put(finalPos, new NormalMove(curPos, finalPos));
                    continue;
                }

                if(!this.isSameColor(piece)) {
                    moves.put(finalPos, new NormalMove(curPos, finalPos));
                }
                break;
            }
        }

        return moves;
    }
    protected Map<Position, Move> generateMoveFromDirs(Board board, Position curPos, Direction[] directions){
        Map<Position, Move> moves = new HashMap<>();
        for(Direction direction : directions) {
            Position finalPos = PositionCalculation.CalculateDestination(curPos, direction);

            Piece piece = board.getPiece(finalPos);

            if (piece != null) {
                if(this.isSameColor(piece))
                    continue;
            }
            moves.put(finalPos, new NormalMove(curPos, finalPos));
        }

        return moves;
    }
    protected boolean isSameColor(Piece piece) {
        return this.color == piece.getColor();
    }

    abstract public Map<Position, Move> getLegalMoves(Board board, Position curPos);
}
