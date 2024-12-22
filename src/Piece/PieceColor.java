package Piece;

public enum PieceColor {
    White(0),
    Black(8),
    None(-1);
    public final int value;

    PieceColor(int value) {
        this.value = value;
    }

}
