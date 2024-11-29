package GUI;

import Logic.Board;
import Logic.GameState;
import Logic.Movement.Move;
import Logic.Movement.Position;
import Logic.Piece.Piece;
import Logic.Piece.PieceColor;
import Logic.Piece.Rook;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Dictionary;

public class BoardPanel extends JPanel implements Runnable {
    private static final int HEIGHT = 800;
    private static final int WIDTH = 800;
    private static final int SQUARE_SIZE = 100;
    private final int FPS = 60;

    private Thread thread;
    private GameState gameState;

    private Mouse mouse;

    private Dictionary<Position, Move> cacheMove;

    public BoardPanel(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new GridLayout(8, 8));
        mouse = new Mouse();
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
    }

    public void launchGame(){
        Initialize();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        double lastTime = System.nanoTime();
        double currentTime;

        while(thread != null){
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1){
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update(){
        if(mouse.isClicked){
            int x = mouse.getX();
            int y = mouse.getY();

            Board board = gameState.getBoard();
            if(board.getPiece(new Position(x, y)) != null){
                Piece piece = board.getPiece(new Position(x, y));

            }
            System.out.println(mouse.getX());
            System.out.println(mouse.getY());
            mouse.isClicked = false;
        }
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        DrawBoard(g2d);
        DrawPiece(g2d);
    }
    private void Initialize(){
        gameState = new GameState(PieceColor.white);
        gameState.getBoard().setPiece(new Rook(PieceColor.black), new Position(0,0));
    }

    private void DrawBoard(Graphics2D g){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if ((i + j) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(j * SQUARE_SIZE, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
    private void DrawPiece(Graphics2D g){
        Board board = gameState.getBoard();

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Piece piece = board.getPiece(new Position(i,j));
                if (piece != null){
                 BufferedImage pieceImg = PieceImage.getImage(piece);
                 g.drawImage(pieceImg, j * SQUARE_SIZE, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
                }
            }
        }
    }


//    private void DrawChessBoard(){
//        for(int i = 0; i < 8; i++){
//            for(int j = 0; j < 8; j++){
//                JPanel cell = new JPanel();
//                cell.setLayout(new BorderLayout());
//                if ((i + j) % 2 == 0) {
//                    cell.setBackground(Color.WHITE);
//                } else {
//                    cell.setBackground(Color.BLACK);
//                }
//                add(cell);
//            }
//        }
//    }
//    private void DrawPieces(){
//        Board board = gameState.getBoard();
//        Component[] cells = getComponents();
//
//        for(int i = 0; i < 8; i++){
//            for(int j = 0; j < 8; j++){
//                Piece piece = board.getPiece(new Position(i,j));
//
//                if(piece != null){
//                    JPanel cell = (JPanel) cells[i*8 + j];
//                    cell.removeAll();
//
//                    ImageIcon scaledIcon = PieceImage.getScaledImageIcon(piece);
//                    JLabel pieceLabel = new JLabel(scaledIcon);
//                    pieceLabel.setHorizontalAlignment(JLabel.CENTER);
//                    pieceLabel.setVerticalAlignment(JLabel.CENTER);
//
//                    cell.add(pieceLabel, BorderLayout.CENTER);
//                }
//            }
//        }
//    }
}
