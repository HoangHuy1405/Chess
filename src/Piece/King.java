package Piece;

import Logic.Board;

import Move.Castle;
import Move.Move;
import Move.MoveType;
import Position.Direction;
import Position.Position;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private static final Direction[] directions = {
        new Direction(0, 1),
        new Direction(0, -1),

        new Direction(1, 0),
        new Direction(-1, 0),

        new Direction(1, -1),
        new Direction(-1, 1),

        new Direction(1, 1),
        new Direction(-1, -1),
    };

    public King(Player color, Position position) {
        super(PieceType.king, color, position);
    }

    @Override
    public List<Move> getMoves(Board board, Position curPos) {
        List<Move> moves = generateMoveFromDirs(board, curPos, directions);
        moves.addAll(getCastleMoves(board, curPos));
        return moves;
    }

    public List<Move> getCastleMoves(Board board, Position curPos){
        List<Move> moves = new ArrayList<>();
        if(hasMoved || board.isInCheckAt(curPos, color)) return moves;

        Position[] QS = {
                new Position(curPos.getRow(), 1),
                new Position(curPos.getRow(), 2),
                new Position(curPos.getRow(), 3),
        };
        Position[] KS = {
                new Position(curPos.getRow(), 5),
                new Position(curPos.getRow(), 6),
        };

        if(hasMoved || board.isInCheckAt(curPos, color)) return moves;

        Position pos = new Position(curPos.getRow(), 0);
        Piece rook = board.getPiece(pos);

        if(rook != null && rook.getType() == PieceType.rook && !rook.hasMoved) {
            boolean flag = true;
            for(Position p : QS){
                Piece piece = board.getPiece(p);
                if(piece != null) {
                    flag = false;
                    break;
                }

                if(board.isInCheckAt(p, color)){
                    flag = false;
                    break;
                }
            }
            if(flag) {
                Position kingToPos = new Position(curPos.getRow(), 2);
                moves.add(new Castle(curPos, kingToPos, MoveType.QSCastle, pos));
            }
        }

        pos = new Position(curPos.getRow(), 7);
        rook = board.getPiece(pos);

        if(rook != null && rook.getType() == PieceType.rook && !rook.hasMoved) {
            boolean flag = true;
            for(Position p : KS){
                Piece piece = board.getPiece(p);
                if(piece != null) {
                    flag = false;
                    break;
                }

                if(board.isInCheckAt(p, color)){
                    flag = false;
                    break;
                }
            }
            if(flag) {
                Position kingToPos = new Position(curPos.getRow(), 6);
                moves.add(new Castle(curPos, kingToPos, MoveType.KSCastle, pos));
            }
        }
        return moves;
    }
}
