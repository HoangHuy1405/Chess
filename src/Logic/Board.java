package Logic;

import Logic.Movement.Position;
import Logic.Piece.Piece;

public class Board {
    private Piece[][] board;

    public Board() {
        board = new Piece[8][8];
    }

    public Piece getPiece(Position pos) {
        return board[pos.getX()][pos.getY()];
    }

    public void setPiece(Piece piece, Position pos) {
        board[pos.getX()][pos.getY()] = piece;
    }

    public void removePiece(Position pos) {
        board[pos.getX()][pos.getY()] = null;
    }
}
