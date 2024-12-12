package Inputs;

import GameStates.GameState;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInput implements MouseListener, MouseMotionListener {
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        GameState.gameState.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GameState.gameState.mouseReleased(e);

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        GameState.gameState.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
