package Evaluate;

import GameLogic.Board;
import GameLogic.Move.Move;
import static GameLogic.Move.MoveGenerator.*;
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

    public Move randomMove(Board board) {
        List<Move> legalMoves = getAllLegalMoves(board);
        if (legalMoves.isEmpty()) return null; // No moves available, likely game over
        // Random move selection for now
        Random random = new Random();
        return legalMoves.get(random.nextInt(legalMoves.size()));
    }
    public void move(Board board)  {
        Move move = Evaluate.startSearch(5, board);
        System.out.println(move);
        board.makeMove(move);

    }
}
