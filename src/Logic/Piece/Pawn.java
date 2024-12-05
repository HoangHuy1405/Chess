package Logic.Piece;

import Logic.Board;
import Logic.Movement.*;

import java.util.HashMap;
import java.util.Map;

public class Pawn extends Piece {
    private final Direction direction;

    public Pawn(Player color) {
        super(PieceType.pawn, color);

        if(color == Player.white){
            direction = new Direction(-1, 0);
        }else{
            direction = new Direction(1, 0);
        }
    }

    @Override
    public Map<Position, Move> getMoves(Board board, Position curPos) {
        Map<Position, Move> pawnMoves = generatePawnMoves(board, curPos);
        Map<Position, Move> pawnCapture = generatePawnCapture(board, curPos);

        if(!pawnCapture.isEmpty()){
            pawnMoves.putAll(pawnCapture);
        }

        return pawnMoves;
    }

    private Map<Position, Move> generatePawnMoves(Board board, Position curPos){
        Map<Position, Move> moves = new HashMap<>();

        Position finalPos = PositionCalculation.CalculateDestination(curPos, direction);
        if(board.isOutOfBoard(finalPos)) return moves;

        Piece piece = board.getPiece(finalPos);
        if(piece != null) return moves;

        moves.put(finalPos, new NormalMove(curPos, finalPos));

        if(!hasMoved){
            finalPos = PositionCalculation.CalculateDestination(curPos, Direction.CalculateScalarDirection(direction, 2));
            if(board.isOutOfBoard(finalPos)) return moves;

            piece = board.getPiece(finalPos);
            if(piece != null) return moves;

            moves.put(finalPos, new DoublePawnMove(curPos, finalPos));
        }

        return moves;
    }
    private Map<Position, Move> generatePawnCapture(Board board, Position curPos){
        Map<Position, Move> moves = new HashMap<>();

        Position diagonal1 = PositionCalculation.CalculateDestination(curPos, Direction.CalculatePlusDirection(direction, new Direction(0, 1)));
        Position diagonal2 = PositionCalculation.CalculateDestination(curPos, Direction.CalculatePlusDirection(direction, new Direction(0, -1)));

        if(!board.isOutOfBoard(diagonal1)){
            Piece piece = board.getPiece(diagonal1);
            if(piece != null){
                if(!isSameColor(piece)) moves.put(diagonal1, new NormalMove(curPos, diagonal1));
            }
        }

        if(!board.isOutOfBoard(diagonal2)){
            Piece piece = board.getPiece(diagonal2);
            if(piece != null){
                if(!isSameColor(piece)) moves.put(diagonal2, new NormalMove(curPos, diagonal2));
            }
        }

        Position lastDoublePawnMove = board.lastDoublePawnMove;

        if(lastDoublePawnMove != null){
            Position positionEnPassant = new Position(curPos.getRow() + direction.getDeltaRow(), lastDoublePawnMove.getCol());
            moves.put(positionEnPassant, new EnPassantMove(curPos, positionEnPassant, lastDoublePawnMove));
        }

        return moves;
    }
}
