package GameStates;

import GUI.ChessGame;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class GameState {
    public static GameState gameState;

    public static void setGameState(GameState gameState) {
        GameState.gameState = gameState;
    }

    protected ChessGame chessGame;

    protected GameState(ChessGame chessGame) {
        this.chessGame = chessGame;
    }


    public abstract void update();
    public abstract void draw(Graphics g);

    public abstract void mousePressed(MouseEvent e);

    public abstract void mouseReleased(MouseEvent e);

    public abstract void mouseDragged(MouseEvent e);
}
