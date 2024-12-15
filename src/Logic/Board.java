package Logic;

import Evaluate.ZobristHashing;
import Move.Move;
import Piece.*;
import Position.Direction;
import Position.Position;
import Position.PositionCalculation;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Piece[][] board;
    public Position lastDoublePawnMove;

    private List<Position> occupiedPosWhiteList;
    private List<Position> occupiedPosBlackList;
    private PieceTracker pieceTracker;

    private boolean whiteToMove; // True if White's turn
    private int castlingRights;
    private int enPassantSquare; // -1 if no en passant square, otherwise the square index

    public Board() {
        board = new Piece[8][8];
        occupiedPosWhiteList = new ArrayList<>();
        occupiedPosBlackList = new ArrayList<>();
        pieceTracker = new PieceTracker();
    }

    public void InitializeBoard(){
        Fen.loadFen("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq 10", this);
    }

    public Piece[][] getBoard(){
        return board;
    }
    public List<Position> getOccupiedPosWhiteList() {
        return this.occupiedPosWhiteList;
    }
    public List<Position> getOccupiedPosBlackList() {
        return this.occupiedPosBlackList;
    }
    public PieceColor getColorToMove() {
        return whiteToMove ? PieceColor.white : PieceColor.black;
    }

    public Piece getPiece(Position pos) {
        return board[pos.getRow()][pos.getCol()];
    }
    public void setPiece(Piece piece, Position pos) {
        board[pos.getRow()][pos.getCol()] = piece;
        if(piece != null) {
            if(piece.getColor().equals(PieceColor.white)) {
                occupiedPosWhiteList.add(new Position(pos.getRow(), pos.getCol()));
            } else {
                occupiedPosBlackList.add(new Position(pos.getRow(), pos.getCol()));
            }
            pieceTracker.add(piece);
        }

    }
    public void removePiece(Position pos) {
        Piece piece = getPiece(pos);
        board[pos.getRow()][pos.getCol()] = null;
        if(piece.getColor().equals(PieceColor.white)) {
            occupiedPosWhiteList.remove(new Position(pos.getRow(), pos.getCol()));
        } else {
            occupiedPosBlackList.remove(new Position(pos.getRow(), pos.getCol()));
        }
        pieceTracker.remove(piece);
    }

    public void clearBoard() {
        board = new Piece[8][8];
    }

    // only for debug/init
    // the game will automatically update occupied pos
    public void findOccupiedPos() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Position position = new Position(i,j);
                if(getPiece(position) != null) {
                    Piece piece = getPiece(position);
                    if(piece.getColor().equals(PieceColor.white)) {
                        occupiedPosWhiteList.add(position);
                    } else {
                        occupiedPosBlackList.add(position);
                    }
                }
            }
        }
    }
    public long getHash() {
        return ZobristHashing.computeZobristHash(board, whiteToMove);
    }
    public void setWhiteToMove(boolean whiteToMove) {
        this.whiteToMove = whiteToMove;
    }


    public Board copy() {
        Board newBoard = new Board();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position pos = new Position(i, j);
                Piece piece = this.getPiece(pos);
                if (piece != null) {
                    newBoard.setPiece(piece.copy(), pos);
                }
            }
        }
        return newBoard;
    }

    public boolean isOutOfBoard(Position pos) {
        return pos.getRow() > 7 || pos.getCol() > 7 || pos.getRow() < 0 || pos.getCol() < 0;
    }
    public boolean isInCheck(PieceColor color) {
        Position kingPos = findKing(color);
        if(kingPos == null) return false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = getPiece(new Position(i, j));
                if (piece == null) continue;
                if(piece.getColor() == color) {
                    continue;
                }
                Position pos = new Position(i, j);
                List<Move> moves = piece.getMoves(this, pos);

                for(Move move : moves)
                    if(move.getToPos().equals(kingPos))
                        return true;


            }
        }

        return false;
    }

    public boolean isInCheckAt(Position pos, PieceColor color) {
        Direction[] dirs = {
            new Direction(0,1),
            new Direction(0,-1),
            new Direction(1,0),
            new Direction(-1,0),

            new Direction(1,1),
            new Direction(1,-1),
            new Direction(-1,1),
            new Direction(-1,-1),

            new Direction(2, 1),
            new Direction(1, 2),
            new Direction(-2, 1),
            new Direction(-1, 2),
            new Direction(2, -1),
            new Direction(1, -2),
            new Direction(-2, -1),
            new Direction(-1, -2),
        };

        for (Direction dir : dirs) {
            Position checkingPos;
            if(dir.getDeltaRow() == 2 || dir.getDeltaCol() == 2 || dir.getDeltaRow() == -2 || dir.getDeltaCol() == -2){
                checkingPos = PositionCalculation.CalculateDestination(pos, dir);

                if(isOutOfBoard(checkingPos)) continue;
                Piece piece = getPiece(checkingPos);

                if(piece == null) continue;
                if(piece.getColor() == color) continue;

                if(piece.getType() == PieceType.knight) return true;
                continue;
            }

            if(dir.getDeltaRow() == 0 || dir.getDeltaCol() == 0){
                for(int i = 1; i < 8; i++){
                    checkingPos = PositionCalculation.CalculateDestination(pos, Direction.CalculateScalarDirection(dir, i));

                    if(isOutOfBoard(checkingPos)) break;
                    Piece piece = getPiece(checkingPos);

                    if(piece == null) continue;
                    if(piece.getColor() == color) break;
                    if(i == 1 && piece.getType() == PieceType.king) return true;

                    if(piece.getType() == PieceType.queen || piece.getType() == PieceType.rook) return true;
                }
                continue;
            }

            for(int i = 1; i < 8; i++){
                checkingPos = PositionCalculation.CalculateDestination(pos, Direction.CalculateScalarDirection(dir, i));

                if(isOutOfBoard(checkingPos)) break;
                Piece piece = getPiece(checkingPos);

                if(piece == null) continue;
                if(piece.getColor() == color) break;
                if(i == 1 && (piece.getType() == PieceType.king || piece.getType() == PieceType.pawn)) return true;

                if(piece.getType() == PieceType.queen || piece.getType() == PieceType.bishop) return true;
            }
        }
        return false;
    }

    private Position findKing(PieceColor color){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = getPiece(new Position(i, j));
                if(piece == null) continue;
                if(piece.getColor() == color && piece instanceof King) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    public int countOfPiece(PieceColor color, PieceType type) {
        return pieceTracker.getCountByTypeAndColor(color, type);
    }

}
