package tictactoe.model;

import org.junit.Test;
import tictactoe.model.Game.GameBuilder;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static tictactoe.model.PlayerType.COMPUTER;
import static tictactoe.model.PlayerType.HUMAN;

public class GameTest {

    @Test
    public void getPlayer() {
        final Player player1 = createPlayer(HUMAN, 'O');
        final Player player2 = createPlayer(COMPUTER, 'X');
        final Game game = new GameBuilder(new Board(3), asList(player1, player2)).build();

        assertThat(game.playerAt(1), is(player2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPlayerInvalidTurn() {
        final Player player1 = createPlayer(HUMAN, 'O');
        final Player player2 = createPlayer(COMPUTER, 'X');
        final Game game = new GameBuilder(new Board(3), asList(player1, player2)).build();

        game.playerAt(2);
    }


    @Test
    public void getActivePlayerInitial() {
        final Player player1 = createPlayer(HUMAN, 'O');
        final Player player2 = createPlayer(COMPUTER, 'X');
        final Game game = new GameBuilder(new Board(3), asList(player1, player2)).build();

        assertThat(game.getActivePlayer(), is(player1));
    }

    @Test
    public void getActivePlayerAfterMoves() {
        final Player player1 = createPlayer(COMPUTER, '$');
        final Player player2 = createPlayer(COMPUTER, 'V');
        final Player player3 = createPlayer(HUMAN, '9');
        final List<Player> playerList = asList(player1, player2, player3);
        final Board board = new Board(3);
        final Game game = new GameBuilder(board, playerList).turnCount(4).build();

        assertThat(game.getActivePlayer(), is(player2));
    }

    @Test
    public void isGameOverWinner() {
        final Player player1 = createPlayer(COMPUTER, '$');
        final Player player2 = createPlayer(COMPUTER, 'V');
        final List<Player> playerList = asList(player1, player2);
        final Board board = new Board(3);
        final Game game = new GameBuilder(board, playerList).winner(player1)
                                                            .turnCount(6)
                                                            .build();

        assertTrue(game.isGameOver());
    }

    @Test
    public void isGameOverTurnsExceeded() {
        final Player player1 = createPlayer(COMPUTER, 'V');
        final Player player2 = createPlayer(COMPUTER, '$');
        final List<Player> playerList = asList(player1, player2);
        final Board board = new Board(3);
        final Game game = new GameBuilder(board, playerList).turnCount(9)
                                                            .build();

        assertTrue(game.isGameOver());
    }

    @Test
    public void isGameOverTurnsEscape() {
        final Player player1 = createPlayer(COMPUTER, 'V');
        final Player player2 = createPlayer(COMPUTER, '$');
        final List<Player> playerList = asList(player1, player2);
        final Board board = new Board(3);
        final Game game = new GameBuilder(board, playerList).escape(true)
                                                            .build();

        assertTrue(game.isGameOver());
    }

    private Player createPlayer(final PlayerType playerType, final char mark) {
        return new Player(playerType, mark);
    }
}
