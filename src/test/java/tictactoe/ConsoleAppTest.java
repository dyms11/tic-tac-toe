package tictactoe;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import tictactoe.model.Board;
import tictactoe.model.Game;
import tictactoe.model.Game.GameBuilder;
import tictactoe.model.Player;
import tictactoe.presenter.TicTacToePresenter;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static tictactoe.model.PlayerType.COMPUTER;
import static tictactoe.model.PlayerType.HUMAN;

@RunWith(MockitoJUnitRunner.class)
public class ConsoleAppTest {

    @Mock
    private TicTacToePresenter ticTacToePresenter;
    @InjectMocks
    private ConsoleApp consoleApp;

    @Test
    public void playGameOverNoTryAgain() {
        final Board board = new Board(5);
        final Player player1 = new Player(HUMAN, 'C');
        final Player player2 = new Player(HUMAN, '*');
        final Player player3 = new Player(COMPUTER, '4');
        final Game game = new GameBuilder(board, asList(player1, player2, player3)).build();
        final Game modifiedGame = new GameBuilder(board, asList(player1, player2, player3)).turnCount(25).build();

        when(ticTacToePresenter.startNewGame()).thenReturn(game);
        when(ticTacToePresenter.playerMakesMove(game)).thenReturn(modifiedGame);
        when(ticTacToePresenter.tryAgain()).thenReturn(false);

        consoleApp.play();

        verify(ticTacToePresenter).startNewGame();
        verify(ticTacToePresenter).playerMakesMove(game);
        verify(ticTacToePresenter).tryAgain();
        verify(ticTacToePresenter).goodBye();
        verify(ticTacToePresenter).announceResults(modifiedGame);
    }

    @Test
    public void playGameOverTryAgain() {
        final Board board = new Board(5);
        final Player player1 = new Player(HUMAN, 'C');
        final Player player2 = new Player(HUMAN, '*');
        final Player player3 = new Player(COMPUTER, '4');
        final Game game = new GameBuilder(board, asList(player1, player2, player3)).build();
        final Game modifiedGame = new GameBuilder(board, asList(player1, player2, player3)).turnCount(25).build();

        when(ticTacToePresenter.startNewGame()).thenReturn(game);
        when(ticTacToePresenter.playerMakesMove(game)).thenReturn(modifiedGame);
        when(ticTacToePresenter.tryAgain()).thenReturn(true)
                                           .thenReturn(false);

        consoleApp.play();

        verify(ticTacToePresenter, times(2)).startNewGame();
        verify(ticTacToePresenter, times(2)).playerMakesMove(game);
        verify(ticTacToePresenter, times(2)).tryAgain();
        verify(ticTacToePresenter).goodBye();
        verify(ticTacToePresenter, times(2)).announceResults(modifiedGame);
    }

    @Test
    public void playThreeMovesGameOver() {
        final Board board = new Board(5);
        final Player player1 = new Player(HUMAN, 'C');
        final Player player2 = new Player(HUMAN, '*');
        final Player player3 = new Player(COMPUTER, '4');
        final Game game = new GameBuilder(board, asList(player1, player2, player3)).build();
        final Game modifiedGame = new GameBuilder(board, asList(player1, player2, player3)).turnCount(25).build();

        when(ticTacToePresenter.startNewGame()).thenReturn(game);
        when(ticTacToePresenter.playerMakesMove(game)).thenReturn(game)
                                                      .thenReturn(game)
                                                      .thenReturn(modifiedGame);
        when(ticTacToePresenter.tryAgain()).thenReturn(false);

        consoleApp.play();

        verify(ticTacToePresenter).startNewGame();
        verify(ticTacToePresenter, times(3)).playerMakesMove(game);
        verify(ticTacToePresenter).tryAgain();
        verify(ticTacToePresenter).goodBye();
        verify(ticTacToePresenter).announceResults(modifiedGame);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(ticTacToePresenter);
    }
}
