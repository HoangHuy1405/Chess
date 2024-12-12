package Logic.Piece;

import Logic.Board;

import Logic.Move.*;
import Logic.Position.Direction;
import Logic.Position.Position;
import Logic.Position.PositionCalculation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static GUI.Panel.BoardPanel.SQUARE_SIZE;
import static GUI.PieceImage.getPieceImage;

public abstract class Piece {
    private BufferedImage img;

    private Position initialBoardPos;
    private Position curPos;
    private Position dragPos;

    public boolean isDragging;

    public boolean hasMoved;
    protected PieceType type;
    protected Player color;

    protected Piece(PieceType type, Player color, Position initialBoardPos) {
        this.type = type;
        this.color = color;

        this.initialBoardPos = initialBoardPos;
        this.curPos = initialBoardPos;
        this.dragPos = initialBoardPos;

        this.img = getPieceImage(type, color);
        hasMoved = false;
        isDragging = false;
    }

    public void resetAll(){
        isDragging = false;
        curPos = initialBoardPos;
        dragPos = initialBoardPos;
    }

    public Position getInitialBoardPos(){
        return initialBoardPos;
    }
    public void setInitialBoardPos(Position initialBoardPos){
        this.initialBoardPos = initialBoardPos;
    }

    public Player getColor() {
        return color;
    }
    public PieceType getType() {
        return type;
    }

    public void setCurPos(Position curPos){
        this.curPos = curPos;
    }

    protected Map<Position, Move> generateMovesFromDirs(Board board, Position curPos, Direction[] directions){
        Map<Position, Move> moves = new HashMap<>();
        for(Direction d : directions){
            for(int i = 1; i <= 8; i++){
                Position finalPos = PositionCalculation.CalculateDestination(curPos, Direction.CalculateScalarDirection(d, i));
                if(board.isOutOfBoard(finalPos)) break;

                Piece piece = board.getPiece(finalPos);

                if (piece == null){
                    moves.put(finalPos, new NormalMove(curPos, finalPos));
                    continue;
                }

                if(!this.isSameColor(piece)) {
                    moves.put(finalPos, new NormalMove(curPos, finalPos));
                }
                break;
            }
        }

        return moves;
    }
    protected Map<Position, Move> generateMoveFromDirs(Board board, Position curPos, Direction[] directions){
        Map<Position, Move> moves = new HashMap<>();
        for(Direction direction : directions) {
            Position finalPos = PositionCalculation.CalculateDestination(curPos, direction);
            if(board.isOutOfBoard(finalPos)) continue;
            Piece piece = board.getPiece(finalPos);

            if (piece != null) {
                if(this.isSameColor(piece))
                    continue;
            }
            moves.put(finalPos, new NormalMove(curPos, finalPos));
        }

        return moves;
    }

    protected boolean isSameColor(Piece piece) {
        return this.color == piece.getColor();
    }

    abstract public Map<Position, Move> getMoves(Board board, Position curPos);

    public Piece copy(){
        switch(this.type){
            case king -> {
                Piece king = new King(this.color, this.initialBoardPos);
                king.hasMoved = this.hasMoved;
                return king;
            }
            case queen -> {
                Piece queen = new Queen(this.color, this.initialBoardPos);
                queen.hasMoved = this.hasMoved;
                return queen;
            }
            case bishop -> {
                Piece bishop = new Bishop(this.color, this.initialBoardPos);
                bishop.hasMoved = this.hasMoved;
                return bishop;
            }
            case knight -> {
                Piece knight = new Knight(this.color, this.initialBoardPos);
                knight.hasMoved = this.hasMoved;
                return knight;
            }
            case rook -> {
                Piece rook = new Rook(this.color, this.initialBoardPos);
                rook.hasMoved = this.hasMoved;
                return rook;
            }
            case pawn -> {
                Piece pawn = new Pawn(this.color, this.initialBoardPos);
                pawn.hasMoved = this.hasMoved;
                return pawn;
            }
            default -> {
                return null;
            }
        }
    }

    public void update(){
        if(isDragging) {
            dragPos = curPos;
        }
    }

    public void draw(Graphics g){
        if(isDragging){
            drawAtDraggingPos(g);
        }else{
            drawAtInitialBoardPos(g);
        }
    }

    public void drawAtInitialBoardPos(Graphics g) {
        g.drawImage(this.img, initialBoardPos.getCol() * SQUARE_SIZE, initialBoardPos.getRow() * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
    }
    public void drawAtDraggingPos(Graphics g) {
        g.drawImage(this.img, dragPos.getCol() - SQUARE_SIZE/2, dragPos.getRow() - SQUARE_SIZE/2, SQUARE_SIZE, SQUARE_SIZE, null);
    }
}
