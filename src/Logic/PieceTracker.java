package Logic;

import Piece.Piece;
import Piece.PieceType;
import Piece.PieceColor;

import java.util.ArrayList;
import java.util.List;

public class PieceTracker {
    private List<Piece>[][] lists;
    private int count; // Total number of pieces on the board

    public PieceTracker()
    {
        lists = new List[2][6]; // 2 for color, 6 for piece types
        for (int color = 0; color < 2; color++) {
            for (int type = 0; type < 6; type++) {
                lists[color][type] = new ArrayList<>();
            }
        }
        count = 0;
    }
    public void add(Piece p) {
        if (p == null) return;

        int colorIndex = p.getColor().getColorIndex(); // 0 for white, 1 for black
        int typeIndex = p.getType().getTypeIndex(); // 0-based type index

        lists[colorIndex][typeIndex].add(p);
        ++count;
    }
    public void remove(Piece p) {
        if (p == null) return;

        int colorIndex = p.getColor().getColorIndex();
        int typeIndex = p.getType().getTypeIndex();

        lists[colorIndex][typeIndex].remove(p);
        --count;
    }
    // Get total piece count
    public int getTotalCount() {
        return count;
    }

    // Get count of a specific piece type and color
    public int getCountByTypeAndColor(PieceColor color, PieceType type) {
        int colorIndex = color.getColorIndex();
        int typeIndex = type.getTypeIndex();

        return lists[colorIndex][typeIndex].size();
    }

}
