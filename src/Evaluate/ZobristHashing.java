package Evaluate;

import Move.Move;
import Piece.Piece;
import Piece.PieceType;

import java.util.Random;

public class ZobristHashing {
    private static final int BOARD_ROWS = 8;
    private static final int BOARD_COLS = 8;
    private static final int NUM_PIECE_TYPES = 12; // 6 types (pawn, knight, etc.) * 2 colors
    public static final long[][][] zobristTable = new long[6][2][64];
    private static final long sideToMoveHash;
    private static final long[] castlingRightsHash = new long[16];
    private static final long[] enPassantHash = new long[BOARD_COLS];

    public static long computeZobristHash(Piece[][] board, boolean whiteToMove) {
        long hash = 0L;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null) {
                    int typeIndex = piece.getType().getTypeIndex(); // Get PieceType index
                    int colorIndex = piece.getColor().getColorIndex(); // Get PieceColor index
                    int squareIndex = row * 8 + col; // Convert (row, col) to square index

                    // XOR the hash with the piece's random value
                    hash ^= zobristTable[typeIndex][colorIndex][squareIndex];
                }
            }
        }
        if (whiteToMove) {
            hash ^= sideToMoveHash;
        }
        return hash;
    }
    public static long updateHash(long currentHash, Move move, Piece[][] board, boolean whiteToMove) {
        int fromSquare = move.getFromPos().getRow() * 8 + move.getFromPos().getCol();
        int toSquare = move.getToPos().getRow() * 8 + move.getToPos().getCol();

        Piece movedPiece = board[move.getFromPos().getRow()][move.getFromPos().getCol()];
        Piece capturedPiece = board[move.getToPos().getRow()][move.getToPos().getCol()];

        // XOR out the moved piece from its original square
        currentHash ^= zobristTable[movedPiece.getType().getTypeIndex()]
                [movedPiece.getColor().getColorIndex()]
                [fromSquare];

        // XOR in the moved piece at its new square
        currentHash ^= zobristTable[movedPiece.getType().getTypeIndex()]
                [movedPiece.getColor().getColorIndex()]
                [toSquare];

        // XOR out the captured piece, if any
        if (capturedPiece != null) {
            currentHash ^= zobristTable[capturedPiece.getType().getTypeIndex()]
                    [capturedPiece.getColor().getColorIndex()]
                    [toSquare];
        }

        // XOR for turn change
        currentHash ^= sideToMoveHash;

        return currentHash;
    }


    static {
        Random random = new Random(42069); // Seeded for reproducibility
        for (int type = 0; type < 6; type++) {
            for (int color = 0; color < 2; color++) {
                for (int square = 0; square < 64; square++) {
                    zobristTable[type][color][square] = random.nextLong();
                }
            }
        }
        // Initialize side to move hash
        sideToMoveHash = random.nextLong();

        // Initialize castling rights hashes
        for (int i = 0; i < castlingRightsHash.length; i++) {
            castlingRightsHash[i] = random.nextLong();
        }

        // Initialize en passant hashes
        for (int col = 0; col < BOARD_COLS; col++) {
            enPassantHash[col] = random.nextLong();
        }
    }

    public static long getPieceSquareHash(int pieceType, int row, int col) {
        return zobristTable[pieceType][row][col];
    }

    public static long getSideToMoveHash() {
        return sideToMoveHash;
    }

    public static long getCastlingHash(int castlingRights) {
        return castlingRightsHash[castlingRights];
    }

    public static long getEnPassantHash(int col) {
        return enPassantHash[col];
    }


}
