package GameStates;

import GUI.ChessGame;
import Logic.GameManager;
import Logic.Move.Move;
import Logic.Piece.Piece;
import Logic.Piece.Player;
import Logic.Position.Position;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Map;

import static GUI.Panel.BoardPanel.SQUARE_SIZE;

public class Playing extends GameState {
    private GameManager manager;

    private Piece curPiece;
    private Map<Position, Move> cacheMoves;

    private boolean isPressed;
    private boolean isDragged;

    public Playing(ChessGame chessGame) {
        super(chessGame);
        isPressed = false;
        isDragged = false;
        Initialize();
    }

    private void Initialize(){
        manager = new GameManager(Player.white);
        manager.InitializeBoard();
    }

    private void setCurPiece(Piece curPiece) {
        this.curPiece = curPiece;

        if(this.curPiece == null)
            cacheMoves = null;
        else
            cacheMoves = manager.getLegalMoves(curPiece.getInitialBoardPos());
    }
    private void unpickPiece(){
        curPiece.resetAll();
        curPiece = null;
        cacheMoves = null;
    }
    private boolean executeMove(Position pos){
        if(cacheMoves == null || cacheMoves.isEmpty() || !cacheMoves.containsKey(pos)){
            return false;
        }

        Move move = cacheMoves.get(pos);
        manager.MakeMove(move);
        curPiece.setInitialBoardPos(pos);
        return true;
    }

    @Override
    public void update() {
        manager.update();
    }

    @Override
    public void draw(Graphics g) {
        manager.render(g);
        if(cacheMoves != null && !cacheMoves.isEmpty()){
            HighlightLegalMove(g);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!isPressed) isPressed = true;
        int row = e.getY()/ SQUARE_SIZE;
        int col = e.getX()/ SQUARE_SIZE;

        Position pos = new Position(row,col);
        Piece piece = manager.getBoard().getPiece(pos);

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
            System.out.println("Released");
            if(!executeMove(pos)){
                if(isPressed) {
                    unpickPiece();
                }
                else curPiece.resetAll();
            }else{
                unpickPiece();
            }
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

    private void HighlightLegalMove(Graphics g){
        cacheMoves.forEach((pos, move) -> {
            int x = pos.getCol() * 100;
            int y = pos.getRow() * 100;

            g.setColor(new Color(158, 140, 93, 232));
            g.fillOval(x, y, SQUARE_SIZE, SQUARE_SIZE);
        });
    }
}
