package Logic.Piece;

import Logic.Board;

import Logic.Move.Castle;
import Logic.Move.Move;
import Logic.Move.MoveType;
import Logic.Position.Direction;
import Logic.Position.Position;

import java.util.HashMap;
import java.util.Map;

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

    public King(Player color) {
        super(PieceType.king, color);
    }

    @Override
    public Map<Position, Move> getMoves(Board board, Position curPos) {
        Map<Position, Move> moves = generateMoveFromDirs(board, curPos, directions);
        moves.putAll(getCastleMoves(board, curPos));
        return moves;
    }

    public Map<Position, Move> getCastleMoves(Board board, Position curPos){
        Map<Position, Move> moves = new HashMap<>();
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
                moves.put(kingToPos, new Castle(curPos, kingToPos, MoveType.QSCastle, pos));
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
                moves.put(kingToPos, new Castle(curPos, kingToPos, MoveType.KSCastle, pos));
            }
        }
        return moves;
    }
}
