package Overlayers;

import Buttons.Button;
import Buttons.PromoteB;
import GameLogic.Move.Move;
import GameLogic.Move.Promote;
import GameStates.TwoPlayer.TPState;
import GameStates.TwoPlayer.TwoPlayer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

import static StaticField.OverlayProperties.PROMOTE_LAYOUT;
import static StaticField.OverlayProperties.getLayoutImg;
import static StaticField.ProjectileProperties.*;

public class PromoteOverlay extends Overlays{
    private TwoPlayer twoPlayer;

    private int color;

    private PromoteB queenPB, rookPB, knightPB, bishopPB;

    public PromoteOverlay(int xPos, int yPos, TwoPlayer twoPlayer, List<Move> promoteMoves) {
        super(getLayoutImg(PROMOTE_LAYOUT), xPos, yPos, 550, 200);
        this.twoPlayer = twoPlayer;

        initializeButton(promoteMoves);
    }

    private void initializeButton(List<Move> promoteMoves){
        queenPB = new PromoteB(xPos + 50, yPos + 50, (Promote) promoteMoves.get(0), twoPlayer.getBoard());
        rookPB = new PromoteB(xPos + Square.SIZE + 50, yPos + 50, (Promote) promoteMoves.get(1), twoPlayer.getBoard());
        knightPB = new PromoteB(xPos + Square.SIZE * 2 + 50, yPos + 50, (Promote) promoteMoves.get(2), twoPlayer.getBoard());
        bishopPB = new PromoteB(xPos + Square.SIZE * 3 + 50, yPos + 50, (Promote) promoteMoves.get(3), twoPlayer.getBoard());
    }



    @Override
    public void render(Graphics g) {
        super.render(g);
        queenPB.render(g);
        rookPB.render(g);
        knightPB.render(g);
        bishopPB.render(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(Button.isInBounds(e, queenPB)){
            queenPB.mouseClicked(e);
            twoPlayer.changeTPState(TPState.None);
        }
        if(Button.isInBounds(e, rookPB)){
            rookPB.mouseClicked(e);
            twoPlayer.changeTPState(TPState.None);
        }
        if(Button.isInBounds(e, knightPB)){
            knightPB.mouseClicked(e);
            twoPlayer.changeTPState(TPState.None);
        }
        if(Button.isInBounds(e, bishopPB)){
            bishopPB.mouseClicked(e);
            twoPlayer.changeTPState(TPState.None);
        }
    }

}
