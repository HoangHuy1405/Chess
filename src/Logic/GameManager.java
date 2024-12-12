package Logic;

import Logic.Move.Move;
import Logic.Piece.Piece;
import Logic.Piece.Player;
import Logic.Position.Position;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static GUI.Panel.BoardPanel.SQUARE_SIZE;

public class GameManager {
    private Player player_turn;
    private Board board;

    public GameManager(Player player_turn) {
        this.player_turn = player_turn;
        this.board = new Board();
    }
    public Board getBoard() {
        return board;
    }
    public Player getPlayer_turn() {
        return player_turn;
    }

    public void InitializeBoard(){
        board.InitializeBoard();
    }

    public Map<Position, Move> getLegalMoves(Position pos) {
        Piece piece = board.getPiece(pos);
        if (piece == null || piece.getColor() != player_turn) return null;

        Map<Position, Move> moves = piece.getMoves(board, pos);
        Map<Position, Move> legalMoves = new HashMap<>(moves);


        for (Map.Entry<Position, Move> entry : moves.entrySet()) {
            Board copy = board.copy();
            entry.getValue().execute(copy);
            if(copy.isInCheck(player_turn)){
                legalMoves.remove(entry.getKey());
            }
        }
        return legalMoves;
    }
    public void MakeMove(Move move){
//        moveHistory.push(Fen.extractFen(board));
        board.lastDoublePawnMove = null;
        move.execute(board);
        player_turn = player_turn.Opponent();
    }

    public void update(){
        updatePieces();
    }

    public void render(Graphics g){
        drawBoard(g);
        drawPieces(g);
    }

    private void drawBoard(Graphics g) {
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
    private void drawPieces(Graphics g){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Position pos = new Position(i, j);
                Piece piece = board.getPiece(pos);

                if(piece != null){
                    piece.draw(g);
                }
            }
        }
    }

    private void updatePieces(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Position pos = new Position(i, j);
                Piece piece = board.getPiece(pos);

                if(piece != null){
                    piece.update();
                }
            }
        }
    }
}
