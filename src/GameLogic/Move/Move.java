package GameLogic.Move;

import GameLogic.Board;
import Piece.*;

import static Piece.PieceType.King;
import static Piece.PieceType.Rook;


public class Move {

    protected int fromPos;
    protected int toPos;

    protected int moveScore;

    public Move(int fromPos, int toPos) {
        this.fromPos = fromPos;
        this.toPos = toPos;
    }

    protected Move(){

    }

    public void execute(Board board){
        int piece = board.getPiece(fromPos);
        board.removePiece(fromPos);
        board.setPiece(piece, toPos);

        if(Pieces.getType(piece) == King) updateKingPos(piece, board);

        if(Pieces.getType(piece) == King || Pieces.getType(piece) == Rook) updateCastle(piece, board);
    }

//    public void changeBoard(Board newBoard) {
//        board = newBoard;
//    }

    public int getFromPos() {
        return fromPos;
    }
    public int getToPos() {
        return toPos;
    }
    public int getMoveScore() {
        return moveScore;
    }
    public void setMoveScore(int moveScore) {
        this.moveScore = moveScore;
    }

    private void updateCastle(int piece, Board board){
        switch(Pieces.getColorValue(piece)) {
            case 0 -> {
                if (!board.WKSCastle && !board.WQSCastle) return;

                int type = Pieces.getTypeValue(piece);
                if (type == King.value) {
                    board.WKSCastle = false;
                    board.WQSCastle = false;
                }

                if (type == Rook.value) {
                    if (fromPos % 8 == 0) board.WQSCastle = false;
                    if (fromPos % 8 == 7) board.WKSCastle = false;
                }
            }
            case 8 -> {
                if (!board.BKSCastle && !board.BQSCastle) return;

                int type = Pieces.getTypeValue(piece);
                if (type == King.value) {
                    board.BKSCastle = false;
                    board.BQSCastle = false;
                }

                if (type == Rook.value) {
                    if (fromPos % 8 == 0) board.BQSCastle = false;
                    if (fromPos % 8 == 7) board.BKSCastle = false;
                }
            }
        }
    }
    private void updateKingPos(int piece, Board board){
        board.kingPos[Pieces.getColorValue(piece)/8] = toPos;
    }

    @Override
    public String toString() {
//        int piece = board.getPiece(fromPos);
        return super.toString().split("\\.")[2].split("@")[0] + " "  //+ Pieces.getColor(piece) + Pieces.getType(piece)
                + "\n from " + fromPos/8 + "-" + fromPos % 8 + " go to " + toPos/8 + "-" + toPos % 8;
    }
}

