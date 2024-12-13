package Piece;

import Logic.Board;
import Position.Direction;
import Move.*;
import Position.Position;
import Position.PositionCalculation;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    private final Direction direction;

    public Pawn(Player color, Position position) {
        super(PieceType.pawn, color, position);

        if(color == Player.white){
            direction = new Direction(-1, 0);
        }else{
            direction = new Direction(1, 0);
        }
    }

    @Override
    public List<Move> getMoves(Board board, Position curPos) {
        List<Move> pawnMoves = generatePawnMoves(board, curPos);
        List<Move> pawnCapture = generatePawnCapture(board, curPos);

        if(!pawnCapture.isEmpty()){
            pawnMoves.addAll(pawnCapture);
        }

        return pawnMoves;
    }

    private List<Move> generatePawnMoves(Board board, Position curPos){
        List<Move> moves = new ArrayList<>();

        Position finalPos = PositionCalculation.CalculateDestination(curPos, direction);
        if(board.isOutOfBoard(finalPos)) return moves;

        Piece piece = board.getPiece(finalPos);
        if(piece != null) return moves;

        if(finalPos.getRow() == 7 || finalPos.getRow() == 0) {
            moves.add(new PromotePawnMove(curPos, finalPos, new Rook(color, finalPos)));
            moves.add(new PromotePawnMove(curPos, finalPos, new Queen(color, finalPos)));
            moves.add(new PromotePawnMove(curPos, finalPos, new Knight(color, finalPos)));
            moves.add(new PromotePawnMove(curPos, finalPos, new Bishop(color, finalPos)));
        }else{
            moves.add(new NormalMove(curPos, finalPos));
        }

        if(!hasMoved && (curPos.getRow() == 6 || curPos.getRow() == 1)){
            finalPos = PositionCalculation.CalculateDestination(curPos, Direction.CalculateScalarDirection(direction, 2));
            if(board.isOutOfBoard(finalPos)) return moves;

            piece = board.getPiece(finalPos);
            if(piece != null) return moves;

            moves.add(new DoublePawnMove(curPos, finalPos));
        }

        return moves;
    }
    private List<Move> generatePawnCapture(Board board, Position curPos){
        List<Move> moves = new ArrayList<>();

        Position diagonal1 = PositionCalculation.CalculateDestination(curPos, Direction.CalculatePlusDirection(direction, new Direction(0, 1)));
        Position diagonal2 = PositionCalculation.CalculateDestination(curPos, Direction.CalculatePlusDirection(direction, new Direction(0, -1)));

        if(!board.isOutOfBoard(diagonal1)){
            Piece piece = board.getPiece(diagonal1);
            if(piece != null){
                if(!isSameColor(piece))
                    if(diagonal1.getRow() == 7 || diagonal1.getRow() == 0) {
                        moves.add(new PromotePawnMove(curPos, diagonal1, new Rook(color, diagonal1)));
                        moves.add(new PromotePawnMove(curPos, diagonal1, new Queen(color, diagonal1)));
                        moves.add(new PromotePawnMove(curPos, diagonal1, new Knight(color, diagonal1)));
                        moves.add(new PromotePawnMove(curPos, diagonal1, new Bishop(color, diagonal1)));
                    }else{
                        moves.add(new NormalMove(curPos, diagonal1));
                    }
            }
        }

        if(!board.isOutOfBoard(diagonal2)){
            Piece piece = board.getPiece(diagonal2);
            if(piece != null){
                if(!isSameColor(piece))
                    if(diagonal2.getRow() == 7 || diagonal2.getRow() == 0) {
                        moves.add(new PromotePawnMove(curPos, diagonal2, new Rook(color, diagonal2)));
                        moves.add(new PromotePawnMove(curPos, diagonal2, new Queen(color, diagonal2)));
                        moves.add(new PromotePawnMove(curPos, diagonal2, new Knight(color, diagonal2)));
                        moves.add(new PromotePawnMove(curPos, diagonal2, new Bishop(color, diagonal2)));
                    }else{
                        moves.add(new NormalMove(curPos, diagonal2));
                    }
            }
        }

        //En Passant Logic
        Position lastDoublePawnMove = board.lastDoublePawnMove;

        if(lastDoublePawnMove != null){
            Position positionEnPassant;

            if(color.equals(Player.white))
                positionEnPassant = new Position(lastDoublePawnMove.getRow() - 1, lastDoublePawnMove.getCol());
            else
                positionEnPassant = new Position(lastDoublePawnMove.getRow() + 1, lastDoublePawnMove.getCol());


            if(diagonal1.equals(positionEnPassant))
                moves.add(new EnPassantMove(curPos, diagonal1, lastDoublePawnMove));

            if(diagonal2.equals(positionEnPassant))
                moves.add(new EnPassantMove(curPos, diagonal2, lastDoublePawnMove));
        }

        return moves;
    }
}
