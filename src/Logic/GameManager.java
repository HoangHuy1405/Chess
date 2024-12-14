package Logic;

import Evaluate.Evaluate;
import GameOver.EndReason;
import GameOver.Result;
import Move.Move;
import Piece.Piece;
import Piece.PieceColor;
import Position.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class GameManager {
    private PieceColor pieceColor_turn;
    private Board board;
    private Result result;



    private Stack<Board> stack;

    public GameManager(PieceColor pieceColor_turn) {
        this.pieceColor_turn = pieceColor_turn;
        this.board = new Board();
        this.result = null;

        stack = new Stack<>();

        Evaluate.gameManager = this;
    }
    public Board getBoard() {
        return board;
    }
    public PieceColor getPlayer_turn() {
        return pieceColor_turn;
    }
    public Result getResult() {
        return result;
    }

    public void InitializeBoard(){
        board.InitializeBoard();
    }

    public List<Move> getLegalMoves(Position pos) {
        Piece piece = board.getPiece(pos);
        if (piece == null || piece.getColor() != pieceColor_turn) return null;

        List<Move> moves = piece.getMoves(board, pos);
        List<Move> legalMoves = new ArrayList<>(moves);

        for (Move move : moves) {
            Board copy = board.copy();;
            move.execute(copy);
            if(copy.isInCheck(pieceColor_turn)){
                legalMoves.remove(move);
            }
        }
        return legalMoves;
    }
    // only all the legal moves of that color turn
    public List<Move> getAllLegalMoves() {
        List<Move> allLegalMoves = new ArrayList<>();
        List<Position> occupiedPos = pieceColor_turn.equals(PieceColor.white) ? board.getOccupiedPosWhiteList() : board.getOccupiedPosBlackList();

        for(Position pos : occupiedPos) {
            List<Move> legalMoves = getLegalMoves(pos);
            if(legalMoves != null)
            allLegalMoves.addAll(legalMoves);
        }
        return allLegalMoves;
    }
    public void MakeMove(Move move){
//        moveHistory.push(Fen.extractFen(board));
        board.lastDoublePawnMove = null;
        stack.push(board.copy());
        move.execute(board);
        pieceColor_turn = pieceColor_turn.Opponent();
        CheckForGameOver();
    }
    public void UnmakeMove() {
        this.board = stack.pop();
    }

    public boolean hasAnyLegalMovesFor(PieceColor pieceColor){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Position pos = new Position(i, j);
                Piece piece = board.getPiece(pos);

                if(piece == null) continue;
                if(!piece.getColor().equals(pieceColor)) continue;

                List<Move> legalMoves = getLegalMoves(pos);
                if(legalMoves != null && !legalMoves.isEmpty()) return true;

            }
        }
        return false;
    }

    public void CheckForGameOver(){
        if(!hasAnyLegalMovesFor(pieceColor_turn)){
            if(board.isInCheck(pieceColor_turn)) result = Result.Win(pieceColor_turn.Opponent());
            else result = Result.Draw(EndReason.Stalemate);
        }
    }

    public boolean isGameOver(){
        return result != null;
    }
}
