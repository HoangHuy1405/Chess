package Logic.Piece;

import Logic.Board;
import Logic.Move;
import Logic.Movement.Direction;
import Logic.Movement.Position;
import Logic.Movement.PositionCalculation;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    private Direction[] directions;

    public Rook(PieceColor color){
        super(PieceType.rook, color);
        directions = new Direction[4];
        addDirection();
    }

    private void addDirection(){
        directions[0] = new Direction(0, 1); //go right
        directions[1] = new Direction(1, 0); //go forward
        directions[2] = new Direction(0, -1); // go left
        directions[3] = new Direction(-1, 0); // go backward
    }

    @Override
    public List<Move> getLegalMoves(Board board, Position curPos){
        return generateLegalMoves(board, curPos);
    }

    private List<Move> generateLegalMoves(Board board, Position curPos){
        List<Move> moves = new ArrayList<>();
        for(Direction d : directions){
            for(int i = 1; i <= 8; i++){
                Position finalPos = PositionCalculation.CalculateDestination(curPos, Direction.CalculateScalarDirection(d, i));
                if(isOutOfBoard(finalPos)) break;

                if (board.getPiece(finalPos) == null){
                    moves.add(new Move(curPos, finalPos));
                    continue;
                }

                if(isSamePieceColor(board.getPiece(finalPos))) break;
                else{
                    moves.add(new Move(curPos, finalPos));
                    break;
                }
            }
        }

        return moves;
    }
    private boolean isOutOfBoard(Position curPos){
        return curPos.getX() > 7 || curPos.getY() > 7 || curPos.getX() < 0 || curPos.getY() < 0;
    }
    private boolean isSamePieceColor(Piece piece){
        return this.color == piece.color;
    }
}
