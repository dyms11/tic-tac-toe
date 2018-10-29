package tictactoe.presenter;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import tictactoe.model.Board;
import tictactoe.model.BoardPosition;
import tictactoe.model.Game;
import tictactoe.model.Game.GameBuilder;
import tictactoe.model.Player;
import tictactoe.model.PlayerMove;
import tictactoe.model.PlayerMove.PlayerMoveBuilder;
import tictactoe.service.GameService;
import tictactoe.view.TicTacToeView;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static tictactoe.model.PlayerType.COMPUTER;
import static tictactoe.model.PlayerType.HUMAN;

@RunWith(MockitoJUnitRunner.class)
public class TicTacToePresenterTest {
    @Mock
    private GameService gameService;
    @Mock
    private TicTacToeView view;
    @InjectMocks
    private TicTacToePresenter ticTacToePresenter;

    @Test
    public void startNewGame() {
        final Game game = createGame();
        when(gameService.startNewGame()).thenReturn(game);

        ticTacToePresenter.startNewGame();

        verify(gameService).startNewGame();
        verify(view).printGameStart(game);
    }

    @Test
    public void tryAgainYes() {
        when(view.inputTryAgain()).thenReturn("Y");

        assertTrue(ticTacToePresenter.tryAgain());

        verify(view).inputTryAgain();
    }

    @Test
    public void tryAgainNo() {
        when(view.inputTryAgain()).thenReturn("n");

        assertFalse(ticTacToePresenter.tryAgain());

        verify(view).inputTryAgain();
    }

    @Test
    public void tryAgainEmpty() {
        when(view.inputTryAgain()).thenReturn("");

        assertFalse(ticTacToePresenter.tryAgain());

        verify(view).inputTryAgain();
    }

    @Test
    public void playerMakesMoveValid() {
        final Game game = createGame();
        final Player player = new Player(COMPUTER, 'X');
        final BoardPosition position = new BoardPosition(2, 1);
        final PlayerMove playerMove = new PlayerMoveBuilder().position(position).build();

        final Character[][] boardGrid = new Character[3][3];
        boardGrid[1][1] = 'X';
        final Board modifiedBoard = new Board(boardGrid);
        final Game modifiedGame = new GameBuilder(modifiedBoard, singletonList(player)).turnCount(1).build();

        when(gameService.nextPlayerMove(eq(game), anyObject())).thenReturn(playerMove);
        when(gameService.playerMakesMove(game, position)).thenReturn(modifiedGame);

        final Game actualGame = ticTacToePresenter.playerMakesMove(game);

        assertThat(actualGame, allOf(sameInstance(modifiedGame),
                                     not(sameInstance(game))));

        final ArgumentCaptor<Board> boardCaptor = ArgumentCaptor.forClass(Board.class);
        verify(gameService).nextPlayerMove(eq(game), anyObject());
        verify(gameService).playerMakesMove(game, position);
        verify(view).printTurn(player);
        verify(view).printBoard(boardCaptor.capture());

        assertThat(boardCaptor.getValue(), samePropertyValuesAs(modifiedBoard));
    }

    @Test
    public void playerMakesMoveGameOver() {
        final Game game = createGame();
        final Player player = new Player(COMPUTER, 'X');
        final BoardPosition position = new BoardPosition(2, 1);
        final PlayerMove playerMove = new PlayerMoveBuilder().position(position).build();

        final Character[][] boardGrid = new Character[3][3];
        boardGrid[1][1] = 'X';
        final Board modifiedBoard = new Board(boardGrid);
        final Game modifiedGame = new GameBuilder(modifiedBoard, singletonList(player)).winner(player).build();

        when(gameService.nextPlayerMove(eq(game), anyObject())).thenReturn(playerMove);
        when(gameService.playerMakesMove(game, position)).thenReturn(modifiedGame);

        final Game actualGame = ticTacToePresenter.playerMakesMove(game);

        assertThat(actualGame, allOf(sameInstance(modifiedGame),
                                     not(sameInstance(game))));

        verify(gameService).nextPlayerMove(eq(game), anyObject());
        verify(gameService).playerMakesMove(game, position);
    }

    @Test
    public void playerMakesMoveAfterGameIsOver() {
        final Player player = new Player(HUMAN, 'X');
        final Board board = new Board(3);
        final BoardPosition position = new BoardPosition(0, 0);
        final PlayerMove playerMove = new PlayerMoveBuilder().position(position).build();

        final Game game = new GameBuilder(board, singletonList(player)).turnCount(9).build();
        when(gameService.nextPlayerMove(eq(game), anyObject())).thenReturn(playerMove);

        final Game actualGame = ticTacToePresenter.playerMakesMove(game);

        assertThat(actualGame, sameInstance(game));

        verify(gameService).nextPlayerMove(eq(game), anyObject());
        verify(view).warnInvalidMove();
    }

    @Test
    public void playerMakesMoveInvalid() {
        final Game game = createGame();
        final PlayerMove playerMove = new PlayerMoveBuilder().build();
        when(gameService.nextPlayerMove(eq(game), anyObject())).thenReturn(playerMove);

        final Game actualGame = ticTacToePresenter.playerMakesMove(game);

        assertThat(actualGame, sameInstance(game));

        verify(gameService).nextPlayerMove(eq(game), anyObject());
        verify(view).warnInvalidMove();
    }

    @Test
    public void playerMakesMoveEscape() {
        final Game game = createGame();
        final PlayerMove playerMove = new PlayerMoveBuilder().escape(true).build();
        final Game modifiedGame = new GameBuilder(game.getBoard(), game.getPlayerList()).escape(true).build();

        when(gameService.nextPlayerMove(eq(game), anyObject())).thenReturn(playerMove);
        when(gameService.exitGame(game)).thenReturn(modifiedGame);

        final Game actualGame = ticTacToePresenter.playerMakesMove(game);

        assertThat(actualGame, allOf(sameInstance(modifiedGame),
                                     not(sameInstance(game))));

        verify(gameService).nextPlayerMove(eq(game), anyObject());
        verify(gameService).exitGame(game);
    }

    @Test
    public void announceResults() {
        final Player player = new Player(HUMAN, 'X');
        final Board board = new Board(3);
        final Game game = new GameBuilder(board, singletonList(player)).turnCount(9).build();

        ticTacToePresenter.announceResults(game);

        final ArgumentCaptor<Board> boardCaptor = ArgumentCaptor.forClass(Board.class);
        verify(view).announceResults(anyObject(), eq(false));
        verify(view).printBoard(boardCaptor.capture());

        assertThat(boardCaptor.getValue(), samePropertyValuesAs(board));
    }

    @Test
    public void announceResultsNotGameOver() {
        final Game game = createGame();

        ticTacToePresenter.announceResults(game);
    }

    @Test
    public void goodBye() {
        ticTacToePresenter.goodBye();
        verify(view).printGoodBye();
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(gameService, view);
    }

    private Game createGame() {
        final Player player = new Player(HUMAN, 'X');
        final Board board = new Board(3);

        return new GameBuilder(board, singletonList(player)).build();
    }
}
