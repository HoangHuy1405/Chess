package Main;

import javax.swing.*;

public class ChessWindow{
    private JFrame frame;

    public ChessWindow(BoardPanel boardPanel) {
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(boardPanel);
        frame.pack(); // sized to fit the preferred size

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
