package GUI;

import GUI.Frame.ChessWindow;
import GUI.Panel.BoardPanel;
import GameStates.GameState;
import GameStates.Playing;

import java.awt.*;

public class ChessGame implements Runnable {
    private BoardPanel boardPanel;
    private ChessWindow chessFrame;
    private Thread thread;
    private final int FPS = 144;
    private final int UPS_SET = 200;

    public ChessGame(){
        GameState.setGameState(new Playing(this));
        boardPanel = new BoardPanel(this);
        chessFrame = new ChessWindow(boardPanel);
        boardPanel.requestFocus();

        startGameLoop();
    }

    private void startGameLoop(){
        thread = new Thread(this);
        thread.start();
    }

    public void update() {
        GameState.gameState.update();
    }

    public void render(Graphics g){
        GameState.gameState.draw(g);
    }

    @Override
//    public void run() {
//        double timePerFrame = 1000000000.0 / FPS;
//        long lastFrame = System.nanoTime();
//        long now;
//        int frames = 0;
//        long lastCheck = System.currentTimeMillis();
//
//        while(true){
//            now = System.nanoTime();
//            if(now - lastFrame >= timePerFrame){
//                boardPanel.repaint();
//                lastFrame = now;
//                frames++;
//            }
//
//
//            if(System.currentTimeMillis() - lastCheck > 1000){
//                lastCheck = System.currentTimeMillis();
//                System.out.println("FPS: " + frames);
//                frames = 0;
//            }
//        }
//    }
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
