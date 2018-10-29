package tictactoe.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Board board;
    private final List<Player> playerList;
    private final int maxTurns;
    private final int turnCount;
    private final Player winner;
    private final boolean escape;

    private Game (final GameBuilder builder) {
        this.board = builder.board;
        this.playerList = new ArrayList<>(builder.players);
        this.maxTurns = builder.maxTurns;
        this.turnCount = builder.turnCount;
        this.winner = builder.winner;
        this.escape = builder.escape;
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayerList() {
        return new ArrayList<>(playerList);
    }

    public Player getWinner() {
        return winner;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public boolean isEscape() {
        return escape;
    }

    public boolean isGameOver() {
        return winner != null
                || turnCount >= maxTurns
                || escape;
    }

    public Player getActivePlayer() {
        final int currentTurn = turnCount % playerList.size();
        return playerList.get(currentTurn);
    }

    public Player playerAt(final int turn) {
        if (turn < 0 || turn >= playerList.size()) {
            throw new IllegalArgumentException("Invalid turn parameter.");
        }
        return playerList.get(turn);
    }

    public static class GameBuilder {
        private final Board board;
        private final List<Player> players;
        private final int maxTurns;
        private int turnCount;
        private Player winner;
        private boolean escape;

        public GameBuilder(final Board board, final List<Player> players) {
            if (board == null || players == null || players.isEmpty()) {
                throw new IllegalArgumentException("Invalid game parameters.");
            }

            this.board = board;
            this.players = players;
            this.maxTurns = board.getSize() * board.getSize();
        }

        public GameBuilder turnCount(final int turnCount) {
            if (turnCount < 0 || turnCount > maxTurns) {
                throw new IllegalArgumentException("Invalid turn count.");
            }
            this.turnCount = turnCount;
            return this;
        }

        public GameBuilder winner(final Player winner) {
            if (winner == null || !players.contains(winner)) {
                throw new IllegalArgumentException("Invalid winner.");
            }
            this.winner = winner;
            return this;
        }

        public GameBuilder escape(final boolean escape) {
            this.escape = escape;
            return this;
        }

        public Game build() {
            return new Game(this);
        }
    }
}
