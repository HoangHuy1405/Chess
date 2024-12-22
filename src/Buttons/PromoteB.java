package Buttons;

import GameLogic.Board;
import GameLogic.Move.Move;
import GameLogic.Move.Promote;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static StaticField.ButtonProperties.*;
import static StaticField.ButtonProperties.Piece.getPieceImage;
import static StaticField.ProjectileProperties.*;

public class PromoteB extends Button{
    private Promote move;
    private Board board;

    public PromoteB(int xPos, int yPos, Promote move, Board board) {
        super(null, xPos, yPos, Square.SIZE, Square.SIZE);
        this.move = move;
        this.board = board;
        initialize();
    }

    private void initialize() {
        image = getPieceImage(move.getPiecePromoted());
    }


    @Override
    public void update() {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        board.makeMove(move);
    }
}
