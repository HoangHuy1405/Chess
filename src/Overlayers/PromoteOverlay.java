package Overlayers;

import Buttons.PromoteB;
import GameStates.TwoPlayer.TwoPlayer;
import Piece.PieceColor;

import java.awt.*;

import static StaticField.ProjectileProperties.*;

public class PromoteOverlay extends Overlays{
    private TwoPlayer twoPlayer;

    private int color;

    private PromoteB queenPB, rookPB, knightPB, bishopPB;

    public PromoteOverlay(int xPos, int yPos, TwoPlayer twoPlayer, PieceColor color) {
        super(xPos, yPos, Square.SIZE, Square.SIZE);
        this.color = color.value;
        this.twoPlayer = twoPlayer;
    }

    private void initialize(){
//        queenPB
    }

    @Override
    public void render(Graphics g) {

    }


}
