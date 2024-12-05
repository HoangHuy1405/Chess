package Logic;

import Logic.Movement.Move;
import Logic.Movement.Position;
import Logic.Piece.King;
import Logic.Piece.Piece;
import Logic.Piece.Player;

import java.util.Map;

public class Board {
    private Piece[][] board;

    public Position lastDoublePawnMove;

    public Board() {
        board = new Piece[8][8];
    }

    public void InitializeBoard(){
        Fen.loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", this);
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
                Map<Position, Move> moves = piece.getMoves(this, pos);
                if(moves.containsKey(kingPos)){
                    return true;
                }
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
