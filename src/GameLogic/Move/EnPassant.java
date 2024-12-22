package GameLogic.Move;

import GameLogic.Board;
import Piece.PieceColor;
import Piece.Pieces;

import static Piece.PieceColor.White;

public class EnPassant extends Move{
    private final int lastDoublePawn;
    public EnPassant(int fromPos, int toPos, int lastDoublePawn){
        super(fromPos, toPos);
        this.lastDoublePawn = lastDoublePawn;
    }

    @Override
    public void execute(Board board) {
        int piece = board.getPiece(fromPos);
        board.removePiece(fromPos);
        board.setPiece(piece, toPos);
        board.removePiece(lastDoublePawn);


//        board.inCheck = false;
//
//
//
//
//        board.attackPattern.clear();
    }

}
