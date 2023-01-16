package com.thg.accelerator23.connectn.ai.rosseleanor;

import com.thehutgroup.accelerator.connectn.player.Board;
import com.thehutgroup.accelerator.connectn.player.Counter;
import com.thehutgroup.accelerator.connectn.player.InvalidMoveException;
import com.thg.accelerator23.connectn.ai.rosseleanor.analysis.BoardAnalyser;
import com.thg.accelerator23.connectn.ai.rosseleanor.analysis.GameState;
import com.thg.accelerator23.connectn.ai.rosseleanor.model.MinimaxMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiniMaxAI implements AI {
    private final int maxDepth;
    private final Counter maximisingCounter;
    private final Counter minimisingCounter;
    GameState gameState;
    MinimaxMove minimaxMove;
    private BoardAnalyser boardAnalyser;
    private Board board;


    public MiniMaxAI(int maxDepth, Counter maximisingCounter) {
        this.maxDepth = maxDepth;
        this.maximisingCounter = maximisingCounter;
        this.minimisingCounter = maximisingCounter.getOther();
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setBoardAnalyser(BoardAnalyser boardAnalyser) {
        this.boardAnalyser = boardAnalyser;
    }


    @Override
    public int getMove() throws InvalidMoveException {

        if (boardAnalyser.isBoardFull(board)) {
            throw new RuntimeException("Board is full!");
        }

        int bestMove = Math.floorDiv(board.getConfig().getWidth(), 2);

        if (boardAnalyser.isBoardEmpty(board)) {
            return bestMove;
        }
        minimaxMove = minimax(maxDepth, maximisingCounter, board);

        return minimaxMove.getColumn();
    }


    protected List<Board> getChildren(Counter counter) throws InvalidMoveException {
        List<Board> children = new ArrayList<>();
        for (int col = 0; col < board.getConfig().getWidth(); col++) {
            if (!boardAnalyser.isColumnFull(board, col)) {
                Board child = new Board(board, col, counter);
                children.add(child);
            }
        }
        return children;
    }


    public int randomMove(Board board) {
        RandomBot randomMove = new RandomBot(maximisingCounter);
        int move = randomMove.makeMove(board);
        return move;
    }


    public MinimaxMove minimax(int depth, Counter counter, Board board) throws InvalidMoveException {

        gameState = boardAnalyser.calculateGameState(board);

        if (gameState.isEnd() || depth == 0) {
            if (gameState.isEnd()){
                if (gameState.getWinner() == maximisingCounter) {
                    return new MinimaxMove(randomMove(board), 1000000);
                } else if (gameState.getWinner() == minimisingCounter) {
                    return new MinimaxMove(randomMove(board), -1000000);
                } else if (gameState.isDraw()) {
                    return new MinimaxMove(randomMove(board), 0);
                }
            }
            else {
                return new MinimaxMove(randomMove(board), boardAnalyser.analyse(board, maximisingCounter));
            }
        }

        List<Board> children;
        if (counter == maximisingCounter) {
            int maxScore = Integer.MIN_VALUE;
            int maxColumnChoice = randomMove(board);

            children = getChildren(maximisingCounter);
            int column = 0;

            for (Board child : children) {
                int score = minimax(depth - 1, minimisingCounter, child).getScore();
                if (maxScore < score) {
                    maxScore = score;
                    maxColumnChoice = column;
                }
                if (maxScore == score){
                    if (new Random().nextInt(2) == 0 ){
                        maxColumnChoice = column;
;                    }
                }
                column++;
            }
            return new MinimaxMove(maxColumnChoice, maxScore);

        } else {
            int minScore = Integer.MAX_VALUE;
            int minColumnChoice = randomMove(board);
            children = getChildren(minimisingCounter);
            int column = 0;

            for (Board child : children) {
                int score = minimax(depth - 1, maximisingCounter, child).getScore();
                if (minScore > score) {
                    minScore = score;
                    minColumnChoice = column;
                }
                column++;
            }

            return new MinimaxMove(minColumnChoice, minScore);
        }
    }
}
