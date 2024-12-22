package GameLogic;

import Piece.PieceColor;

import static GameLogic.EndReason.*;
import static Piece.PieceColor.*;

public class Result {
    public PieceColor winner;
    public EndReason reason;

    private Result(PieceColor winner, EndReason reason) {
        this.winner = winner;
        this.reason = reason;
    }

    public static Result Win(PieceColor winner){
        return new Result(winner, Checkmate);
    }

    public static Result Draw(EndReason reason){
        return new Result(None, reason);
    }
}
