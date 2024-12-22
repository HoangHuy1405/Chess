package Buttons;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public abstract class Button implements MouseListener, MouseMotionListener {
    protected int xPos, yPos;
    protected int width, height;

    protected BufferedImage image;
    protected Rectangle bounds;

    public Button(BufferedImage image, int xPos, int yPos, int width, int height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.image = image;
        setBounds();
    }

    public void render(Graphics g){
        drawImg(g);
    }

    protected void setBounds(){
        bounds = new Rectangle(xPos, yPos, width, height);
    }
    protected void drawImg(Graphics g){
        g.drawImage(image, xPos, yPos, width, height, null);
    }

    public abstract void update();

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

    public static boolean isInBounds(MouseEvent e, Button b){
        return b.bounds.contains(e.getX(), e.getY());
    }
}
