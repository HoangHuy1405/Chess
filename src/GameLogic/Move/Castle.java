package GameLogic.Move;

import GameLogic.Board;
import Piece.PieceColor;

public class Castle extends Move{
    public static final int KS_CASTLE = 0;
    public static final int QS_CASTLE = 1;

    private Move rook;

    public Castle(PieceColor color, int type) {
        switch (color) {
            case White -> {
                fromPos = 60;
                toPos = (type == 0) ? 62 : 58;
                rook = (type == 0) ? new Move(63, 61) : new Move(56, 59);
            }
            case Black -> {
                fromPos = 4;
                toPos = (type == 0) ? 6 : 2;
                rook = (type == 0) ? new Move(7, 5) : new Move(0, 3);
            }
        }
    }

    @Override
    public void execute(Board board) {
        super.execute(board);
        rook.execute(board);
    }
}
