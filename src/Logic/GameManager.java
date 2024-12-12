package Logic;

import GameOver.EndReason;
import GameOver.Result;
import GameStates.GameState;
import GameStates.Playing;
import Move.Move;
import Piece.Piece;
import Piece.Player;
import Position.Position;

import java.util.ArrayList;
import java.util.List;


public class GameManager {
    private Player player_turn;
    private Board board;

    private Result result;

    public GameManager(Player player_turn) {
        this.player_turn = player_turn;
        this.board = new Board();
        this.result = null;
    }
    public Board getBoard() {
        return board;
    }
    public Player getPlayer_turn() {
        return player_turn;
    }
    public Result getResult() {
        return result;
    }

    public void InitializeBoard(){
        board.InitializeBoard();
    }

    public List<Move> getLegalMoves(Position pos) {
        Piece piece = board.getPiece(pos);
        if (piece == null || piece.getColor() != player_turn) return null;

        List<Move> moves = piece.getMoves(board, pos);
        List<Move> legalMoves = new ArrayList<>(moves);

        for (Move move : moves) {
            Board copy = board.copy();;
            move.execute(copy);
            if(copy.isInCheck(player_turn)){
                legalMoves.remove(move);
            }
        }
        return legalMoves;
    }
    public void MakeMove(Move move){
//        moveHistory.push(Fen.extractFen(board));
        board.lastDoublePawnMove = null;
        move.execute(board);
        player_turn = player_turn.Opponent();
        CheckForGameOver();

    }

    public boolean hasAnyLegalMovesFor(Player player){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Position pos = new Position(i, j);
                Piece piece = board.getPiece(pos);

                if(piece == null) continue;
                if(!piece.getColor().equals(player)) continue;

                List<Move> legalMoves = getLegalMoves(pos);
                if(legalMoves != null && !legalMoves.isEmpty()) return true;

            }
        }
        return false;
    }

    public void CheckForGameOver(){
        if(!hasAnyLegalMovesFor(player_turn)){
            if(board.isInCheck(player_turn)) result = Result.Win(player_turn.Opponent());
            else result = Result.Draw(EndReason.Stalemate);
        }
    }

    public boolean isGameOver(){
        return result != null;
    }
}
