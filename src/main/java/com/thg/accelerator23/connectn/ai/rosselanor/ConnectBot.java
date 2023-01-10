package com.thg.accelerator23.connectn.ai.rosselanor;

import com.thehutgroup.accelerator.connectn.player.*;
import com.thg.accelerator23.connectn.ai.rosselanor.analysis.BoardAnalyser;
import com.thg.accelerator23.connectn.ai.rosselanor.analysis.BoardLine;
import com.thg.accelerator23.connectn.ai.rosselanor.model.Line;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.thehutgroup.accelerator.connectn.player.Board;
import com.thehutgroup.accelerator.connectn.player.Counter;
import com.thehutgroup.accelerator.connectn.player.Player;
import com.thg.accelerator23.connectn.ai.rosselanor.analysis.BoardAnalyser;


public class ConnectBot extends Player {
    Counter myCounter;
    BoardAnalyser boardAnalyser;

    public ConnectBot(Counter counter) {
        super(counter, ConnectBot.class.getName());
        this.myCounter = counter;
    }

  private Counter otherPlayer(){
    if (this.myCounter == Counter.X){
      return Counter.O;
    } else if (this.myCounter == Counter.O) {
      return Counter.X;
    }
    return null;
  }

  private boolean isColumnFull(Board board, int position) {
    int i = position;
    return IntStream.range(0, board.getConfig().getHeight())
            .allMatch(
                    j -> board.hasCounterAtPosition(new Position(i, j))
            );
  }

  public int makeMove(Board board) {
    MiniMaxAI miniMaxAI = new MiniMaxAI(5, myCounter, board);
      return miniMaxAI.runMiniMax();
    };

  private int boardEvaluator(Board board) {
    BoardAnalyser analysis = new BoardAnalyser(board.getConfig());
    List<Line> lines = analysis.getLines(board);
    Counter otherCounter = otherPlayer();
    int playerOneScore = 0;
    int playerTwoScore = 0;
    for (int i = 0; i < lines.size(); i++){
      Map<Counter, Integer> result = analysis.getBestRunByColour(lines.get(1));

      if (result.get(this.myCounter) == 2){
        playerOneScore = playerOneScore + 1;
        playerTwoScore = playerTwoScore - 1;
      }
      if (result.get(otherCounter) == 2){
        playerOneScore = playerOneScore - 1;
        playerTwoScore = playerTwoScore + 1;
      }
      if (result.get(this. myCounter) == 3){
        playerOneScore = playerOneScore + 10;
        playerTwoScore = playerTwoScore - 10;
      }
      if (result.get(otherCounter) == 3){
        playerOneScore = playerOneScore - 10;
        playerTwoScore = playerTwoScore + 10;
      }
    }
    int difference = playerOneScore - playerTwoScore;
    return difference;

  }


}

