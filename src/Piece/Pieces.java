package Piece;

import static Piece.PieceColor.*;

public class Pieces {
    public static final int WhitePawn = White.value | PieceType.Pawn.value;
    public static final int WhiteRook = White.value | PieceType.Rook.value;
    public static final int WhiteKnight = White.value | PieceType.Knight.value;
    public static final int WhiteBishop = White.value | PieceType.Bishop.value;
    public static final int WhiteQueen = White.value | PieceType.Queen.value;
    public static final int WhiteKing = White.value | PieceType.King.value;

    public static final int BlackPawn = PieceColor.Black.value | PieceType.Pawn.value;
    public static final int BlackRook = PieceColor.Black.value | PieceType.Rook.value;
    public static final int BlackKnight = PieceColor.Black.value | PieceType.Knight.value;
    public static final int BlackBishop = PieceColor.Black.value | PieceType.Bishop.value;
    public static final int BlackQueen = PieceColor.Black.value | PieceType.Queen.value;
    public static final int BlackKing = PieceColor.Black.value | PieceType.King.value;

    public static boolean isSlidingPiece(int piece){
        int type = getTypeValue(piece);
        return type == PieceType.Bishop.value || type == PieceType.Queen.value || type == PieceType.Rook.value;
    }
    public static boolean isStraightAttackPiece(int piece){
        int type = getTypeValue(piece);
        return type == PieceType.Queen.value || type == PieceType.Rook.value;
    }
    public static boolean isDiagonalAttackPiece(int piece){
        int type = getTypeValue(piece);
        return type == PieceType.Queen.value || type == PieceType.Bishop.value;
    }

    public static PieceColor getColor(int piece){
        return switch (getColorValue(piece)) {
            case 0 -> White;
            case 8 -> PieceColor.Black;

            default -> PieceColor.None;
        };
    }
    public static int getColorValue(int piece){
        return (piece >>> 3) << 3; // remove all piece bits
    }

    public static PieceType getType(int piece){
        return switch (getTypeValue(piece)) {
            case 1 -> PieceType.Pawn;
            case 2 -> PieceType.Knight;
            case 3 -> PieceType.Bishop;
            case 4 -> PieceType.Rook;
            case 5 -> PieceType.Queen;
            case 6 -> PieceType.King;
            default -> PieceType.None;
        };
    }
    public static int getTypeValue(int piece){
        // (10000 | 01000) = 11000
        // ~11000 = 00111
        // XXXXX & 00111 = 00XXX
        return ~(White.value | PieceColor.Black.value) & piece; //remove all color bits
    }

    public static PieceColor getColorByValue(int value){
        return switch (value) {
            case 0 -> White;
            case 8 -> Black;
            default -> None;
        };
    }

}
