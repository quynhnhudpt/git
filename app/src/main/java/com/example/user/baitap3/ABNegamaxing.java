package com.example.user.baitap3;

import android.util.Log;

/**
 * Created by User on 4/14/2018.
 */

public class ABNegamaxing {

    public Record negamax(Chessboard chessBoard,int currentDept, int maxDept, int alpha, int beta){
        Move bestMove = null;
        int bestScore;

        if(chessBoard.isGameOver() || currentDept == maxDept){
            Log.i("EVALUATE",String.valueOf(chessBoard.evaluate()));
            return  new Record(null,chessBoard.evaluate());
        }

        bestScore = Integer.MIN_VALUE;
        for (Move move:chessBoard.getMove()){
            Chessboard newChess = new Chessboard(chessBoard.getContext(),chessBoard.getBitmapWidth(),chessBoard.getBitmapHeight(),chessBoard.getColQty(),chessBoard.getRowQty());

            newChess.setBoard(chessBoard.getNewBoard());
            newChess.setPlayer(chessBoard.getPlayer());
            newChess.makeMove(move);

            Record record = negamax(
                    newChess,
                    currentDept++,
                    maxDept,
                    -beta,
                    -Math.max(alpha,bestScore)
            );

            int currentScore = - record.getScore();//do dang ktra o thang khac,

            if(currentScore > bestScore){
                bestScore = currentScore;
                bestMove = move;
                if(bestScore >= beta || currentScore <= alpha){
                    new Record(bestMove,bestScore);
                }
            }
        }
        return new Record(bestMove,bestScore);
    }
}

