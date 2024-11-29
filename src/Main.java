import GUI.BoardPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        BoardPanel bp = new BoardPanel();
        frame.add(bp);
        frame.pack(); // sized to fit the preferred size

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        bp.launchGame();
    }
}