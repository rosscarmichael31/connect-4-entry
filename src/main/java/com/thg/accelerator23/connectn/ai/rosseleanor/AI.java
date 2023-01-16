package com.thg.accelerator23.connectn.ai.rosseleanor;

import com.thehutgroup.accelerator.connectn.player.Board;
import com.thehutgroup.accelerator.connectn.player.InvalidMoveException;
import com.thg.accelerator23.connectn.ai.rosseleanor.analysis.BoardAnalyser;

public interface AI {
    int getMove() throws InvalidMoveException;

    void setBoard(Board board);

    void setBoardAnalyser(BoardAnalyser boardAnalyser);
}
