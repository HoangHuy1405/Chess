package GameStates;

import GameStates.TwoPlayer.TwoPlayer;
import Main.BoardPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public abstract class GameState implements MouseListener, MouseMotionListener {
    private static GameState state;
    protected BoardPanel boardPanel;

    protected GameState(BoardPanel boardPanel){
        this.boardPanel = boardPanel;
    }

    public static GameState getState(){
        return state;
    }

    public static void changeState(EState state, BoardPanel boardPanel){
        switch (state){
            case MainScreen -> GameState.state = new MainScreen(boardPanel);
            case TwoPlayer -> GameState.state = new TwoPlayer(boardPanel);
            case TwoBot -> GameState.state = new TwoBot(boardPanel);
        }
    }

    public void update(){}
    public abstract void render(Graphics g);

    @Override
    public void mousePressed(MouseEvent e){}
    @Override
    public void mouseReleased(MouseEvent e){}
    @Override
    public void mouseClicked(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e){}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
}
