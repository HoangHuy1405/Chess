package Move;

import Piece.Piece;
import Position.Position;

public class MoveState {
    public Move lastMove;
    public Piece capturedPiece;
    public Piece movingPiece;

    public Position doublePawnMovePosition; // For en passant
    public boolean[] castlingRights; // Store castling rights
    public Position kingPosition; // Save king's position
}
