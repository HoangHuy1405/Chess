package GameLogic.Move;

import GameLogic.Board;
import Piece.PieceColor;
import Piece.PieceType;
import Piece.Pieces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Evaluate.Evaluate.printBoard;
import static GameLogic.Board.*;
import static GameLogic.Move.Castle.*;
import static Piece.PieceColor.*;
import static Piece.Pieces.*;
import static Piece.PieceType.*;

public class MoveGenerator {
    public static final int[] queenDirs = {-9, -8, -7, -1, 1, 7, 8, 9};
    public static final int[] rookDirs = { -8, -1, 1, 8};
    public static final int[] bishopDirs = { -9, -7, 7, 9};
    public static final int[] knightDirs = {-17, -15, -10, -6, 6, 10, 15, 17};
    public static final int[] kingDirs = {-9, -8, -7, -1, 1, 7, 8, 9};

    public static HashMap<Integer, List<Move>> generateLegalMoves(int index, Board board){
        HashMap<Integer, List<Move>> legalMoves = new HashMap<>();

        int piece = board.getPiece(index);
        if(piece == 0) return legalMoves;

        PieceColor color = getColor(piece);
        if(!board.checkTurn(color)) return legalMoves;

        PieceType type = getType(piece);

        switch(type){
            case Queen -> generateSlidingMoves(color, index, queenDirs, board, legalMoves);
            case Rook -> generateSlidingMoves(color, index, rookDirs, board, legalMoves);
            case Bishop -> generateSlidingMoves(color, index, bishopDirs, board, legalMoves);
            case Knight -> generateKnightMoves(color, index, knightDirs, board, legalMoves);
            case Pawn -> generatePawnMoves(color, index, board, legalMoves);
            case King -> generateKingMoves(color, index, kingDirs, board, legalMoves);
        }
        return legalMoves;
    }

    private static void generateSlidingMoves(PieceColor color, int index, int[] dirs, Board board, HashMap<Integer, List<Move>> moves){
        for (int dir : dirs) {
            for (int i = 1; i < 8; i++) {
                int newIndex = index + dir * i;

                if (isOutOfBoard(newIndex)) break;
//                if ((dir == -9 || dir == -7 || dir == 7 || dir == 9) && (Math.abs((newIndex % 8) - (index % 8)) != i || Math.abs((newIndex / 8) - (index / 8)) != i)) break;
//                if ((dir == -1 || dir == 1) && Math.abs((newIndex/8) - (index/8)) != 0) break;
                if(!isLegalNewMove(index, newIndex, dir, i)) break;

                int piece = board.getPiece(newIndex);

                if(piece != 0 && Pieces.getColor(piece) == color) break;

                Move move = new Move(index, newIndex);

                if(isLeadToACheck(move, board)) continue;

                List<Move> moveList = moves.getOrDefault(newIndex, new ArrayList<>());
                moveList.add(move);
                moves.put(newIndex, moveList);

                if(piece != 0) break;
//
//                if(piece == 0){
//                    // not in check
//                    if(!board.inCheck){
//                        //piece pinned can only move in a pinned Pattern
//                        //if a piece is pinned and its new move is not in pinnedPattern => break
//                        if(board.pinIndex.contains(index) && !board.pinnedPattern.contains(newIndex)) break;
//
//                        List<Move> moveList = moves.getOrDefault(newIndex, new ArrayList<>());
//                        moveList.add(new Move(index, newIndex, board));
//                        moves.put(newIndex, moveList);
//
//                    }else{// in check
//                        //can only block a check
//                        if(board.attackPattern.contains(newIndex)){
//                            List<Move> moveList = moves.getOrDefault(newIndex, new ArrayList<>());
//                            moveList.add(new Move(index, newIndex, board));
//                            moves.put(newIndex, moveList);
//                        }
//                    }
//
//
//                }else {
//                    if (getColorValue(piece) == color.value) break;
//
//                    // not in check
//                    if(!board.inCheck){
//                        //piece pinned can only move in a pinned Pattern
//                        //if a piece is pinned and its new move is not in pinnedPattern => break
//                        if(board.pinIndex.contains(index) && !board.pinnedPattern.contains(newIndex)) break;
//
//                        List<Move> moveList = moves.getOrDefault(newIndex, new ArrayList<>());
//                        moveList.add(new Move(index, newIndex, board));
//                        moves.put(newIndex, moveList);
//                    }else{// in check
//                        //can only block a check
//                        if(board.attackPattern.contains(newIndex)){
//                            List<Move> moveList = moves.getOrDefault(newIndex, new ArrayList<>());
//                            moveList.add(new Move(index, newIndex, board));
//                            moves.put(newIndex, moveList);
//                        }
//
//                    }
//                    break;
//                }
            }
        }
    }
    private static void generatePawnMoves(PieceColor color, int index, Board board, HashMap<Integer, List<Move>> moves) {
        int scalar = (color == White) ? -1 : 1;
        int[] attackDirs = {7 * scalar, 9 * scalar};

        //attack move
        for (int dir : attackDirs) {
            int newIndex = index + dir;

            if(isOutOfBoard(newIndex)) break;

            int p = board.getPiece(newIndex);

//            if (Math.abs((newIndex % 8) - (index % 8)) != 1) continue;
            if (!isLegalNewMove(index, newIndex, dir, 1)) continue;

            //en passant
            if (board.lastDoubleMove != -1) {
                if (newIndex == board.lastDoubleMove) {

                    EnPassant move = new EnPassant(index, newIndex, board.lastDoubleMove - 8 * scalar);
                    if(isLeadToACheck(move, board)) continue;

                    List<Move> moveList = moves.getOrDefault(newIndex, new ArrayList<>());
                    moveList.add(move);
                    moves.put(newIndex, moveList);

                    continue; // there is no scenario that makes en passant and normal attack at the same time
                }
            }

            if (p == 0 || getColorValue(p) == color.value) continue;

            //add move
            List<Move> moveList = moves.getOrDefault(newIndex, new ArrayList<>());
            if(isRankPromote(index, color)){
                moveList.addAll(generateLegalPromote(board, index, newIndex, color));
            }else {
                Move move = new Move(index, newIndex);
                if(!isLeadToACheck(move, board)) moveList.add(move);
            }

            if(!moveList.isEmpty()) moves.put(newIndex, moveList);
//            if(!board.inCheck || board.attackPattern.contains(newIndex)){
//                List<Move> moveList = moves.getOrDefault(newIndex, new ArrayList<>());
//                moveList.add(new Move(index, newIndex, board));
//                moves.put(newIndex, moveList);
//            }
        }

        //forward move
        int dir = 8 * scalar;
        int newIndex = index + dir;
        int p = board.getPiece(newIndex);

        // a piece block
        if (p != 0) return;
        List<Move> moveList = moves.getOrDefault(newIndex, new ArrayList<>());

        if(isRankPromote(index, color)){
            moveList.addAll(generateLegalPromote(board, index, newIndex, color));
        }else {
            Move move = new Move(index, newIndex);
            if(!isLeadToACheck(move, board)) moveList.add(move);
        }

        if(!moveList.isEmpty()) moves.put(newIndex, moveList);

        //double move
        if (!(color == Black && index/8 == 1) && !(color == White && index/8 == 6)) return;

        newIndex += dir;
        p = board.getPiece(newIndex);

        // a teammate piece block
        if (p != 0) return;


        DoubleMove move = new DoubleMove(index, newIndex);
        if(isLeadToACheck(move, board)) return;

        moveList = moves.getOrDefault(newIndex, new ArrayList<>());
        moveList.add(move);
        moves.put(newIndex, moveList);
//        if(p == 0) {
//            if(!board.inCheck || board.attackPattern.contains(newIndex)){
//                List<Move> moveList = moves.getOrDefault(newIndex, new ArrayList<>());
//                moveList.add(new Move(index, newIndex, board));
//                moves.put(newIndex, moveList);
//            }
//
//            //double move
//            if((index/8 == 1 && color == Black) || (index/8 == 6 && color == White)){
//                newIndex += dir;
//                p = board.getPiece(newIndex);
//
//                if(p == 0)
//                    if(!board.inCheck || board.attackPattern.contains(newIndex)){
//                        List<Move> moveList = moves.getOrDefault(newIndex, new ArrayList<>());
//                        moveList.add(new DoubleMove(index, newIndex, board));
//                        moves.put(newIndex, moveList);
//                    }
//            }
    }
    private static void generateKnightMoves(PieceColor color, int index, int[] dirs, Board board, HashMap<Integer, List<Move>> moves) {
        for(int dir : dirs) {
            int newIndex = index + dir;

            if(isOutOfBoard(newIndex)) continue;
            if(!isLegalNewMove(index, newIndex, dir, 1)) continue;

            int piece = board.getPiece(newIndex);
            if(piece != 0 && getColorValue(piece) == color.value) continue;

            Move move = new Move(index, newIndex);

            if(isLeadToACheck(move, board)) continue;

            List<Move> moveList = moves.getOrDefault(newIndex, new ArrayList<>());
            moveList.add(move);
            moves.put(newIndex, moveList);
        }
    }
    private static void generateKingMoves(PieceColor color, int index, int[] dirs, Board board, HashMap<Integer, List<Move>> moves) {
        //normal move
        //total: n =  32 * 8 = 256
        for(int dir : dirs){
            int newIndex = index + dir;

            if(isOutOfBoard(newIndex)) continue;
            if(!isLegalNewMove(index, newIndex, dir, 1)) continue;
//            if ((dir == -9 || dir == -7 || dir == 7 || dir == 9) && Math.abs((newIndex % 8) - (index % 8)) > 1) break;
//            if ((dir == -1 || dir == 1) && Math.abs((newIndex/8) - (index/8)) != 0) break;

            int piece = board.getPiece(newIndex);
            if(piece != 0 && getColorValue(board.getPiece(newIndex)) == color.value) continue;

            Move move = new Move(index, newIndex);
            //check if move go into an attack
            if(isLeadToACheck(move, board)) continue;

            List<Move> moveList = moves.getOrDefault(newIndex, new ArrayList<>());
            moveList.add(new Move(index, newIndex));
            moves.put(newIndex, moveList);
        }

        if(board.isKingInCheck(color)) return;

        //castle move
        //total: n =  64 + 96 = 160
        switch (color){
            case White -> {
                //total: n = 32 * 2 = 64
                if (board.WKSCastle && board.getPiece(61) == 0 && board.getPiece(62) == 0 &&
                    board.getPiece(63) == WhiteRook &&
                    !board.isInAttackAt(61, color) && !board.isInAttackAt(62, color))
                {
                    List<Move> moveList = moves.getOrDefault(62, new ArrayList<>());
                    moveList.add(new Castle(White, KS_CASTLE));
                    moves.put(62, moveList);
                }


                //total: n = 32 * 3 = 96
                if (board.WQSCastle && board.getPiece(57) == 0 && board.getPiece(58) == 0 && board.getPiece(59) == 0 &&
                    board.getPiece(56) == WhiteRook &&
                    !board.isInAttackAt(57, color) && !board.isInAttackAt(58, color) && !board.isInAttackAt(59, color)){

                    List<Move> moveList = moves.getOrDefault(58, new ArrayList<>());
                    moveList.add(new Castle(White, QS_CASTLE));
                    moves.put(58, moveList);
                }

            }
            case Black -> {
                //total: n = 32 * 3 = 64
                if (board.BKSCastle && board.getPiece(5) == 0 && board.getPiece(6) == 0 &&
                    board.getPiece(7) == BlackRook &&
                    !board.isInAttackAt(5, color) && !board.isInAttackAt(6, color)){

                    List<Move> moveList = moves.getOrDefault(6, new ArrayList<>());
                    moveList.add(new Castle(Black, KS_CASTLE));
                    moves.put(6, moveList);
                }

                //total: n = 32 * 3 = 96
                if (board.BQSCastle && board.getPiece(1) == 0 && board.getPiece(2) == 0 && board.getPiece(3) == 0 &&
                    board.getPiece(0) == BlackRook &&
                    !board.isInAttackAt(1, color) && !board.isInAttackAt(2, color) && !board.isInAttackAt(3, color)){

                    List<Move> moveList = moves.getOrDefault(2, new ArrayList<>());
                    moveList.add(new Castle(Black, QS_CASTLE));
                    moves.put(2, moveList);
                }
            }
        }
    }

    public static List<Move> getAllLegalMoves(Board board) {
        List<Move> allLegalMoves = new ArrayList<>();
        for(int i = 0; i < 64; i++) {
            HashMap<Integer, List<Move>> moveMap  = generateLegalMoves(i, board);
            if(moveMap.isEmpty()) continue;

            for (List<Move> moves : moveMap.values()) {
                allLegalMoves.addAll(moves);
            }
        }
        return allLegalMoves;
    }
    public static boolean isLegalNewMove(int fromPos, int toPos, int dir, int scalar){
        int deltaRow = Math.abs((toPos/8) - (fromPos/8));
        int deltaCol = Math.abs((toPos%8) - (fromPos%8));

        if((dir == -1 || dir == 1) && (deltaRow != 0)) return false;
        if((dir == -9 || dir == -7 || dir == 7 || dir == 9) && (deltaRow != scalar || deltaCol != scalar)) return false;
        if((dir == -17 || dir == -15 || dir == -10 || dir == -6 ||
            dir == 17  || dir == 15  || dir == 10  || dir == 6) && (deltaCol > 2)) return false;

        return true;
    }
    private static boolean isLeadToACheck(Move move, Board board){
        boolean result = false;
        PieceColor color = board.turn == White ? White : Black;

        board.makeMove(move, false);
        if(board.isKingInCheck(color)) result = true;
        board.unmakeMove();
        return result;
    }
    private static List<Move> generateLegalPromote(Board board, int index, int newIndex, PieceColor color){
        List<Move> moveList = new ArrayList<>();

        Promote qP = new Promote(index, newIndex, color.value | Queen.value);
        Promote rP = new Promote(index, newIndex, color.value | Rook.value);
        Promote nP = new Promote(index, newIndex, color.value | Knight.value);
        Promote bP = new Promote(index, newIndex, color.value | Bishop.value);

        if(!isLeadToACheck(qP, board)) moveList.add(qP);
        if(!isLeadToACheck(rP, board)) moveList.add(rP);
        if(!isLeadToACheck(nP, board)) moveList.add(nP);
        if(!isLeadToACheck(bP, board)) moveList.add(bP);

        return moveList;
    }
    private static boolean isRankPromote(int index, PieceColor color){
        return (color == Black && index/8 == 6) || (color == White && index /8 == 1);
    }



}
