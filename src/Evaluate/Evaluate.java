package Evaluate;

import Logic.GameManager;
import Move.*;
import Piece.Piece;
import Piece.PieceColor;
import Position.Position;

import java.util.List;

public class Evaluate {
    /*static final int pawnVal = 1;
    static final int knightVal = 3;
    static final int bishopVal = 3;
    static final int rookVal = 5;
    static final int queenVal = 9;*/

    public static GameManager gameManager;
    public static int evaluateCount = 0;

    private static int evaluate() {
        int whiteVal = CountMaterialValue(PieceColor.white);
        int blackVal = CountMaterialValue(PieceColor.black);

        int playerTurn = gameManager.getPlayer_turn().equals(PieceColor.white) ? 1 : -1;
        return (whiteVal - blackVal)*playerTurn;
    }

    private static int CountMaterialValue(PieceColor color) {
        int materialVal = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Piece piece = gameManager.getBoard().getPiece(new Position(i,j));
                if(piece != null && color.equals(piece.getColor())) {
                    /*switch (piece.getType()) {
                        case rook:
                            materialVal += rookVal;
                            break;
                        case knight:
                            materialVal += knightVal;
                            break;
                        case bishop:
                            materialVal += bishopVal;
                            break;
                        case queen:
                            materialVal += queenVal;
                            break;
                        case pawn:
                            materialVal += pawnVal;
                            break;
                    }*/
                    materialVal += piece.getType().getValue();
                }
            }
        }
        return materialVal;
    }

    public static int dfs(int depth, int alpha, int beta, BestMove bestMove) {
         if(depth == 0) {
             return evaluate();
        }
        List<Move> legalMoves = gameManager.getAllLegalMoves();
         // sort all the legal moves in prioritized order
        prioritizeGoodMoves(legalMoves);
        if(legalMoves.size() == 0) {
            if(gameManager.getBoard().isInCheck(gameManager.getPlayer_turn())) {
                return Integer.MIN_VALUE;
            }
            return 0;
        }
        for(Move move : legalMoves) {
            gameManager.MakeMove(move);
            /*System.out.println("make move from " +  move.getFromPos() + " to " + move.getToPos());*/
            int evaluate = -dfs(depth - 1, -beta, -alpha, bestMove);
            evaluateCount++;
            /*System.out.println("evaluated: " + evaluateCount);
            System.out.println("evaluate = " + alpha);*/
            gameManager.UnmakeMove();
            // if the current mode is so good that the opponent will never allow it => pruning
            if(evaluate >= beta) {
                return beta;
            }
            // update alpha to reflect the best score
            if(evaluate > alpha) {
                alpha = evaluate;
                bestMove.move = move;
                System.out.println("best Move found: " + move);
            }
        }
        return alpha;
    }
    private static void prioritizeGoodMoves(List<Move> moves) {
        for (Move move : moves) {
            int moveScore = 0;
            Position moveFrom = move.getFromPos();
            Position moveTo = move.getToPos();

            Piece movePiece = gameManager.getBoard().getPiece(moveFrom);
            Piece capturePiece = gameManager.getBoard().getPiece(moveTo);

            // Prioritize capturing opponent's most valuable pieces with the least value piece
            if (capturePiece != null) {
                moveScore = 10 * getPieceValue(capturePiece) - getPieceValue(movePiece);
            }

            // Prioritize promoting pawn
            /*if (isPromoteMove(move)) {
                moveScore += 100; // Arbitrary high score for promotions
            }*/

            // Penalize moves to squares attacked by the opponent
            /*if (isSquareAttacked(moveTo)) {
                moveScore -= 50; // Arbitrary penalty for unsafe moves
            }*/

            // Assign the calculated score to the move
            move.setMoveScore(moveScore);
        }

        // Sort moves based on moveScore (descending order)
        moves.sort((move1, move2) -> Integer.compare(move2.getMoveScore(), move1.getMoveScore()));
    }
    private static int calculateMoveScore(Move move) {
        int moveScore = 0;
        Position moveFrom = move.getFromPos();
        Position moveTo = move.getToPos();

        Piece movePiece = gameManager.getBoard().getPiece(moveFrom);
        Piece capturePiece = gameManager.getBoard().getPiece(moveTo);

        // Prioritize capturing opponent's most valuable pieces with the least valuable piece
        if (capturePiece != null) {
            moveScore = 10 * getPieceValue(capturePiece) - getPieceValue(movePiece);
        }
        // Prioritize promoting pawns
        if (isPromoteMove(move)) {
            moveScore += 100;
        }
        // Penalize moves to squares attacked by the opponent
        /*if (isSquareAttacked(moveTo)) {
            moveScore -= 50; // Arbitrary penalty for unsafe moves
        }*/
        return moveScore;
    }


    private static int getPieceValue(Piece piece) {
        return piece.getType().getValue();
    }
    private static boolean isPromoteMove(Move move) {
        return move.getType().equals(MoveType.PawnPromote);
    }


    // useless
    /*public static void printBoard() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Piece piece = gameManager.getBoard().getPiece(new Position(i,j));
                if(piece != null) {
                    // Get first character of Player and PieceType enums
                    char colorChar = piece.getColor().name().charAt(0); // Assuming getPlayer() exists
                    char typeChar = piece.getType().name().charAt(0);
                    System.out.print(colorChar + "" + typeChar + " ");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
    }*/




    
}
