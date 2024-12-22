package Buttons;

import GameLogic.Move.Move;
import GameLogic.Move.MoveGenerator;
import GameLogic.Move.Promote;
import GameStates.TwoPlayer.TPState;
import GameStates.TwoPlayer.TwoPlayer;
import Overlayers.PromoteOverlay;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import static GameStates.TwoPlayer.TwoPlayer.*;
import static StaticField.ProjectileProperties.*;
import static StaticField.ButtonProperties.Piece.*;

public class PieceB extends Button{
    private TwoPlayer twoPlayer;

    private int xDrag;
    private int yDrag;

    private boolean isDragging;

    public PieceB(int xPos, int yPos, TwoPlayer twoPlayer) {
        super(null, xPos, yPos, Square.SIZE, Square.SIZE);
        this.twoPlayer = twoPlayer;
    }

    private void resetPos(){
        isDragging = false;
        xDrag = xPos;
        yDrag = yPos;
    }

    @Override
    public void update() {
        updateImage();
    }
    private void updateImage(){
        int index = (yPos / height) * 8 + (xPos / height);
        if(twoPlayer.getBoard().getPiece(index) == 0) this.image = null;
        else image = getPieceImage(twoPlayer.getBoard().getPiece(index));
    }

    @Override
    public void drawImg(Graphics g) {
        if(this.image == null) return;
        if(isDragging) g.drawImage(image, xDrag - Square.SIZE/2, yDrag - Square.SIZE/2, Square.SIZE, Square.SIZE, null);
        else super.drawImg(g);
    }

    @Override
    public void mouseDragged(MouseEvent e){
        if(image == null) return;
        if(!isDragging) isDragging = true;
        xDrag = e.getX();
        yDrag = e.getY();
//        System.out.println("x: " + xDrag + ", y: " + yDrag);
    }
    @Override
    public void mouseReleased(MouseEvent e){
        resetPos();
    }
    @Override
    public void mouseClicked(MouseEvent e){
        int index = (yPos / height) * 8 + (xPos / height);
        System.out.println("Inner clicked: " + index);
        switch(twoPlayer.state){
            case None -> {
                HashMap<Integer, List<Move>> moves = MoveGenerator.generateLegalMoves(index, twoPlayer.getBoard());
                if(moves.isEmpty()) return;

                twoPlayer.setCacheMoves(moves);
                twoPlayer.changeTPState(TPState.Hold_Piece);
            }
            case Hold_Piece -> {
                Move move = twoPlayer.getMove(index);

                if(move instanceof Promote) {
                    List<Move> promoteMove = twoPlayer.getMoves(index);
                    twoPlayer.startPromote(promoteMove);
                    twoPlayer.changeTPState(TPState.Promote);
                    return;
                }

                if(move != null) {
                    twoPlayer.getBoard().makeMove(move);
                    twoPlayer.updatePieces = true;
                    twoPlayer.isBotTurn = true;
                }


                twoPlayer.changeTPState(TPState.None);
            }
            case Promote -> {
                twoPlayer.promoteOverlay.mouseClicked(e);
            }
        }
    }
}
