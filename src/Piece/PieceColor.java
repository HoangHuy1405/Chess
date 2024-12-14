package Piece;

public enum PieceColor {
    black,
    white,
    none;

    public PieceColor Opponent() {
        return switch (this) {
            case white -> PieceColor.black;
            case black -> PieceColor.white;
            default -> PieceColor.none;
        };
    }
}
