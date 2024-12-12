package Piece;

public enum Player {
    black,
    white,
    none;

    public Player Opponent() {
        return switch (this) {
            case white -> Player.black;
            case black -> Player.white;
            default -> Player.none;
        };
    }
}
