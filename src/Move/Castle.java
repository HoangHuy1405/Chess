package Move;

import Logic.Board;
import Position.Position;

public class Castle extends Move{
    private final Position rookFromPos;
    private final Position rookToPos;

    public Castle(Position fromPos, Position toPos, MoveType type, Position rookPosition) {
        super(fromPos, toPos, type);
        this.rookFromPos = rookPosition;
        if(type == MoveType.KSCastle) {
            rookToPos = new Position(rookPosition.getRow(), 5);
        }else{
            rookToPos = new Position(rookPosition.getRow(), 3);
        }
    }

    @Override
    public void execute(Board board) {
        new NormalMove(fromPos, toPos).execute(board);
        new NormalMove(rookFromPos, rookToPos).execute(board);
    }
}
