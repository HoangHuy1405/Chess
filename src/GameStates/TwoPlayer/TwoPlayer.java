package GameStates.TwoPlayer;

import Buttons.PieceB;

import Evaluate.ChessBot;
import GameLogic.Move.Move;
import GameStates.GameState;
import Main.BoardPanel;
import GameLogic.*;
import Piece.PieceColor;

import static GameStates.TwoPlayer.TPState.*;
import static StaticField.ProjectileProperties.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

public class TwoPlayer extends GameState {
    public Board board;
    private PieceB[] pieceBS;

    private ChessBot chessBot;

    public boolean isBotTurn;

    private HashMap<Integer, List<Move>> cacheMoves;
    public boolean drawHighlight;

    public TPState state;

    private boolean updatePieces;

    private int pressedIndex;

    public TwoPlayer(BoardPanel boardPanel) {
        super(boardPanel);
        board = new Board();
        chessBot = new ChessBot(PieceColor.Black);

        isBotTurn = false;

        pieceBS = new PieceB[64];
        cacheMoves = new HashMap<>();

        state = None;

        resetBool();
        initialize();
    }

    public void clearCacheMoves(){
        cacheMoves.clear();
    }
    public void setCacheMoves(HashMap<Integer, List<Move>> cacheMoves){
        this.cacheMoves = cacheMoves;
    }
    public Move getMove(int index){
        List<Move> moves = cacheMoves.get(index);
        if(moves == null){
            return null;
        }else{
            return moves.get(0);
        }
    }
    public Move getMove(int index, int n){
        List<Move> moves = cacheMoves.get(index);
        if(moves == null){
            return null;
        }else{
            return moves.get(n);
        }
    }

    private void resetBool(){
        updatePieces = false;
        drawHighlight = false;
    }

    private void initialize(){
        board.initializeDefault();

        initializePieceB();


    }
    private void initializePieceB(){
        for(int i = 0; i < 64; i++){
            pieceBS[i] = new PieceB(i%8 * Square.SIZE, i/8 * Square.SIZE, this);
        }
        updatePieces = true;
    }

    public Board getBoard(){
        return board;
    }

    public void changeTPState(TPState state){
        switch(state){
            case None -> {
                this.state = None;
                resetBool();
                clearCacheMoves();
                updatePieces = true;
            }
            case Hold_Piece -> {
                this.state = Hold_Piece;
                drawHighlight = true;
            }
            case Promote -> {
                this.state = Promote;
            }
        }
    }

    @Override
    public void update() {
        if(updatePieces){
            updatePiece();
            updatePieces = false;
        }
        if(isBotTurn){
            synchronized (chessBot){
                if(isBotTurn) {
                    updatePieces = true;
                    chessBot.move(board);
                    isBotTurn = false;
                    updatePieces = true;
                }
            }
        }


    }
    private void updatePiece(){
        for(int i = 0; i < 64; i++){
            pieceBS[i].update();
        }
    }

    @Override
    public void render(Graphics g) {
        drawBoard(g);
        drawPiece(g);
        if(drawHighlight) highlightMove(g);
    }
    public void drawBoard(Graphics g) {
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if ((i + j) % 2 == 0) {
                    g.setColor(Square.WHITE);
                } else {
                    g.setColor(Square.BLACK);
                }
                g.fillRect(j * Square.SIZE, i * Square.SIZE, Square.SIZE, Square.SIZE);
            }
        }
    }
    public void drawPiece(Graphics g){
        for(int i = 0; i < 64; i++){
            pieceBS[i].render(g);
        }
    }
    public void highlightMove(Graphics g){
        g.setColor(new Color(174, 171, 171, 219));
        for(Integer i : cacheMoves.keySet()){
            g.fillOval(i%8 * Square.SIZE, i/8 * Square.SIZE, Square.SIZE, Square.SIZE);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        int index = (e.getY() / Square.SIZE) * 8 + (e.getX() / Square.SIZE);
//        PieceB pb = pieceBS[index];
//
//        if(isInBounds(e, pb)){
//            pb.mouseEntered(e);
//        }else{
//            pb.mouseExited(e);
//        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        pressedIndex = (e.getY() / Square.SIZE) * 8 + (e.getX() / Square.SIZE);
        System.out.println("Press at " + pressedIndex);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Click at " + pressedIndex);
        pieceBS[pressedIndex].mouseClicked(e);
    }
    @Override
    public void mouseDragged(MouseEvent e){

        pieceBS[pressedIndex].mouseDragged(e);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        pieceBS[pressedIndex].mouseReleased(e);
    }
}
