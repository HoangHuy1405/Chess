package GUI.State;

import GUI.BoardPanel;
import Logic.Position.Position;

public class PieceHoldingState extends BoardState {
    private boolean isPress, isDrag;
    private Position currentPosition;
    private boolean holdPiece;

    public PieceHoldingState(BoardPanel bp, Position currentPosition) {
        super(bp);
        isPress = false;
        isDrag = false;
        this.currentPosition = currentPosition;
        holdPiece = true;
    }

    @Override
    public void pressEvent(Position p) {
        isPress = true;
        if(!p.equals(currentPosition)) {
            if(bp.MovePiece(p)){
                bp.MovePiece(p);
                bp.setState(new NoPieceHoldingState(bp));
            }else{
                holdPiece = bp.PickPiece(p);
                if (holdPiece) {
                    this.currentPosition = p;
                    isDrag = false;
                    isPress = false;
                }else{
                    bp.setState(new NoPieceHoldingState(bp));
                }
            }
        }

    }

    @Override
    public void dragEvent(Position p) {
//        System.out.println("Dragging");
        isDrag = true;
        if(holdPiece) {
            bp.setDraggingPiece(p);
        }

    }

    @Override
    public void releaseEvent(Position releasePos) {
        if(isDrag && !isPress) {
            if(bp.MovePiece(releasePos)){
                bp.UnpickPiece();
                bp.setState(new NoPieceHoldingState(bp));
            }else{
                bp.resetPiece();
            }
            return;
        }

        if(isPress && !isDrag) {
            bp.UnpickPiece();
            bp.setState(new NoPieceHoldingState(bp));
        }

        if(isPress && isDrag) {
            if(currentPosition.equals(releasePos)){
                bp.UnpickPiece();
                bp.setState(new NoPieceHoldingState(bp));
            }else{
                if(bp.MovePiece(releasePos)){
                    bp.UnpickPiece();
                    bp.setState(new NoPieceHoldingState(bp));
                }else{
                    bp.resetPiece();
                }
            }
        }
    }
}
