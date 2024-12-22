package GameLogic;

import GameLogic.Move.Move;
import Piece.PieceColor;
import Piece.PieceTracker;
import Piece.PieceType;
import Piece.Pieces;

import java.util.List;
import java.util.Stack;

import static GameLogic.EndReason.*;
import static GameLogic.Move.MoveGenerator.*;
import static GameLogic.Result.*;
import static Piece.PieceColor.*;
import static Piece.Pieces.*;

public class Board {
    private int[] pieces;
    public PieceColor turn;

    private PieceTracker tracker;

    private Result result;

    public int lastDoubleMove; // -1 means no en passant
    public int[] kingPos;

    public boolean WKSCastle, WQSCastle;
    public boolean BKSCastle, BQSCastle;
    public boolean inCheck;


    public final Stack<Board> boardHistory;

    public Board(){
        pieces = new int[64];

        tracker = new PieceTracker();
        boardHistory = new Stack<>();

        kingPos = new int[2];
    }

    public void initializeDefault(){
        Fen.loadFen(this, "8/3K1PP1/8/3k4/8/2q5/5pp1/8 b - - 0 1");

        updateKingInCheck();
    }

    //time complexity: n = 32 + 28 = 80 => O(1)
    public void makeMove(Move move){
        boardHistory.push(this.copy());
        lastDoubleMove = -1;
        move.execute(this);

        swapTurn();
        updateKingInCheck(); //total: n = 32
        updateEndGame();
    }

    public void makeMove(Move move, boolean updateEndGame){
        if(updateEndGame){
            makeMove(move);
            return;
        }
        boardHistory.push(this.copy());
        lastDoubleMove = -1;
        move.execute(this);

        swapTurn();
        updateKingInCheck(); //total: n = 32
    }


    public void unmakeMove() {
        if (!boardHistory.isEmpty()) {
            // Restore the last state from the stack
            Board previousState = boardHistory.pop();
            // Restore fields from the previous state
            this.pieces = previousState.pieces;
            this.turn = previousState.turn;
            this.tracker = previousState.tracker;
            this.lastDoubleMove = previousState.lastDoubleMove;
            this.kingPos = previousState.kingPos;
            this.WKSCastle = previousState.WKSCastle;
            this.WQSCastle = previousState.WQSCastle;
            this.BKSCastle = previousState.BKSCastle;
            this.BQSCastle = previousState.BQSCastle;
            this.inCheck = previousState.inCheck;
        }
    }

    public void setPiece(int piece, int index){
        pieces[index] = piece;
        tracker.addPiece(piece, index);
    }
    public int getPiece(int index){
        return pieces[index];
    }
    public void removePiece(int index){
        int piece = pieces[index];
        pieces[index] = 0;
        tracker.removePiece(piece, index);
    }
    public void updateKingInCheck(){
        inCheck = isInAttackAt(kingPos[turn.value / 8], turn);
    }

    public void updateEndGame(){
        if(getAllLegalMoves(this).isEmpty()){
            if(inCheck){
                result = Win(opponent(turn));
                System.out.println("Checkmate");
            }else{
                result = Draw(Stalemate);
                System.out.println("Stalemate");
            }
        }
    }

    public boolean isKingInCheck(PieceColor color){
        return isInAttackAt(kingPos[color.value / 8], color);
    }
    public static boolean isOutOfBoard(int index){
        return index < 0 || index > 63;
    }

    public boolean isInAttackAt(int index, PieceColor color){
        int[] dirs;

        //check straight attack
        dirs = rookDirs;
        for(int dir : dirs){
            for(int i = 1; i < 8; i++) {
                int newIndex = index + dir*i;

                if (isOutOfBoard(newIndex)) break;
                if (!isLegalNewMove(index, newIndex, dir, i)) break;

                int piece = getPiece(newIndex);

                if (piece == 0) continue;

                PieceType ptype = Pieces.getType(piece);
                PieceColor pcolor = Pieces.getColor(piece);

                //block by teammate
                if (pcolor == color)
                    if(ptype == PieceType.King) continue;
                else break;

                if(i == 1 && ptype == PieceType.King) return true;

                //block by non-straight attack piece
                if (!isStraightAttackPiece(piece)) break;

                return true;
            }
        }

        //check diagonal attack
        dirs = bishopDirs;
        for(int dir : dirs){
            for(int i = 1; i < 8; i++) {
                int newIndex = index + dir*i;

                if (isOutOfBoard(newIndex)) break;
                if (!isLegalNewMove(index, newIndex, dir, i)) break;

                int piece = getPiece(newIndex);

                if (piece == 0) continue;

                PieceType ptype = Pieces.getType(piece);
                PieceColor pcolor = Pieces.getColor(piece);

                //block by teammate
                if (pcolor == color)
                    if(ptype == PieceType.King) continue;
                else break;


                //check if pawn or king
                if(i == 1){
                    int scalar = (color == White) ? 1 : -1;
                    if(dir * scalar < 0 && ptype == PieceType.Pawn) return true;
                    if(ptype == PieceType.King) return true;
                }

                //block by none-diagonal attack piece
                if (!isDiagonalAttackPiece(piece)) break;

                return true;
            }
        }

        //check knight attack
        dirs = knightDirs;
        for(int dir : dirs) {
            int newIndex = index + dir;

            if (isOutOfBoard(newIndex)) continue;
            if (!isLegalNewMove(index, newIndex, dir, 1)) continue;;

            int piece = getPiece(newIndex);

            if (piece == 0) continue;

            //skip teammate
            if (Pieces.getColorValue(piece) == color.value) continue;

            if (Pieces.getTypeValue(piece) == PieceType.Knight.value) return true;
        }

        return false;
    }

    public void setTurn(PieceColor turn){
        this.turn = turn;
    }
    private void swapTurn(){
        turn = (turn == PieceColor.White) ? Black : PieceColor.White;
    }

    //check if it is the player turn
    public boolean checkTurn(PieceColor player){
        return player == turn;
    }

    public boolean isInCheck() {
        return inCheck;
    }

    public List<Integer> getAllPiecePos(PieceColor color) {
        return tracker.getAllPiecePos(color);
    }
    public int getPieceCount(int piece) {
        return tracker.getPieceCount(piece);
    }
    public Board copy() {
        Board copy = new Board();

        // Copy primitive and immutable fields
        copy.turn = this.turn;
        copy.lastDoubleMove = this.lastDoubleMove;
        copy.WKSCastle = this.WKSCastle;
        copy.WQSCastle = this.WQSCastle;
        copy.BKSCastle = this.BKSCastle;
        copy.BQSCastle = this.BQSCastle;
        copy.inCheck = this.inCheck;

        // Deep copy arrays
        copy.pieces = this.pieces.clone();
        copy.kingPos = this.kingPos.clone();

//        // Deep copy collections
//        copy.attackPattern = new HashSet<>(this.attackPattern);

        // Copy PieceTracker (assuming PieceTracker has a copy method)
        if (this.tracker != null) {
            copy.tracker = this.tracker.copy();
        }

        return copy;
    }

    public void setCastingRights(boolean WKSCastle, boolean WQSCastle, boolean BKSCastle, boolean BQSCastle) {
        this.WKSCastle = WKSCastle;
        this.WQSCastle = WQSCastle;
        this.BKSCastle = BKSCastle;
        this.BQSCastle = BQSCastle;
    }

    private PieceColor opponent(PieceColor color){
        return color == White ? Black : White;
    }

    public boolean isEnd(){
        return result != null;
    }

    public Result getResult(){
        return result;
    }
}
