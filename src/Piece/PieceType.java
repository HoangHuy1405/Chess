package Piece;

public enum PieceType {
    pawn(1,0),
    knight(3,1),
    bishop(3,2),
    rook(5,3),
    queen(9,4),
    king(100,5);

    private final int value;
    private final int index;

    PieceType(int value, int index) {
        this.value = value;
        this.index = index;
    }

    public int getValue() {
        return value;
    }
    public int getTypeIndex() {
        return index;
    }
}
