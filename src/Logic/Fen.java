package Logic;

import Logic.Movement.Position;
import Logic.Piece.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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

        while (index < fenString.length()){
            if(Character.isDigit(fenString.charAt(index))){
                col += Character.getNumericValue(fenString.charAt(index));
            }else{
                if(fenString.charAt(index) == '/'){
                    row ++;
                    col = 0;
                }else{
                    Piece piece = generatePiece(fenString.charAt(index));
                    if(piece == null){
                        return false;
                    }

                    board.setPiece(piece, new Position(row, col++));
                }
            }
            index++;
        }

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
        Player color = piece.getColor();
        char c = ' ';

        for(Map.Entry<Character, PieceType> entry : pieces.entrySet()){
            if(entry.getValue().equals(piece.getType())){
                c = entry.getKey();
                break;
            }
        }

        return color == Player.white ? Character.toUpperCase(c) : c;
    }
    private static Piece generatePiece(Character c){
        if(!pieces.containsKey(Character.toLowerCase(c))){
            return null;
        }

        Player player;
        if(Character.isLowerCase(c)){
            player = Player.black;
        }else{
            player = Player.white;
        }

        return switch (pieces.get(Character.toLowerCase(c))){
            case bishop -> new Bishop(player);
            case king -> new King(player);
            case knight -> new Knight(player);
            case pawn -> new Pawn(player);
            case queen -> new Queen(player);
            case rook -> new Rook(player);
        };
    }

}
