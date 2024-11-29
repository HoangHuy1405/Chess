package GUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {
    private int x, y;
    public boolean isClicked = false;

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        x = e.getX() / 100;
        y = e.getY() / 100;
        isClicked = true;
    }


}
