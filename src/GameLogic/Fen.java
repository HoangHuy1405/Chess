package GameLogic;



import java.util.HashMap;

import static Piece.Pieces.*;
import static Piece.PieceColor.*;

public class Fen {
    public static boolean loadFen(Board board, String fenString){
        int index = 0;
        String[] fenParts = fenString.split(" ");
        for(char c : fenParts[0].toCharArray()){
            if(Character.isLetter(c)){
                int piece = getPiece(c);
                if(piece == 0) return false;

                if(piece == WhiteKing) board.kingPos[0] = index;
                if(piece == BlackKing) board.kingPos[1] = index;

                board.setPiece(piece, index++);
            }else{
                if(c == '/') continue;
                index += Character.getNumericValue(c);
            }
        }
        // who to move
        board.setTurn(fenParts[1].equals("w") ? White : Black);

        // castling rights
        String castling = fenParts[2];
        board.setCastingRights(castling.contains("K"), castling.contains("Q"), castling.contains("k"),castling.contains("q") );

        // Parse en passant square
        String enPassant = fenParts[3];
        if (enPassant.equals("-")) {
            board.lastDoubleMove = -1;
        } else {
            board.lastDoubleMove = getSquareIndex(enPassant);
        }
        return true;
    }
//    private boolean loadFen(HashMap<Character, Long> bitboards, String fenString){
//        int index = 63;
//        for(char c : fenString.toCharArray()){
//            if(bitboards.containsKey(c)){
//                long bit = 1L << index;
//                bitboards.put(c, bitboards.get(c) | bit);
//                index--;
//            }else if(Character.isDigit(c)) index -= Character.getNumericValue(c);
//            else if(c != '/') return false;
//        }
//        return true;
//    }
    public static int getPiece(char c){
        switch(Character.toUpperCase(c)){
            case 'P' -> { return Character.isLowerCase(c) ? BlackPawn : WhitePawn; }
            case 'N' -> { return Character.isLowerCase(c) ? BlackKnight : WhiteKnight; }
            case 'B'-> { return Character.isLowerCase(c) ? BlackBishop : WhiteBishop; }
            case 'R' -> { return Character.isLowerCase(c) ? BlackRook : WhiteRook; }
            case 'Q' -> { return Character.isLowerCase(c) ? BlackQueen : WhiteQueen; }
            case 'K' -> { return Character.isLowerCase(c) ? BlackKing : WhiteKing; }
            default -> { return 0; }
        }
    }

    private static int getSquareIndex(String squareName) {
        char file = squareName.charAt(0); // 'a' to 'h'
        char rank = squareName.charAt(1); // '1' to '8'

        int fileIndex = file - 'a'; // Convert 'a' to 0, 'b' to 1, ...
        int rankIndex = 8 - (rank - '0'); // Convert '1' to 7, '2' to 6, ...
        return rankIndex * 8 + fileIndex; // Compute 0-based index
    }


}
