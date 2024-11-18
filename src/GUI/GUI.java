package GUI;

import javax.swing.*;
import java.awt.*;

public class GUI {
    private static final int TILE_SIZE = 75;

    public void createBoard() throws Exception {
// Create the main frame
        JFrame frame = new JFrame("8x8 Grid Layout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);  // Adjust size as needed
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(new GridLayout(8, 8, 0, 0));  // 8 rows, 8 columns, with spacing

        MouseEventHandler mouseEventHandler = new MouseEventHandler();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JPanel panel = new JPanel(new GridBagLayout());
                JLabel piece = initilizeBoard(row, col);

                if(piece != null) {
                    panel.add(piece);
                }
                if(row == 0 && col == 0) {

                }



                // Check if the sum of row and column is even or odd to set color
                if ((row + col) % 2 == 0) {
                    panel.setBackground(Color.LIGHT_GRAY);
                } else {
                    panel.setBackground(Color.DARK_GRAY);
                }
                panel.addMouseListener(mouseEventHandler);
                // Set row and column properties to identify each square
                panel.putClientProperty("row", row);
                panel.putClientProperty("col", col);
                panel.setPreferredSize(new Dimension(60, 60));

                frame.add(panel);
            }
        }

        // Display the frame
        frame.setVisible(true);
    }
    private ImageIcon getScaledIcon(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage(); // Get the original image
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); // Scale image
        return new ImageIcon(scaledImg); // Return scaled image as an icon
    }
    private JLabel initilizeBoard(int row, int col) throws Exception {
        Piece piece = null;
        PieceColor pieceColor = null;
        if(row == 0 || row == 1) {
            pieceColor = PieceColor.black;
        }
        if(row == 7 || row == 6) {
            pieceColor = PieceColor.white;
        }
        switch(col) {
            case 0:
            case 7:
                piece = Piece.rook;
                break;
            case 1:
            case 6:
                piece = Piece.knight;
                break;
            case 2:
            case 5:
                piece = Piece.bishop;
                break;
            case 3:
                piece = Piece.queen;
                break;
            case 4:
                piece = Piece.king;
                break;
            default:
                throw new Exception("piece not found 404");
        }
        if(row == 1 || row == 6) {
            piece = Piece.pawn;
        }
        return placePiece(row, col, pieceColor, piece);
    }
    private JLabel placePiece(int row, int col,PieceColor pieceColor, Piece piece) {
        if(pieceColor == null || piece == null) {
            return null;
        }
        String imageStr = "src/GUI/images/"+pieceColor+"-"+piece+".png";
        JLabel pieceLabel = new JLabel(getScaledIcon(imageStr, TILE_SIZE, TILE_SIZE));
        pieceLabel.setName(pieceColor+"-"+piece);
        pieceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pieceLabel.setVerticalAlignment(SwingConstants.CENTER);

        return pieceLabel;
    }
}
