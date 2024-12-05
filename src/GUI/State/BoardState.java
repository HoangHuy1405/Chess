package GUI.State;

import GUI.BoardPanel;
import Logic.Board;
import Logic.Movement.Position;

import java.awt.*;

public abstract class BoardState {
    protected BoardPanel bp;
    public BoardState(BoardPanel bp) {
        this.bp = bp;
    }

    public abstract void pressEvent(Position p);
    public abstract void dragEvent(Position p);
    public abstract void releaseEvent(Position p);
}
