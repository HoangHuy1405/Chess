package Buttons;

import GameStates.EState;
import GameStates.GameState;
import Main.BoardPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class StateChanger extends Button{
    private BufferedImage hoverImg;

    private EState state;
    private BoardPanel boardPanel;

    private boolean isHovered;
    private boolean drawHover;

    public StateChanger(BufferedImage image, int xPos, int yPos, BoardPanel boardPanel, EState state) {
        super(image, xPos, yPos, image.getWidth(null), image.getHeight(null));

        this.state = state;
        this.boardPanel = boardPanel;
    }

    private void resetBool(){
        isHovered = false;
        drawHover = false;
    }

    public void setHoverImg(BufferedImage hoverImg) {
        this.hoverImg = hoverImg;
    }

    private void drawHover(Graphics g) {
        g.drawImage(hoverImg, xPos, yPos, width, height, null);
    }

    @Override
    public void update() {
        if(isHovered){
            drawHover = true;
        }
    }
    @Override
    protected void drawImg(Graphics g) {
        if(drawHover) drawHover(g);
        else super.drawImg(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        changeState();
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        if(hoverImg != null && !isHovered) {
            System.out.println("Hover");
            isHovered = true;
        }
    }
    @Override
    public void mouseExited(MouseEvent e) {
        if(isHovered) {
            System.out.println("UnHover");
            isHovered = false;
        }
    }


    private void changeState() {
        GameState.changeState(state, boardPanel);
    }
}
