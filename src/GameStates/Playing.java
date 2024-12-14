package GameStates;

import Main.ChessGame;
import Logic.GameManager;
import Move.Move;
import Piece.Piece;
import Piece.PieceColor;
import Position.Position;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

import static GUI.Panel.BoardPanel.SQUARE_SIZE;

public class Playing extends GameState {
    private GameManager gameManager;

    private Piece curPiece;
    private List<Move> cacheMoves;

    private boolean isPressed;
    private boolean isDragged;

    private boolean isGameOver;

    public Playing(ChessGame chessGame) {
        super(chessGame);
        isPressed = false;
        isDragged = false;
        isGameOver = false;
        Initialize();
    }

    private void Initialize(){
        gameManager = new GameManager(PieceColor.white);
        gameManager.InitializeBoard();
    }


    private void setCurPiece(Piece curPiece) {
        this.curPiece = curPiece;

        if(this.curPiece == null)
            cacheMoves = null;
        else
            cacheMoves = gameManager.getLegalMoves(curPiece.getInitialBoardPos());
    }
    private void unpickPiece(){
        curPiece.resetAll();
        curPiece = null;
        cacheMoves = null;
    }
    private boolean executeMove(Position pos){
        if(cacheMoves == null || cacheMoves.isEmpty()){
            return false;
        }
        Move move = null;
        for(Move m : cacheMoves){
            if(m.getToPos().equals(pos)){
                move = m;
                break;
            }
        }
        if(move == null) return false;
        gameManager.MakeMove(move);
        if(gameManager.isGameOver()){
            isGameOver = true;
        }
        return true;
    }

    @Override
    public void update() {
        if(isGameOver) System.out.println(gameManager.getResult().getReason());
        else updatePieces();
    }
    @Override
    public void draw(Graphics g) {
        drawBoard(g);
        if(cacheMoves != null && !cacheMoves.isEmpty()){
            highlightLegalMove(g);
        }
        drawPieces(g);

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!isPressed) isPressed = true;
        int row = e.getY()/ SQUARE_SIZE;
        int col = e.getX()/ SQUARE_SIZE;

        Position pos = new Position(row,col);
        Piece piece = gameManager.getBoard().getPiece(pos);

        if(curPiece != null)
            if(!executeMove(pos)) setCurPiece(piece);
            else unpickPiece();
        else setCurPiece(piece);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        int row = e.getY()/ SQUARE_SIZE;
        int col = e.getX()/ SQUARE_SIZE;

        Position pos = new Position(row,col);

        if(isDragged){
            if(!executeMove(pos))
                if(isPressed) unpickPiece();
                else curPiece.resetAll();
            else unpickPiece();

            isPressed = false;
            isDragged = false;
        }
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        if(curPiece == null) return;

        if(!isDragged) isDragged = true;
        if(!curPiece.isDragging) curPiece.isDragging = true;
        int row = e.getY();
        int col = e.getX();

        Position pos = new Position(row,col);

        curPiece.setCurPos(pos);
    }

    private void highlightLegalMove(Graphics g){
        cacheMoves.forEach((move) -> {
            int x = move.getToPos().getCol() * 100;
            int y = move.getToPos().getRow() * 100;

            g.setColor(new Color(174, 171, 171, 219));
            g.fillOval(x + SQUARE_SIZE/4, y + SQUARE_SIZE/4, SQUARE_SIZE/2, SQUARE_SIZE/2);
        });
    }
    private void updatePieces(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Position pos = new Position(i, j);
                Piece piece = gameManager.getBoard().getPiece(pos);

                if(piece != null){
                    piece.update();
                }
            }
        }
    }
    private void drawBoard(Graphics g) {
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if ((i + j) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(new Color(55, 131, 0));
                }
                g.fillRect(j * SQUARE_SIZE, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
    private void drawPieces(Graphics g){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Position pos = new Position(i, j);
                Piece piece = gameManager.getBoard().getPiece(pos);

                if(piece != null){
                    piece.draw(g);
                }
            }
        }
    }
}
