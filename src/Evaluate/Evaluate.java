package Evaluate;

import Logic.Board;
import Logic.GameManager;
import Move.*;
import Piece.Piece;
import Piece.PieceColor;
import Piece.PieceType;
import Position.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evaluate {
    private static final int posInfinity = 9999999;
    private static final int negInfinity = -posInfinity;
    private static final int pawnValue   = 100;
    private static final int knightValue = 320;
    private static final int bishopValue = 330;
    private static final int rookValue   = 500;
    private static final int queenValue  = 900;
    private static final int kingValue  = 9999;

    public static GameManager gameManager;
    public static int evaluateCount = 0;

    private static Map<Long, Integer> transpositionTable = new HashMap<>(); // Memoization

    private static int evaluate() {
        int playerTurn = gameManager.getPlayer_turn().equals(PieceColor.white) ? 1 : -1;

        int whiteVal = CountMaterialValue(PieceColor.white);
        int blackVal = CountMaterialValue(PieceColor.black);

        int evaluate = (whiteVal - blackVal)*playerTurn;
        System.out.println(evaluate);
        return evaluate;
    }

    private static int CountMaterialValue(PieceColor color) {
        int materialVal = 0;

        List<Position> occupiedPos = color.equals(PieceColor.white) ? gameManager.getBoard().getOccupiedPosWhiteList() : gameManager.getBoard().getOccupiedPosBlackList();

        int[][] pawnPos, knightPos, bishopPos, rookPos, queenPos, kingPos;
        int pawnCount, opponentPawnCount, bishopCount, knightCount, rookCount, queenCount;
        if(color == PieceColor.white) {
            pawnPos = pawnPosWhite;
            knightPos = knightPosWhite;
            bishopPos = bishopPosWhite;
            rookPos = rookPosWhite;
            queenPos = queenPosWhite;
            // count the piece
            pawnCount = gameManager.getBoard().countOfPiece(PieceColor.white, PieceType.pawn);
            opponentPawnCount = gameManager.getBoard().countOfPiece(PieceColor.black, PieceType.pawn);
            bishopCount = gameManager.getBoard().countOfPiece(PieceColor.white, PieceType.bishop);
            knightCount = gameManager.getBoard().countOfPiece(PieceColor.white, PieceType.knight);
            rookCount = gameManager.getBoard().countOfPiece(PieceColor.white, PieceType.rook);
            queenCount = gameManager.getBoard().countOfPiece(PieceColor.white, PieceType.queen);

        } else {
            pawnPos = pawnPosBlack;
            knightPos = knightPosBlack;
            bishopPos = bishopPosBlack;
            rookPos = rookPosBlack;
            queenPos = queenPosBlack;
            // count the piece
            pawnCount = gameManager.getBoard().countOfPiece(PieceColor.black, PieceType.pawn);
            opponentPawnCount = gameManager.getBoard().countOfPiece(PieceColor.white, PieceType.pawn);
            bishopCount = gameManager.getBoard().countOfPiece(PieceColor.black, PieceType.bishop);
            knightCount = gameManager.getBoard().countOfPiece(PieceColor.black, PieceType.knight);
            rookCount = gameManager.getBoard().countOfPiece(PieceColor.black, PieceType.rook);
            queenCount = gameManager.getBoard().countOfPiece(PieceColor.black, PieceType.queen);
        }

        for(Position pos : occupiedPos) {
            Piece piece = gameManager.getBoard().getPiece(pos);
            PieceType pieceType = piece.getType();
            PieceColor pieceColor = piece.getColor();

            if(piece != null && color.equals(pieceColor)) {
                switch (pieceType) {
                    case pawn:
                        materialVal += pawnValue + pawnPos[pos.getRow()][pos.getCol()];
                        break;
                    case knight:
                        materialVal += knightValue + knightPos[pos.getRow()][pos.getCol()] + knightPawnAdjustment[pawnCount];
                        break;
                    case bishop:
                        materialVal += bishopValue + bishopPos[pos.getRow()][pos.getCol()];
                        break;
                    case rook:
                        materialVal += rookValue + rookPos[pos.getRow()][pos.getCol()] + rookPawnAdjustment[pawnCount];
                        break;
                    case queen:
                        materialVal += queenValue + queenPos[pos.getRow()][pos.getCol()];
                        break;
                }
            }
        }
        return materialVal;
    }
    public static Move startSearch(int maxDepth) {
        BestMove bestMove = new BestMove();
        //Board board = gameManager.getBoard().copy();
        int alpha = negInfinity;
        int beta = posInfinity;

        long zobristHash = gameManager.getBoard().getHash();
        dfs(maxDepth, alpha, beta, bestMove, zobristHash);

        System.out.println("evaluateCount = " + evaluateCount);
        System.out.println("evaluateCount = " + evaluateCount);

        System.out.println(bestMove);
        return bestMove.move;
    }
    public static int dfs(int depth, int alpha, int beta, BestMove bestMove, long zobristHash) {
         if(depth == 0) {
             return evaluate();
            }
        evaluateCount++;
         // the exact identical board pos reached twice => return the evaluation
        if (transpositionTable.containsKey(zobristHash)) {
            System.out.println("duplicative chess board found " + zobristHash);
            return transpositionTable.get(zobristHash);
        }
        List<Move> legalMoves = gameManager.getAllLegalMoves();
         // sort all the legal moves in prioritized order
        prioritizeGoodMoves(legalMoves);
        if(legalMoves.isEmpty()) {
            if(gameManager.getBoard().isInCheck(gameManager.getPlayer_turn())) {
                return Integer.MIN_VALUE;
            }
            return 0;
        }
        int originalAlpha = alpha;
        for(Move move : legalMoves) {
            long updatedHash = ZobristHashing.updateHash(zobristHash, move, gameManager.getBoard().getBoard(), gameManager.getPlayer_turn().equals(PieceColor.white));
            gameManager.MakeMove(move);
            int evaluate = -dfs(depth - 1, -beta, -alpha, bestMove, updatedHash);

            gameManager.UnmakeMove();
            // if the current mode is so good that the opponent will never allow it => pruning
            if(evaluate >= beta) {
                return beta;
            }
            // update alpha to reflect the best score
            if(evaluate > alpha) {
                alpha = evaluate;
                bestMove.move = move;
                System.out.println("best Move found: " + move + "alpha: " + alpha);
                System.out.println("evaluate count: " + evaluateCount);
            }
        }
        // Store in transposition table
        transpositionTable.put(zobristHash, (alpha == originalAlpha) ? alpha : originalAlpha);
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
                moveScore = getPieceValue(capturePiece) - getPieceValue(movePiece);
            }

            // Prioritize promoting pawn
            if (isPromoteMove(move)) {
                PromotePawnMove promoteMove = (PromotePawnMove) move;
                Piece promotePiece = promoteMove.getPiece();
                int promotionValue = getPieceValue(promotePiece);

                if (isSquareAttacked(move)) {
                    // Penalize dangerous promotion squares
                    moveScore -= getPieceValue(movePiece); // Reduce value of promotion if square is attacked
                } else {
                    // Full value for safe promotion
                    moveScore += promotionValue; // Weighted multiplier for safe promotion
                }
            }

            // Penalize moves to squares attacked by the opponent pawn
            if (isSquareAttacked(move)) {
                moveScore -= getPieceValue(movePiece);
            }

            // Assign the calculated score to the move
            move.setMoveScore(moveScore);
        }
        // Sort moves based on moveScore (descending order)
        moves.sort((move1, move2) -> Integer.compare(move2.getMoveScore(), move1.getMoveScore()));
    }


    private static int getPieceValue(Piece piece) {
        return switch (piece.getType()) {
            case knight -> knightValue;
            case pawn -> pawnValue;
            case queen -> queenValue;
            case bishop -> bishopValue;
            case rook -> rookValue;
            case king -> kingValue;
        };
    }


    private static boolean isPromoteMove(Move move) {
        return move.getType().equals(MoveType.PawnPromote);
    }
    private static boolean isSquareAttacked(Move move) {
        Piece piece = gameManager.getBoard().getPiece(move.getFromPos());
        return gameManager.getBoard().isInCheckAt(move.getToPos(), piece.getColor());
    }


    private static final int pawnPosWhite[][] =
            {
                    {0,   0,   0,   0,   0,   0,  0,  0},
                    {5,  10,  10, -20, -20,  10, 10,  5},
                    {5,  -5, -10,   0,   0, -10, -5,  5},
                    {0,   0,   0,  20,  20,   0,  0,  0},
                    {5,   5,  10,  25,  25,  10,  5,  5},
                    {10, 10,  20,  30,  30,  20, 10, 10},
                    {50, 50,  50,  50,  50,  50, 50, 50},
                    {0,   0,   0,   0,   0,   0,  0,  0}
            };

    private static final int pawnPosBlack[][] =
            {
                    {0,   0,   0,   0,   0,   0,  0,  0},
                    {50, 50,  50,  50,  50,  50, 50, 50},
                    {10, 10,  20,  30,  30,  20, 10, 10},
                    {5,   5,  10,  25,  25,  10,  5,  5},
                    {0,   0,   0,  20,  20,   0,  0,  0},
                    {5,  -5, -10,   0,   0, -10, -5,  5},
                    {5,  10,  10, -20, -20,  10, 10,  5},
                    {0,   0,   0,   0,   0,   0,  0,  0}
            };

    private static final int knightPosWhite[][] =
            {
                    {-50, -40, -30, -30, -30, -30, -40, -50},
                    {-40, -20,   0,   5,   5,   0, -20, -40},
                    {-30,   5,  10,  15,  15,  10,   5, -30},
                    {-30,   0,  15,  20,  20,  15,   0, -30},
                    {-30,   5,  15,  20,  20,  15,   5, -30},
                    {-30,   0,  10,  15,  15,  10,   0, -30},
                    {-40, -20,   0,   0,   0,   0, -20, -40},
                    {-50, -40, -30, -30, -30, -30, -40, -50}
            };

    private static final int knightPosBlack[][] =
            {
                    {-50, -40, -30, -30, -30, -30, -40, -50},
                    {-40, -20,   0,   0,   0,   0, -20, -40},
                    {-30,   0,  10,  15,  15,  10,   0, -30},
                    {-30,   5,  15,  20,  20,  15,   5, -30},
                    {-30,   0,  15,  20,  20,  15,   0, -30},
                    {-30,   5,  10,  15,  15,  10,   5, -30},
                    {-40, -20,   0,   5,   5,   0, -20, -40},
                    {-50, -40, -30, -30, -30, -30, -40, -50}
            };

    private static final int bishopPosWhite[][] =
            {
                    {-20, -10, -10, -10, -10, -10, -10, -20},
                    {-10,   5,   0,   0,   0,   0,   5, -10},
                    {-10,  10,  10,  10,  10,  10,  10, -10},
                    {-10,   0,  10,  10,  10,  10,   0, -10},
                    {-10,   5,   5,  10,  10,   5,   5, -10},
                    {-10,   0,   5,  10,  10,   5,   0, -10},
                    {-10,   0,   0,   0,   0,   0,   0, -10},
                    {-20, -10, -10, -10, -10, -10, -10, -20}
            };

    private static final int bishopPosBlack[][] =
            {
                    {-20, -10, -10, -10, -10, -10, -10, -20},
                    {-10,   0,   0,   0,   0,   0,   0, -10},
                    {-10,   0,   5,  10,  10,   5,   0, -10},
                    {-10,   5,   5,  10,  10,   5,   5, -10},
                    {-10,   0,  10,  10,  10,  10,   0, -10},
                    {-10,  10,  10,  10,  10,  10,  10, -10},
                    {-10,   5,   0,   0,   0,   0,   5, -10},
                    {-20, -10, -10, -10, -10, -10, -10, -20}
            };

    private static final int rookPosWhite[][] =
            {
                    {0,  0,  0,  5,  5,  0,  0,  0},
                    {-5, 0,  0,  0,  0,  0,  0, -5},
                    {-5, 0,  0,  0,  0,  0,  0, -5},
                    {-5, 0,  0,  0,  0,  0,  0, -5},
                    {-5, 0,  0,  0,  0,  0,  0, -5},
                    {-5, 0,  0,  0,  0,  0,  0, -5},
                    {5, 10, 10, 10, 10, 10, 10,  5},
                    {0,  5,  5,  5,  5,  5,  5,  0}
            };

    private static final int rookPosBlack[][] =
            {
                    {0,  5,  5,  5,  5,  5,  5,  0},
                    {5, 10, 10, 10, 10, 10, 10,  5},
                    {-5, 0,  0,  0,  0,  0,  0, -5},
                    {-5, 0,  0,  0,  0,  0,  0, -5},
                    {-5, 0,  0,  0,  0,  0,  0, -5},
                    {-5, 0,  0,  0,  0,  0,  0, -5},
                    {-5, 0,  0,  0,  0,  0,  0, -5},
                    {0,  0,  0,  5,  5,  0,  0,  0}
            };

    private static final int queenPosWhite[][] =
            {
                    {-20, -10, -10, -5, -5, -10, -10, -20},
                    {-10,   0,   5,  0,  0,   0,   0, -10},
                    {-10,   5,   5,  5,  5,   5,   0, -10},
                    {0,     0,   5,  5,  5,   5,   0, -5},
                    {-5,    0,   5,  5,  5,   5,   0, -5},
                    {-10,   0,   5,  5,  5,   5,   0, -10},
                    {-10,   0,   0,  0,  0,   0,   0, -10},
                    {-20, -10, -10, -5, -5, -10, -10, -20}
            };

    private static final int queenPosBlack[][] =
            {
                    {-20, -10, -10, -5, -5, -10, -10, -20},
                    {-10,   0,   0,  0,  0,   0,   0, -10},
                    {-10,   0,   5,  5,  5,   5,   0, -10},
                    {-5,    0,   5,  5,  5,   5,   0, -5},
                    {0,     0,   5,  5,  5,   5,   0, -5},
                    {-10,   5,   5,  5,  5,   5,   0, -10},
                    {-10,   0,   5,  0,  0,   0,   0, -10},
                    {-20, -10, -10, -5, -5, -10, -10, -20}
            };

    private static final int kingPosWhite[][] =
            {
                    {20,   30,  10,   0,   0,  10,  30,  20},
                    {20,   20,   0,   0,   0,   0,  20,  20},
                    {-10, -20, -20, -20, -20, -20, -20, -10},
                    {-20, -30, -30, -40, -40, -30, -30, -20},
                    {-30, -40, -40, -50, -50, -40, -40, -30},
                    {-30, -40, -40, -50, -50, -40, -40, -30},
                    {-30, -40, -40, -50, -50, -40, -40, -30},
                    {-30, -40, -40, -50, -50, -40, -40, -30}
            };

    private static final int kingPosBlack[][] =
            {
                    {-30, -40, -40, -50, -50, -40, -40, -30},
                    {-30, -40, -40, -50, -50, -40, -40, -30},
                    {-30, -40, -40, -50, -50, -40, -40, -30},
                    {-30, -40, -40, -50, -50, -40, -40, -30},
                    {-20, -30, -30, -40, -40, -30, -30, -20},
                    {-10, -20, -20, -20, -20, -20, -20, -10},
                    {20,   20,   0,   0,   0,   0,  20,  20},
                    {20,   30,  10,   0,   0,  10,  30,  20}
            };

    // Bonuses depending on how many pawns are left
    private static final int[] knightPawnAdjustment =
            {-30, -20, -15, -10, -5, 0, 5, 10, 15};

    private static final int[] rookPawnAdjustment =
            {25, 20, 15, 10, 5, 0, -5, -10, -15};

    private static final int[] dualBishopPawnAdjustment =
            {40, 40, 35, 30, 25, 20, 20, 15, 15};



    
}
