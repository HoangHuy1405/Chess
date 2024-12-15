package Logic;

import Piece.*;
import Position.Position;

import java.util.HashMap;
import java.util.Map;

public class Fen {
    private static final Map<Character, PieceType> pieces = new HashMap<>(){
        {
            put('r', PieceType.rook);
            put('b', PieceType.bishop);
            put('n', PieceType.knight);
            put('q', PieceType.queen);
            put('k', PieceType.king);
            put('p', PieceType.pawn);
        }
    };
    public static boolean loadFen(String fenString, Board board){
        int index = 0, row = 0, col = 0;
        board.clearBoard();

        // Initialize additional game state variables
        boolean isWhiteTurn = true;  // Default to white's turn
        boolean[] castlingRights = {true, true, true, true}; // [White King, White Queen, Black King, Black Queen]
        String enPassant = "-";  // No en passant by default
        int halfMoveClock = 0;  // Number of half-moves since last capture or pawn move
        int fullMoveNumber = 1;  // Full move counter starts at 1

        String[] fenParts = fenString.split(" ");
        if (fenParts.length < 4) {
            System.out.println("Invalid fen string!");
            return false;  // Invalid FEN (missing important game state info)
        }
        // init board (1st)
        while (index < fenParts[0].length()){
            if(Character.isDigit(fenString.charAt(index))){
                col += Character.getNumericValue(fenString.charAt(index));
            }else{
                if(fenString.charAt(index) == '/'){
                    row ++;
                    col = 0;
                }else{
                    Position pos = new Position(row, col++);
                    Piece piece = generatePiece(fenString.charAt(index), pos);
                    if(piece == null){
                        return false;
                    }
                    board.setPiece(piece, pos);

                }
            }
            index++;
        }
        // who to move (2nd)
        board.setWhiteToMove(fenParts[1].equals("w"));

        // castling rights (3rd)

        // Parse en passant square (4th)

        return true;
    }
    public static String extractFen(Board board) {
        StringBuilder fen = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int count = 0;

            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(new Position(i, j));
                if (piece == null) {
                    count++;
                } else {
                    if (count > 0) {
                        fen.append(count);
                        count = 0;
                    }
                    fen.append(getFenCharacter(piece));
                }
            }
            if (count > 0) {
                fen.append(count);
            }
            if (i < 7) {
                fen.append("/");
            }
        }

        return fen.toString();
    }

    private static char getFenCharacter(Piece piece) {
        PieceColor color = piece.getColor();
        char c = ' ';

        for(Map.Entry<Character, PieceType> entry : pieces.entrySet()){
            if(entry.getValue().equals(piece.getType())){
                c = entry.getKey();
                break;
            }
        }

        return color == PieceColor.white ? Character.toUpperCase(c) : c;
    }
    private static Piece generatePiece(Character c, Position pos){
        if(!pieces.containsKey(Character.toLowerCase(c))){
            return null;
        }

        PieceColor pieceColor;
        if(Character.isLowerCase(c)){
            pieceColor = PieceColor.black;
        }else{
            pieceColor = PieceColor.white;
        }

        return switch (pieces.get(Character.toLowerCase(c))){
            case bishop -> new Bishop(pieceColor, pos);
            case king -> new King(pieceColor, pos);
            case knight -> new Knight(pieceColor, pos);
            case pawn -> new Pawn(pieceColor, pos);
            case queen -> new Queen(pieceColor, pos);
            case rook -> new Rook(pieceColor, pos);
        };
    }

}
