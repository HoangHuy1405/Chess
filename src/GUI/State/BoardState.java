package GUI.State;

import GUI.BoardPanel;
import Logic.Position.Position;

public abstract class BoardState {
    protected BoardPanel bp;
    public BoardState(BoardPanel bp) {
        this.bp = bp;
    }

    public abstract void pressEvent(Position p);
    public abstract void dragEvent(Position p);
    public abstract void releaseEvent(Position p);
}
