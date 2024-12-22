package GameStates;

import Buttons.StateChanger;
import Main.BoardPanel;

import static StaticField.ButtonProperties.*;
import static Buttons.Button.*;


import java.awt.*;
import java.awt.event.MouseEvent;


public class MainScreen extends GameState{
    private StateChanger stateChanger;

    public MainScreen(BoardPanel boardPanel){
        super(boardPanel);
        initializeButton();

    }

    public void initializeButton(){
        stateChanger = new StateChanger(getButtonImg(Play.DEFAULT), 800/2 - 100/2, 800/2, boardPanel, EState.TwoPlayer);
        stateChanger.setHoverImg(getButtonImg(Play.HOVER));
    }

    @Override
    public void render(Graphics g) {
        stateChanger.render(g);
    }
    @Override
    public void update() {
        stateChanger.update();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(isInBounds(e, stateChanger)){
            stateChanger.mouseEntered(e);
        }else{
            stateChanger.mouseExited(e);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(isInBounds(e, stateChanger)){
            stateChanger.mouseClicked(e);
        }
    }
}
