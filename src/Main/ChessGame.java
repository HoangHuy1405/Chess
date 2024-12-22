package Main;

import GameStates.EState;
import GameStates.GameState;

import java.awt.*;

public class ChessGame implements Runnable {
    private BoardPanel boardPanel;
    private ChessWindow chessFrame;
    private Thread thread;
    private final int FPS = 144;
    private final int UPS_SET = 144;

    public ChessGame(){
        boardPanel = new BoardPanel(this);
        GameState.changeState(EState.MainScreen, boardPanel);

        chessFrame = new ChessWindow(boardPanel);
        boardPanel.requestFocus();

        startGameLoop();
    }

    private void startGameLoop(){
        thread = new Thread(this);
        thread.start();
    }


    public void update() {
        GameState.getState().update();
    }

    public void render(Graphics g){
        GameState.getState().render(g);
    }

    public void run() {
        double timePerFrame = 1000000000.0 / FPS;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {

            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                boardPanel.repaint();
                frames++;
                deltaF--;

            }

//            if (SHOW_FPS_UPS)
                if (System.currentTimeMillis() - lastCheck >= 1000) {

                    lastCheck = System.currentTimeMillis();
                    System.out.println("FPS: " + frames + " | UPS: " + updates);
                    frames = 0;
                    updates = 0;

                }

        }
    }
}
