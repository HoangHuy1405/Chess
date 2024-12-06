package Logic.Piece;

import Logic.Board;

import Logic.Move.*;
import Logic.Position.Direction;
import Logic.Position.Position;
import Logic.Position.PositionCalculation;

import java.util.HashMap;
import java.util.Map;

public abstract class Piece {
    public boolean hasMoved;
    protected PieceType type;
    protected Player color;

    protected Piece(PieceType type, Player color) {
        this.type = type;
        this.color = color;
        hasMoved = false;
    }

    public Player getColor() {
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
            if(board.isOutOfBoard(finalPos)) continue;
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

    abstract public Map<Position, Move> getMoves(Board board, Position curPos);

    public Piece copy(){
        switch(this.type){
            case king -> {
                Piece king = new King(this.color);
                king.hasMoved = this.hasMoved;
                return king;
            }
            case queen -> {
                Piece queen = new Queen(this.color);
                queen.hasMoved = this.hasMoved;
                return queen;
            }
            case bishop -> {
                Piece bishop = new Bishop(this.color);
                bishop.hasMoved = this.hasMoved;
                return bishop;
            }
            case knight -> {
                Piece knight = new Knight(this.color);
                knight.hasMoved = this.hasMoved;
                return knight;
            }
            case rook -> {
                Piece rook = new Rook(this.color);
                rook.hasMoved = this.hasMoved;
                return rook;
            }
            case pawn -> {
                Piece pawn = new Pawn(this.color);
                pawn.hasMoved = this.hasMoved;
                return pawn;
            }
            default -> {
                return null;
            }
        }
    }
}
