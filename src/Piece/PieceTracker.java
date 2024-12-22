package Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieceTracker {
    // Separate maps for tracking counts of each piece type by color
    private final Map<Integer, Integer> pieceCounts;

    public final List<Integer> whitePiecePositions;
    public final List<Integer> blackPiecePositions;


    public PieceTracker() {
        this.pieceCounts = new HashMap<>();
        this.whitePiecePositions = new ArrayList<>();
        this.blackPiecePositions = new ArrayList<>();
    }

    public void initialize(int[] pieces) {
        whitePiecePositions.clear();
        blackPiecePositions.clear();
        pieceCounts.clear();

        for (int index = 0; index < pieces.length; index++) {
            int piece = pieces[index];
            if (piece == 0) continue; // Skip empty squares
            addPiece(piece, index); // Add the piece to the tracker
        }
    }

    public void addPiece(int piece, int index) {
        PieceColor color = Pieces.getColor(piece);

        if (color == PieceColor.White) {
            whitePiecePositions.add(index);
        } else if (color == PieceColor.Black) {
            blackPiecePositions.add(index);
        }
        pieceCounts.put(piece, pieceCounts.getOrDefault(piece, 0) + 1);
    }

    public void removePiece(int piece, int index) {
        PieceColor color = Pieces.getColor(piece);

        if (color == PieceColor.White) {
            whitePiecePositions.remove((Integer) index); // Remove the index
        } else if (color == PieceColor.Black) {
            blackPiecePositions.remove((Integer) index); // Remove the index
        }

        pieceCounts.put(piece, pieceCounts.getOrDefault(piece, 0) - 1);
        if (pieceCounts.get(piece) == 0) {
            pieceCounts.remove(piece); // Remove the entry if the count is zero
        }
    }

    // Getters for specific piece counts
    public int getPieceCount(int piece) {
        return pieceCounts.getOrDefault(piece, 0);
    }
    public List<Integer> getAllPiecePos(PieceColor color) {
        return color == PieceColor.White
                ? new ArrayList<>(whitePiecePositions)
                : new ArrayList<>(blackPiecePositions);
    }
    public PieceTracker copy() {
        PieceTracker copy = new PieceTracker();
        // Deep copy the pieceCounts map
        copy.pieceCounts.putAll(this.pieceCounts);

        // Deep copy the white and black piece positions
        copy.whitePiecePositions.addAll(this.whitePiecePositions);
        copy.blackPiecePositions.addAll(this.blackPiecePositions);

        return copy;
    }

    /**
     * Prints a summary of the tracker state (useful for debugging).
     */
    public void printSummary() {
        System.out.println("White piece positions: " + whitePiecePositions);
        System.out.println("Black piece positions: " + blackPiecePositions);
        System.out.println("Piece counts: " + pieceCounts);
    }
}
