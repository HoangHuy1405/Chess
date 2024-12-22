package Buttons;

import GameLogic.Move.Move;
import Piece.PieceColor;

import Piece.PieceType;

import java.awt.image.BufferedImage;

import static StaticField.ButtonProperties.*;
import static StaticField.ProjectileProperties.*;

public class PromoteB extends Button{
    private Move move;

    public PromoteB(int xPos, int yPos, Move move, BufferedImage img) {
        super(img, xPos, yPos, Square.SIZE, Square.SIZE);
    }

    @Override
    public void update() {

    }
}
