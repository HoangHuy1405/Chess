package Piece;

public enum PieceType {
    None(0),
    Pawn(1),
    Knight(2),
    Bishop(3),
    Rook(4),
    Queen(5),
    King(6);

    public final int value;

    PieceType(int value) {
        this.value = value;
    }
}
