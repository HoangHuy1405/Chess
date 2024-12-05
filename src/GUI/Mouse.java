package GUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {
    private int row, col;
    public boolean isDragging = false;
    public boolean isPressed = false;
    public boolean isReleased = false;

    @Override
    public void mousePressed(MouseEvent e) {
        col = e.getX();
        row = e.getY();
        isPressed = true;
        isDragging = false;
        isReleased = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        col = e.getX();
        row = e.getY();
        isDragging = true;
        isReleased = false;
        isPressed = false;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        col = e.getX();
        row = e.getY();
        isReleased = true;
        isDragging = false;
        isPressed = false;
    }

    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
}
