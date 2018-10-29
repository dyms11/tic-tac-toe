package tictactoe.service;

import tictactoe.model.Board;
import tictactoe.model.BoardPosition;
import tictactoe.model.PlayerMove;
import tictactoe.model.PlayerMove.PlayerMoveBuilder;

import java.util.Set;

public class ComputerPlayerEngine {

    PlayerMove getComputerPlayerMove(final Board board) {
        final BoardPosition position = nextMove(board);
        if (position == null) {
            throw new IllegalStateException("No available moves to make.");
        }

        return new PlayerMoveBuilder().position(position).build();
    }

    private BoardPosition nextMove(final Board board) {
        final Set<BoardPosition> availablePositions = board.getAvailablePositions();
        return availablePositions.stream()
                                 .findAny()
                                 .orElse(null);
    }
}
