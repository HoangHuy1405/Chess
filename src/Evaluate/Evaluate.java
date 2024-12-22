package Evaluate;

import GameLogic.Board;

import GameLogic.Move.*;
import Piece.PieceColor;
import Piece.PieceTracker;
import Piece.PieceType;
import Piece.Pieces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static Piece.Pieces.*;

public class Evaluate {
    private static final int posInfinity = 9999999;
    private static final int negInfinity = -posInfinity;
    private static final int pawnValue   = 100;
    private static final int knightValue = 320;
    private static final int bishopValue = 330;
    private static final int rookValue   = 500;
    private static final int queenValue  = 900;
    private static final int kingValue  = 9999;


    public static Move bestMove;
    public static Board copiedBoard;

    private static int nodeNum = 0;
    private static int pruneNum = 0;

    private static int MAX_DEPTH;

    private static Map<Long, Integer> transpositionTable = new HashMap<>(); // Memoization

    private static int evaluate() {
        int playerTurn = copiedBoard.checkTurn(PieceColor.White) ? 1 : -1;

        int whiteVal = CountMaterialValue(PieceColor.White);
        int blackVal = CountMaterialValue(PieceColor.Black);
        int evaluate = (whiteVal - blackVal)*playerTurn;
        System.out.println("evaluate = " +  evaluate);
        return evaluate;
    }

    private static int CountMaterialValue(PieceColor color) {
        int materialVal = 0;
        List<Integer> occupiedPos = copiedBoard.getAllPiecePos(color);

        int[] pawnPos, knightPos, bishopPos, rookPos, queenPos, kingPos;
        int pawnCount;
        if(color == PieceColor.White) {
            pawnPos = pawnPosWhite;
            knightPos = knightPosWhite;
            bishopPos = bishopPosWhite;
            rookPos = rookPosWhite;
            queenPos = queenPosWhite;
            kingPos = kingPosWhite;
            pawnCount = copiedBoard.getPieceCount(Pieces.WhitePawn);

        } else {
            pawnPos = pawnPosBlack;
            knightPos = knightPosBlack;
            bishopPos = bishopPosBlack;
            rookPos = rookPosBlack;
            queenPos = queenPosBlack;
            kingPos = kingPosBlack;
            pawnCount = copiedBoard.getPieceCount(Pieces.BlackPawn);
        }

        for(Integer pos : occupiedPos) {
            int piece = copiedBoard.getPiece(pos);
            PieceType pieceType = Pieces.getType(piece);
            PieceColor pieceColor = Pieces.getColor(piece);

            if(color.equals(pieceColor)) {
                switch (pieceType) {
                    case Pawn:
                        materialVal += pawnValue + pawnPos[pos];
                        break;
                    case Knight:
                        materialVal += knightValue + knightPos[pos] + knightPawnAdjustment[pawnCount];
                        break;
                    case Bishop:
                        materialVal += bishopValue + bishopPos[pos];
                        break;
                    case Rook:
                        materialVal += rookValue + rookPos[pos] + rookPawnAdjustment[pawnCount];
                        break;
                    case Queen:
                        materialVal += queenValue + queenPos[pos];
                        break;
                    case King:
                        materialVal += kingPos[pos];
                }
            }
        }
        return materialVal;
    }
    public static Move startSearch(int maxDepth, Board board)  {
        int alpha = negInfinity;
        int beta = posInfinity;

        copiedBoard = board.copy();
        MAX_DEPTH = maxDepth;
        dfs(maxDepth, alpha, beta);

        System.out.println("\nNodes = " + nodeNum);
        System.out.println("best move = " + bestMove);

        return bestMove;
    }
    public static int dfs(int depth, int alpha, int beta) {
        copiedBoard.updateEndGame();
        if(copiedBoard.isEnd()) {
            return 999999;
        }
         if(depth == 0) {
             nodeNum++;
             return evaluate();
        }
         // the exact identical board pos reached twice => return the evaluation
        /*if (transpositionTable.containsKey(zobristHash)) {
            System.out.println("duplicative chess board found " + zobristHash);
            return transpositionTable.get(zobristHash);
        }*/
        List<Move> legalMoves = MoveGenerator.getAllLegalMoves(copiedBoard);
        // sort all the legal moves in prioritized order
        prioritizeGoodMoves(legalMoves);
        if(legalMoves.isEmpty()) {
            if(copiedBoard.isInCheck()) {
                return Integer.MIN_VALUE;
            }
            return 0;
        }
        for(Move move : legalMoves) {
            //long updatedHash = ZobristHashing.updateHash(zobristHash, move, copiedBoard);
            copiedBoard.makeMove(move);
            int evaluate = -dfs(depth - 1, -beta, -alpha);
            copiedBoard.unmakeMove();

            // if the current mode is so good that the opponent will never allow it => pruning
            if(evaluate >= beta) {
                pruneNum++;
                return beta;
            }
            // update alpha to reflect the best score
            if(evaluate > alpha) {
                alpha = evaluate;
                System.out.println(copiedBoard.getPiece(move.getFromPos()));
                /*if(getColor(copiedBoard.getPiece(move.getFromPos())) == PieceColor.White) {
                    printBoard();
                }*/
                if (depth == MAX_DEPTH) { // Update bestMove only at root level
                    bestMove = move;
                }
                System.out.println("\nbest Move found: " + move + " alpha: " + alpha + " evaluation = " + evaluate);
            }
        }
        // Store in transposition table
        // transpositionTable.put(zobristHash, (alpha != originalAlpha) ? alpha : originalAlpha);
        return alpha;
    }
    public static void printBoard() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                int piece = copiedBoard.getPiece(i*8 + j);
                switch(piece) {
                    case 12 -> System.out.print("br ");
                    case 10 -> System.out.print("bn ");
                    case 11 -> System.out.print("bb ");
                    case 13 -> System.out.print("bq ");
                    case 14 -> System.out.print("bk ");
                    case 9 -> System.out.print("bp ");

                    case 4 -> System.out.print("wr ");
                    case 2 -> System.out.print("wn ");
                    case 5 -> System.out.print("wq ");
                    case 3 -> System.out.print("wb ");
                    case 6 -> System.out.print("wk ");
                    case 1 -> System.out.print("wp ");

                    case 0 -> System.out.print("   ");
                }
            }
            System.out.println();
        }

        for(int i = 0; i < 64; i++) {
            System.out.print(copiedBoard.getPiece(i) + " ");
        }
    }

    private static void prioritizeGoodMoves(List<Move> moves) {
        for (Move move : moves) {
            int moveScore = 0;
            int moveFrom = move.getFromPos();
            int moveTo = move.getToPos();

            int movePiece = copiedBoard.getPiece(moveFrom);
            int capturePiece = copiedBoard.getPiece(moveTo);

            // Prioritize capturing opponent's most valuable pieces with the least value piece
            if (capturePiece != 0) {
                // if a king move to capture, change the value to 0
                if(movePiece == BlackKing || movePiece == WhiteKing) moveScore = getPieceValue(capturePiece);
                else moveScore = getPieceValue(capturePiece) - getPieceValue(movePiece);
            }

            // Prioritize promoting pawn
            if (isPromoteMove(move)) {
                Promote promoteMove = (Promote) move;
                int promotePiece = promoteMove.getPiecePromoted();
                int promotionValue = getPieceValue(promotePiece);

                if (isSquareAttacked(move)) {

                    moveScore -= promotionValue; // Penalize dangerous promotion squares
                } else {
                    moveScore += promotionValue; // Full value for safe promotion
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
    // use for debugging
    private static void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds); // Pause for the given time
        } catch (InterruptedException e) {
            System.err.println("Sleep was interrupted!");
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }


    private static int getPieceValue(int piece) {
        PieceType type = Pieces.getType(piece);
        return switch (type) {
            case None -> 0;
            case Knight -> knightValue;
            case Pawn -> pawnValue;
            case Queen -> queenValue;
            case Bishop -> bishopValue;
            case Rook -> rookValue;
            case King -> kingValue;
        };
    }


    private static boolean isPromoteMove(Move move) {
        return move instanceof Promote;
    }
    private static boolean isSquareAttacked(Move move) {
        int movePiece = copiedBoard.getPiece(move.getFromPos());
        PieceColor currentColor = Pieces.getColor(movePiece);
        return copiedBoard.isInAttackAt(move.getFromPos(), currentColor);

    }


    private static final int pawnPosBlack[] =
            {
                    0,   0,   0,   0,   0,   0,  0,  0,
                    5,  10,  10, -20, -20,  10, 10,  5,
                    5,  -5, -10,   0,   0, -10, -5,  5,
                    0,   0,   0,  20,  20,   0,  0,  0,
                    5,   5,  10,  25,  25,  10,  5,  5,
                    10, 10,  20,  30,  30,  20, 10, 10,
                    50, 50,  50,  50,  50,  50, 50, 50,
                    0,   0,   0,   0,   0,   0,  0,  0
            };

    private static final int pawnPosWhite[] =
            {
                    0,   0,   0,   0,   0,   0,  0,  0,
                    50, 50,  50,  50,  50,  50, 50, 50,
                    10, 10,  20,  30,  30,  20, 10, 10,
                    5,   5,  10,  25,  25,  10,  5,  5,
                    0,   0,   0,  20,  20,   0,  0,  0,
                    5,  -5, -10,   0,   0, -10, -5,  5,
                    5,  10,  10, -20, -20,  10, 10,  5,
                    0,   0,   0,   0,   0,   0,  0,  0
            };

    private static final int knightPosWhite[] =
            {
                    -50, -40, -30, -30, -30, -30, -40, -50,
                    -40, -20,   0,   5,   5,   0, -20, -40,
                    -30,   5,  10,  15,  15,  10,   5, -30,
                    -30,   0,  15,  20,  20,  15,   0, -30,
                    -30,   5,  15,  20,  20,  15,   5, -30,
                    -30,   0,  10,  15,  15,  10,   0, -30,
                    -40, -20,   0,   0,   0,   0, -20, -40,
                    -50, -40, -30, -30, -30, -30, -40, -50
            };

    private static final int knightPosBlack[] =
            {
                    -50, -40, -30, -30, -30, -30, -40, -50,
                    -40, -20,   0,   0,   0,   0, -20, -40,
                    -30,   0,  10,  15,  15,  10,   0, -30,
                    -30,   5,  15,  20,  20,  15,   5, -30,
                    -30,   0,  15,  20,  20,  15,   0, -30,
                    -30,   5,  10,  15,  15,  10,   5, -30,
                    -40, -20,   0,   5,   5,   0, -20, -40,
                    -50, -40, -30, -30, -30, -30, -40, -50
            };

    private static final int bishopPosWhite[] =
            {
                    -20, -10, -10, -10, -10, -10, -10, -20,
                    -10,   5,   0,   0,   0,   0,   5, -10,
                    -10,  10,  10,  10,  10,  10,  10, -10,
                    -10,   0,  10,  10,  10,  10,   0, -10,
                    -10,   5,   5,  10,  10,   5,   5, -10,
                    -10,   0,   5,  10,  10,   5,   0, -10,
                    -10,   0,   0,   0,   0,   0,   0, -10,
                    -20, -10, -10, -10, -10, -10, -10, -20
            };

    private static final int bishopPosBlack[] =
            {
                    -20, -10, -10, -10, -10, -10, -10, -20,
                    -10,   0,   0,   0,   0,   0,   0, -10,
                    -10,   0,   5,  10,  10,   5,   0, -10,
                    -10,   5,   5,  10,  10,   5,   5, -10,
                    -10,   0,  10,  10,  10,  10,   0, -10,
                    -10,  10,  10,  10,  10,  10,  10, -10,
                    -10,   5,   0,   0,   0,   0,   5, -10,
                    -20, -10, -10, -10, -10, -10, -10, -20
            };

    private static final int rookPosWhite[] =
            {
                    0,  0,  0,  5,  5,  0,  0,  0,
                    -5, 0,  0,  0,  0,  0,  0, -5,
                    -5, 0,  0,  0,  0,  0,  0, -5,
                    -5, 0,  0,  0,  0,  0,  0, -5,
                    -5, 0,  0,  0,  0,  0,  0, -5,
                    -5, 0,  0,  0,  0,  0,  0, -5,
                    5, 10, 10, 10, 10, 10, 10,  5,
                    0,  5,  5,  5,  5,  5,  5,  0
            };

    private static final int rookPosBlack[] =
            {
                    0,  5,  5,  5,  5,  5,  5,  0,
                    5, 10, 10, 10, 10, 10, 10,  5,
                    -5, 0,  0,  0,  0,  0,  0, -5,
                    -5, 0,  0,  0,  0,  0,  0, -5,
                    -5, 0,  0,  0,  0,  0,  0, -5,
                    -5, 0,  0,  0,  0,  0,  0, -5,
                    -5, 0,  0,  0,  0,  0,  0, -5,
                    0,  0,  0,  5,  5,  0,  0,  0
            };

    private static final int queenPosWhite[] =
            {
                    -20, -10, -10, -5, -5, -10, -10, -20,
                    -10,   0,   5,  0,  0,   0,   0, -10,
                    -10,   5,   5,  5,  5,   5,   0, -1,
                    0,     0,   5,  5,  5,   5,   0, -5,
                    -5,    0,   5,  5,  5,   5,   0, -5,
                    -10,   0,   5,  5,  5,   5,   0, -10,
                    -10,   0,   0,  0,  0,   0,   0, -10,
                    -20, -10, -10, -5, -5, -10, -10, -20
            };

    private static final int queenPosBlack[] =
            {
                    -20, -10, -10, -5, -5, -10, -10, -20,
                    -10,   0,   0,  0,  0,   0,   0, -10,
                    -10,   0,   5,  5,  5,   5,   0, -10,
                    -5,    0,   5,  5,  5,   5,   0, -5,
                    0,     0,   5,  5,  5,   5,   0, -5,
                    -10,   5,   5,  5,  5,   5,   0, -10,
                    -10,   0,   5,  0,  0,   0,   0, -10,
                    -20, -10, -10, -5, -5, -10, -10, -20
            };

    private static final int kingPosWhite[] =
            {
                    20,   30,  10,   0,   0,  10,  30,  20,
                    20,   20,   0,   0,   0,   0,  20,  20,
                    -10, -20, -20, -20, -20, -20, -20, -10,
                    -20, -30, -30, -40, -40, -30, -30, -20,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30
            };

    private static final int kingPosBlack[] =
            {
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -20, -30, -30, -40, -40, -30, -30, -20,
                    -10, -20, -20, -20, -20, -20, -20, -10,
                    20,   20,   0,   0,   0,   0,  20,  20,
                    20,   30,  10,   0,   0,  10,  30,  20
            };

    // Bonuses depending on how many pawns are left
    private static final int[] knightPawnAdjustment =
            {-30, -20, -15, -10, -5, 0, 5, 10, 15};

    private static final int[] rookPawnAdjustment =
            {25, 20, 15, 10, 5, 0, -5, -10, -15};

    private static final int[] dualBishopPawnAdjustment =
            {40, 40, 35, 30, 25, 20, 20, 15, 15};
}
