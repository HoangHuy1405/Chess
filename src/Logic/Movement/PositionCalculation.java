package Logic.Movement;

public class PositionCalculation {
    public static Position CalculateDestination(Position p, Direction d) {
        return new Position(p.getX() + d.getDeltaX(), p.getY() + d.getDeltaY());
    }




}
