package Evaluate;

import Logic.GameManager;
import Move.Move;
import Piece.PieceColor;

import java.util.List;
import java.util.Random;

public class ChessBot {
    private PieceColor botColor;

    public ChessBot(PieceColor botColor) {
        this.botColor = botColor;
    }

    public PieceColor getBotColor() {
        return botColor;
    }

    public Move randomMove(GameManager gameManager) {
        List<Move> legalMoves = gameManager.getAllLegalMoves();
        if (legalMoves.isEmpty()) return null; // No moves available, likely game over

        // Random move selection for now
        Random random = new Random();
        return legalMoves.get(random.nextInt(legalMoves.size()));
    }
    public Move move(GameManager gameManager) {
        // alpha, best already explored option along path to the root for maximizer
        // beta, best already explored option along path to the root for minimizer
        return Evaluate.startSearch(4);
    }







}
