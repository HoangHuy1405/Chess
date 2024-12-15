package Piece;

public enum PieceColor {
    black(0),
    white(1),
    none(-1);

    private final int index;

    PieceColor(int index) {
        this.index = index;
    }
    public int getColorIndex() {
        return index;
    }

    public PieceColor Opponent() {
        return switch (this) {
            case white -> PieceColor.black;
            case black -> PieceColor.white;
            default -> PieceColor.none;
        };
    }
}
