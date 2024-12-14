package Piece;

public enum PieceType {
    bishop(3),
    king(100),
    knight(3),
    pawn(1),
    queen(9),
    rook(5);

    private final int value;

    PieceType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
