package Logic.Movement;

public class Direction {
    private int deltaX;
    private int deltaY;

    public Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }
    public int getDeltaY() {
        return deltaY;
    }

    public static Direction CalculateScalarDirection(Direction d, int scalar){
        return new Direction(d.getDeltaX() * scalar, d. getDeltaY() * scalar);
    }

    public static Direction CalculatePlusDirection(Direction d1, Direction d2){
        return new Direction(d1.getDeltaX() + d2.getDeltaX(), d1.getDeltaY() + d2.getDeltaY());
    }
}

