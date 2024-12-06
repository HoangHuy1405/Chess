package Logic.Position;

public class Direction {
    private int deltaRow;
    private int deltaCol;

    public Direction(int deltaRow, int deltaCol) {
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
    }

    public int getDeltaRow() {
        return deltaRow;
    }
    public int getDeltaCol() {
        return deltaCol;
    }

    public static Direction CalculateScalarDirection(Direction d, int scalar){
        return new Direction(d.getDeltaRow() * scalar, d.getDeltaCol() * scalar);
    }

    public static Direction CalculatePlusDirection(Direction d1, Direction d2){
        return new Direction(d1.getDeltaRow() + d2.getDeltaRow(), d1.getDeltaCol() + d2.getDeltaCol());
    }
}

