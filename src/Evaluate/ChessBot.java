package Evaluate;

import GameLogic.Board;
import GameLogic.Move.Move;
import static GameLogic.Move.MoveGenerator.*;
import Piece.PieceColor;

import java.util.List;
import java.util.Random;


public class ChessBot {
    private PieceColor botColor;
    private Board board;

    public boolean done;

    public ChessBot(PieceColor botColor, Board board) {
        this.botColor = botColor;
        this.board = board;
        done = false;
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
    public void move()  {
        Move move = Evaluate.startSearch(3, board);
        System.out.println(move);
        board.makeMove(move);
    }

}
