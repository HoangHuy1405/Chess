package GameStates;

import Main.BoardPanel;

import java.awt.*;

public abstract class PlayingState extends GameState{
    protected PlayingState(BoardPanel boardPanel) {
        super(boardPanel);
    }



    @Override
    public void render(Graphics g) {

    }
}
