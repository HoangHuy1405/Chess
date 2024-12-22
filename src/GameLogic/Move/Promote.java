package GameLogic.Move;

import GameLogic.Board;

public class Promote extends Move{
    private int piecePromoted;

    public Promote(int fromPos, int toPos, int piecePromoted) {
        super(fromPos, toPos);
        this.piecePromoted = piecePromoted;
    }

    public int getPiecePromoted() {
        return piecePromoted;
    }

    @Override
    public void execute(Board board){
        board.removePiece(fromPos);
        board.setPiece(piecePromoted, toPos);
    }
}
