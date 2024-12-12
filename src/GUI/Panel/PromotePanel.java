//package GUI.Panel;
//
//import GUI.PieceImage;
//import Piece.*;
//import Position.Position;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.image.BufferedImage;
//import java.util.HashMap;
//import java.util.Map;
//
//import static javax.swing.UIManager.put;
//
//public class PromotePanel extends JPanel {
//    private Player color;
//    private Piece piece;
//
//    private int x;
//    private int y;
//
//    private static final Map<Piece, BufferedImage> whitePromotedPieces = new HashMap<>(){{
//            put(new Queen(Player.white),PieceImage.getImage(new Queen(Player.white)));
//            put(new Rook(Player.white), PieceImage.getImage(new Rook(Player.white)));
//            put(new Bishop(Player.white), PieceImage.getImage(new Bishop(Player.white)));
//            put(new Knight(Player.white), PieceImage.getImage(new Knight(Player.white)));
//    }};
//
//    private static final Map<Piece, BufferedImage> blackPromotedPieces = new HashMap<>(){{
//        put(new Queen(Player.black),PieceImage.getImage(new Queen(Player.black)));
//        put(new Rook(Player.black), PieceImage.getImage(new Rook(Player.black)));
//        put(new Bishop(Player.black), PieceImage.getImage(new Bishop(Player.black)));
//        put(new Knight(Player.black), PieceImage.getImage(new Knight(Player.black)));
//    }};
//
//    public PromotePanel(Position pos, Player color){
//        this.color = color;
//
//        x = pos.getCol() * BoardPanel.SQUARE_SIZE;
//        y = pos.getRow() * BoardPanel.SQUARE_SIZE;
//
//        InitializePanel();
//        addPieceButton();
//    }
//
//    public Piece getPiece(){
//        return piece;
//    }
//
//    private void InitializePanel(){
//        setBackground(Color.white);
//        addPieceButton();
//    }
//    private void addPieceButton(){
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        if(color == Player.white){
//            setBounds(x, y, BoardPanel.SQUARE_SIZE, BoardPanel.SQUARE_SIZE * whitePromotedPieces.size() + 50);
//
//            whitePromotedPieces.forEach((piece, image) -> {
//                add(createChoosePieceButton(piece, image));
//            });
//            add(createCancelButton());
//        }else {
//            setBounds(x, y - BoardPanel.SQUARE_SIZE * whitePromotedPieces.size() + 50, BoardPanel.SQUARE_SIZE, BoardPanel.SQUARE_SIZE * whitePromotedPieces.size() + 50);
//
//            add(createCancelButton());
//            blackPromotedPieces.forEach((piece, image) -> {
//                add(createChoosePieceButton(piece, image));
//            });
//        }
//    }
//
//    private JButton createChoosePieceButton(Piece piece, BufferedImage image){
//        Icon icon = new ImageIcon(image);
//        JButton button = new JButton(icon);
//        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, BoardPanel.SQUARE_SIZE));
//
//        button.addActionListener(e -> this.piece = piece);
//
//        return button;
//    }
//    private JButton createCancelButton(){
//        Icon xIcon = new ImageIcon("/x-icon.png");
//        JButton button = new JButton(xIcon);
//        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, BoardPanel.SQUARE_SIZE));
//        button.addActionListener(e -> {
//            Container parent = getParent();
//            if (parent != null) {
//                parent.remove(this);
//                parent.revalidate();
//                parent.repaint();
//            }
//        });
//
//        return button;
//    }
//}
