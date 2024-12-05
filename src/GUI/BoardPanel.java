package GUI;

import GUI.State.BoardState;
import GUI.State.NoPieceHoldingState;
import Logic.Board;
import Logic.Fen;
import Logic.GameManager;
import Logic.Movement.Move;
import Logic.Movement.Position;
import Logic.Piece.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class BoardPanel extends JPanel implements Runnable {
    private static final int HEIGHT = 800;
    private static final int WIDTH = 800;
    private static final int SQUARE_SIZE = 100;
    private final int FPS = 144;

    private Thread thread;
    private GameManager manager;
    private BoardState state;
    private Mouse mouse;

    public Map<Position, Move> cacheMove;

    private Position currentPiecePosition;
    private Piece pickedPiece;
    private Position draggingPosition;
//    private boolean isDragging;

    public BoardPanel(){
        Initialize();
        setPanelSize();
//        setLayout(new GridLayout(8, 8));
        mouse = new Mouse();
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
    }

    private void setPanelSize(){
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
    }

    public void launchGame(){
        thread = new Thread(this);
        thread.start();
    }
    private void Initialize(){
        cacheMove = new HashMap<>();
        state = new NoPieceHoldingState(this);
        manager = new GameManager(Player.white);
        manager.InitializeBoard();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS;
        long lastFrame = System.nanoTime();
        long now;
        int frames = 0;
        long lastCheck = System.currentTimeMillis();

        while(true){
            now = System.nanoTime();
            update();
            if(now - lastFrame >= timePerFrame){
                repaint();
                lastFrame = now;
                frames++;
            }

            if(System.currentTimeMillis() - lastCheck > 1000){
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }
    private void update(){
        if(mouse.isPressed){
//            System.out.println("isPress");
            Position pos = new Position(mouse.getRow() / 100, mouse.getCol() / 100);
//            System.out.println("Press at " + pos.getRow() + " " + pos.getCol());
            state.pressEvent(pos);
            mouse.isPressed = false;
//            return;
        }
        if(mouse.isDragging){
//            System.out.println("isDragging");
            Position pos = new Position(mouse.getRow(), mouse.getCol());
//            System.out.println("Dragging at " + pos.getRow() + " " + pos.getCol());
            state.dragEvent(pos);
            mouse.isDragging = false;
//            return;
        }
        if(mouse.isReleased){
//            System.out.println("isRelease");
            Position pos = new Position(mouse.getRow() / 100, mouse.getCol() / 100);
//            System.out.println("Released at " + pos.getRow() + " " + pos.getCol());
            state.releaseEvent(pos);
            mouse.isReleased = false;

        }


    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        DrawBoard(g2d);
        DrawPiece(g2d);

        if(cacheMove != null) {
            if (!cacheMove.isEmpty()) {
                HighlightMoves(g2d);
            }
        }

        if(pickedPiece != null && draggingPosition != null){
            DraggingPiece(g2d);
        }
    }

    public void setState(BoardState state){
        this.state = state;
    }
    public GameManager getGameManager(){
        return manager;
    }

    public boolean MovePiece(Position pos){
        if(cacheMove == null){
            return false;
        }

        if(!cacheMove.containsKey(pos)){
            return false;
        }

        Move move = cacheMove.get(pos);
        manager.MakeMove(move);

        UnpickPiece();

        return true;
    }
    public void UnpickPiece(){
//        System.out.println("UnpickPiece");
        pickedPiece = null;
        cacheMove = null;
        currentPiecePosition = null;
        resetPiece();
    }
    public boolean PickPiece(Position pos){
        pickedPiece = manager.getBoard().getPiece(pos);
        if(pickedPiece == null){
            return false;
        }
        currentPiecePosition = pos;
        cacheMove = manager.getLegalMoves(pos);

        return true;
    }
    public void setDraggingPiece(Position position){
//        draggingPiece = piece;
        draggingPosition = position;
    }
    public void resetPiece(){
//        draggingPiece = null;
        draggingPosition = null;
    }

    private void DrawBoard(Graphics2D g){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if ((i + j) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(new Color(193, 42, 42));
                }
                g.fillRect(j * SQUARE_SIZE, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
    private void DrawPiece(Graphics2D g){
        Board board = manager.getBoard();

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
//                System.out.println("currentPiecePosition " + currentPiecePosition + " draggingPosition " + draggingPosition);

                if (currentPiecePosition != null && draggingPosition != null) {
//                    System.out.println("Flag 1");
                    if(new Position(i, j).equals(currentPiecePosition)){
//                        System.out.println("Flag 2");
                        continue;
                    }
                }
                Piece piece = board.getPiece(new Position(i,j));
                if (piece != null){
                    BufferedImage pieceImg = PieceImage.getImage(piece);

                    g.drawImage(pieceImg, j * SQUARE_SIZE, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
                }
            }
        }
    }
    private void HighlightMoves(Graphics2D g){
        for(Map.Entry<Position, Move> entry : cacheMove.entrySet()){
            int row = entry.getKey().getRow();
            int col = entry.getKey().getCol();

            int x = col * 100;
            int y = row * 100;

            g.setColor(new Color(158, 140, 93, 232));
            g.fillOval(x, y, SQUARE_SIZE, SQUARE_SIZE);
        }
    }
    private void DraggingPiece(Graphics2D g){
        BufferedImage pieceImg = PieceImage.getImage(pickedPiece);
        g.drawImage(pieceImg, draggingPosition.getCol() - SQUARE_SIZE/2, draggingPosition.getRow() - SQUARE_SIZE/2, SQUARE_SIZE, SQUARE_SIZE, null);
    }

    private Position ConvertPos(Position position){
        return new Position(position.getCol() / 100, position.getRow() / 100);
    }
}
