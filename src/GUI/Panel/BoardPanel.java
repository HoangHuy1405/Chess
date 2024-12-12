package GUI.Panel;

import GUI.ChessGame;
import Inputs.MouseInput;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private static final int HEIGHT = 800;
    private static final int WIDTH = 800;
    public static final int SQUARE_SIZE = 100;

    private ChessGame game;
    private MouseInput mouseInput;

    public BoardPanel(ChessGame chessGame){
        this.game = chessGame;
        mouseInput = new MouseInput();

        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
        InitializePanel();

    }

    private void InitializePanel(){
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

}
