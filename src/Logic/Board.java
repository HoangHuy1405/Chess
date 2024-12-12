package Logic;

import Move.Move;
import Piece.*;
import Position.Direction;
import Position.Position;
import Position.PositionCalculation;

import java.util.List;

public class Board {
    private Piece[][] board;
    public Position lastDoublePawnMove;

    public Board() {
        board = new Piece[8][8];
    }

    public void InitializeBoard(){
        Fen.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", this);
    }

    public Piece[][] getBoard(){
        return board;
    }

    public Piece getPiece(Position pos) {
        return board[pos.getRow()][pos.getCol()];
    }
    public void setPiece(Piece piece, Position pos) {
        board[pos.getRow()][pos.getCol()] = piece;
    }
    public void removePiece(Position pos) {
        board[pos.getRow()][pos.getCol()] = null;
    }
    public void clearBoard() {
        board = new Piece[8][8];
    }
    public Board copy() {
        Board newBoard = new Board();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position pos = new Position(i, j);
                Piece piece = this.getPiece(pos);
                if (piece != null) {
                    newBoard.setPiece(piece.copy(), pos);
                }
            }
        }
        return newBoard;
    }

    public boolean isOutOfBoard(Position pos) {
        return pos.getRow() > 7 || pos.getCol() > 7 || pos.getRow() < 0 || pos.getCol() < 0;
    }
    public boolean isInCheck(Player color) {
        Position kingPos = findKing(color);
        if(kingPos == null) return false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = getPiece(new Position(i, j));
                if (piece == null) continue;
                if(piece.getColor() == color) {
                    continue;
                }
                Position pos = new Position(i, j);
                List<Move> moves = piece.getMoves(this, pos);

                for(Move move : moves)
                    if(move.getToPos().equals(kingPos))
                        return true;


            }
        }

        return false;
    }

    public boolean isInCheckAt(Position pos, Player color) {
        Direction[] dirs = {
            new Direction(0,1),
            new Direction(0,-1),
            new Direction(1,0),
            new Direction(-1,0),

            new Direction(1,1),
            new Direction(1,-1),
            new Direction(-1,1),
            new Direction(-1,-1),

            new Direction(2, 1),
            new Direction(1, 2),
            new Direction(-2, 1),
            new Direction(-1, 2),
            new Direction(2, -1),
            new Direction(1, -2),
            new Direction(-2, -1),
            new Direction(-1, -2),
        };

        for (Direction dir : dirs) {
            Position checkingPos;
            if(dir.getDeltaRow() == 2 || dir.getDeltaCol() == 2 || dir.getDeltaRow() == -2 || dir.getDeltaCol() == -2){
                checkingPos = PositionCalculation.CalculateDestination(pos, dir);

                if(isOutOfBoard(checkingPos)) continue;
                Piece piece = getPiece(checkingPos);

                if(piece == null) continue;
                if(piece.getColor() == color) continue;

                if(piece.getType() == PieceType.knight) return true;
                continue;
            }

            if(dir.getDeltaRow() == 0 || dir.getDeltaCol() == 0){
                for(int i = 1; i < 8; i++){
                    checkingPos = PositionCalculation.CalculateDestination(pos, Direction.CalculateScalarDirection(dir, i));

                    if(isOutOfBoard(checkingPos)) break;
                    Piece piece = getPiece(checkingPos);

                    if(piece == null) continue;
                    if(piece.getColor() == color) break;
                    if(i == 1 && piece.getType() == PieceType.king) return true;

                    if(piece.getType() == PieceType.queen || piece.getType() == PieceType.rook) return true;
                }
                continue;
            }

            for(int i = 1; i < 8; i++){
                checkingPos = PositionCalculation.CalculateDestination(pos, Direction.CalculateScalarDirection(dir, i));

                if(isOutOfBoard(checkingPos)) break;
                Piece piece = getPiece(checkingPos);

                if(piece == null) continue;
                if(piece.getColor() == color) break;
                if(i == 1 && (piece.getType() == PieceType.king || piece.getType() == PieceType.pawn)) return true;

                if(piece.getType() == PieceType.queen || piece.getType() == PieceType.bishop) return true;
            }
        }

        return false;
    }

    private Position findKing(Player color){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = getPiece(new Position(i, j));
                if(piece == null) continue;
                if(piece.getColor() == color && piece instanceof King) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

}
