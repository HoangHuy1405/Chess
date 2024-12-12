package Logic;

import Logic.Move.Move;
import Logic.Piece.Piece;
import Logic.Piece.Player;
import Logic.Position.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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
}
