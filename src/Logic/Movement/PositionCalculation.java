package Logic.Movement;

public class PositionCalculation {
    public static Position CalculateDestination(Position p, Direction d) {
        return new Position(p.getRow() + d.getDeltaRow(), p.getCol() + d.getDeltaCol());
    }

}
