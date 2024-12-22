package Overlayers;

import Buttons.Button;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public abstract class Overlays implements MouseListener, MouseMotionListener {
    protected int xPos, yPos;
    protected int width, height;

    protected BufferedImage image;

    protected Rectangle bounds;

    public Overlays(BufferedImage image, int xPos, int yPos, int width, int height) {
        this.image = image;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;

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

    @Override
    public void mouseClicked(MouseEvent e){}
    @Override
    public void mousePressed(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void mouseDragged(MouseEvent e) {

    }
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public static boolean isInBounds(MouseEvent e, Overlays b){
        return b.bounds.contains(e.getX(), e.getY());
    }
}
