package Inputs;

import GameStates.GameState;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInput implements MouseListener, MouseMotionListener {
    @Override
    public void mousePressed(MouseEvent e) {
        GameState.getState().mousePressed(e);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        GameState.getState().mouseReleased(e);
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        GameState.getState().mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        GameState.getState().mouseClicked(e);
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        GameState.getState().mouseEntered(e);
    }
    @Override
    public void mouseExited(MouseEvent e) {
        GameState.getState().mouseExited(e);
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        GameState.getState().mouseMoved(e);
    }
}
