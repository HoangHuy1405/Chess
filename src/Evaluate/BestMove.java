package Evaluate;

import Move.Move;

public class BestMove {
    public Move move;

    @Override
    public String toString() {
        return "best move " + move.toString();
    }
}
