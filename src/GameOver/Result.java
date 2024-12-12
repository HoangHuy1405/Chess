package GameOver;

import Piece.Player;

public class Result {
    private Player player;
    private EndReason reason;

    public Result(Player player, EndReason reason) {
        this.player = player;
        this.reason = reason;
    }
    public EndReason getReason() {
        return reason;
    }

    public static Result Win(Player player) {
        return new Result(player, EndReason.Checkmate);
    }

    public static Result Draw(EndReason endReason) {
        return new Result(Player.none, endReason);
    }
}
