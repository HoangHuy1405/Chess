package GUI.State;

import GUI.BoardPanel;
import Logic.Movement.Position;
import Logic.Piece.Piece;

public class NoPieceHoldingState extends BoardState {
    public NoPieceHoldingState(BoardPanel bp) {
        super(bp);
    }

    @Override
    public void pressEvent(Position p) {
//        Piece piece = bp.PickPiece(p);
        if(bp.PickPiece(p)){
            bp.setState(new PieceHoldingState(bp, p));
        }
    }

    @Override
    public void dragEvent(Position p) {

    }

    @Override
    public void releaseEvent(Position p) {

    }
}
