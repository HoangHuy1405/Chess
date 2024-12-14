package GameOver;

import Piece.PieceColor;

public class Result {
    private PieceColor pieceColor;
    private EndReason reason;

    public Result(PieceColor pieceColor, EndReason reason) {
        this.pieceColor = pieceColor;
        this.reason = reason;
    }
    public EndReason getReason() {
        return reason;
    }

    public static Result Win(PieceColor pieceColor) {
        return new Result(pieceColor, EndReason.Checkmate);
    }

    public static Result Draw(EndReason endReason) {
        return new Result(PieceColor.none, endReason);
    }
}
