package tictactoe.service;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import tictactoe.model.Board;
import tictactoe.model.BoardPosition;
import tictactoe.model.Game;
import tictactoe.model.Game.GameBuilder;
import tictactoe.model.GameConfiguration;
import tictactoe.model.Player;
import tictactoe.model.PlayerMove;
import tictactoe.model.PlayerMove.PlayerMoveBuilder;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static tictactoe.model.PlayerType.COMPUTER;
import static tictactoe.model.PlayerType.HUMAN;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {
    private static final String PROP_BOARD = "board";
    private static final String PROP_PLAYER_LIST = "playerList";
    private static final String PROP_TURN_COUNT = "turnCount";
    private static final String PROP_WINNER = "winner";
    private static final String PROP_GAME_OVER = "gameOver";
    private static final String PROP_ACTIVE_PLAYER = "activePlayer";
    private static final String PROP_POSITION = "position";
    private static final String PROP_VALID_MOVE = "validMove";
    private static final String PROP_ESCAPE = "escape";

    @Mock
    private GameConfiguration gameConfiguration;
    @Mock
    private ComputerPlayerEngine computerPlayerEngine;
    @InjectMocks
    private GameService gameService;

    @Test
    public void startNewGame() {
        when(gameConfiguration.getPlayerMarks()).thenReturn(new Character[] {'A', 'B', 'C'});
        when(gameConfiguration.getPlaygroundSize()).thenReturn(10);

        final Game actualGame = gameService.startNewGame();

        final Player player1 = new Player(COMPUTER, 'A');
        final Player player2 = new Player(HUMAN, 'B');
        final Player player3 = new Player(HUMAN, 'C');
        final Board expectedBoard = new Board(10);

        assertThat(actualGame, allOf(hasProperty(PROP_BOARD, samePropertyValuesAs(expectedBoard)),
                                     hasProperty(PROP_PLAYER_LIST, containsInAnyOrder(player1, player2, player3)))
        );

        verify(gameConfiguration).getPlayerMarks();
        verify(gameConfiguration).getPlaygroundSize();
    }

    @Test
    public void nextPlayerMoveComputer() {
        final Player player1 = new Player(COMPUTER, 'O');
        final Player player2 = new Player(HUMAN, 'X');
        final Board board = new Board(10);
        final Game game = new GameBuilder(board, asList(player1, player2)).build();
        final BoardPosition position = new BoardPosition(0, 0);
        final PlayerMove playerMove = new PlayerMoveBuilder().position(position).build();

        when(computerPlayerEngine.getComputerPlayerMove(board)).thenReturn(playerMove);

        final PlayerMove actualMove = gameService.nextPlayerMove(game, () -> "2,2");

        assertThat(actualMove, is(playerMove));

        verify(computerPlayerEngine).getComputerPlayerMove(board);
    }

    @Test
    public void nextPlayerMoveHuman() {
        final Player player1 = new Player(COMPUTER, 'O');
        final Player player2 = new Player(HUMAN, 'X');
        final Board board = new Board(10);
        final Game game = new GameBuilder(board, asList(player1, player2)).turnCount(3).build();
        final BoardPosition position = new BoardPosition(1, 1);
        final PlayerMove playerMove = new PlayerMoveBuilder().position(position).build();

        final PlayerMove actualMove = gameService.nextPlayerMove(game, () -> "2,2");

        assertThat(actualMove, is(playerMove));
    }

    @Test
    public void nextPlayerMoveHumanInvalid() {
        final Player player1 = new Player(COMPUTER, 'O');
        final Player player2 = new Player(HUMAN, 'X');
        final Board board = new Board(10);
        final Game game = new GameBuilder(board, asList(player1, player2)).turnCount(1).build();

        final PlayerMove move = gameService.nextPlayerMove(game, () -> "0,4");

        assertThat(move, allOf(hasProperty(PROP_POSITION, nullValue()),
                               hasProperty(PROP_ESCAPE, equalTo(false)),
                               hasProperty(PROP_VALID_MOVE, equalTo(false))));
    }

    @Test
    public void nextPlayerMoveHumanOccupied() {
        final Player player1 = new Player(COMPUTER, 'O');
        final Player player2 = new Player(HUMAN, 'X');
        final Character[][] boardGrid = new Character[3][3];
        boardGrid[1][1] = 'X';
        final Board board = new Board(boardGrid);
        final Game game = new GameBuilder(board, asList(player1, player2)).turnCount(1).build();

        final PlayerMove move = gameService.nextPlayerMove(game, () -> "2,2");

        assertThat(move, allOf(hasProperty(PROP_POSITION, nullValue()),
                               hasProperty(PROP_ESCAPE, equalTo(false)),
                               hasProperty(PROP_VALID_MOVE, equalTo(false))));
    }

    @Test
    public void nextPlayerMoveHumanTypo() {
        final Player player1 = new Player(COMPUTER, 'O');
        final Player player2 = new Player(HUMAN, 'X');
        final Board board = new Board(10);
        final Game game = new GameBuilder(board, asList(player1, player2)).turnCount(1).build();

        final PlayerMove move = gameService.nextPlayerMove(game, () -> "A");

        assertThat(move, allOf(hasProperty(PROP_POSITION, nullValue()),
                               hasProperty(PROP_ESCAPE, equalTo(false)),
                               hasProperty(PROP_VALID_MOVE, equalTo(false))));
    }

    @Test
    public void nextPlayerMoveHumanEscape() {
        final Player player1 = new Player(COMPUTER, 'O');
        final Player player2 = new Player(HUMAN, 'X');
        final Board board = new Board(10);
        final Game game = new GameBuilder(board, asList(player1, player2)).turnCount(1).build();

        final PlayerMove move = gameService.nextPlayerMove(game, () -> "eXiT");

        assertThat(move, allOf(hasProperty(PROP_POSITION, nullValue()),
                               hasProperty(PROP_ESCAPE, equalTo(true)),
                               hasProperty(PROP_VALID_MOVE, equalTo(false))));
    }

    @Test
    public void nextPlayerMoveAfterGameIsOver() {
        final Player player1 = new Player(COMPUTER, 'O');
        final Player player2 = new Player(HUMAN, 'X');
        final Board board = new Board(10);
        final Game game = new GameBuilder(board, asList(player1, player2)).turnCount(100).build();

        final PlayerMove move = gameService.nextPlayerMove(game, () -> "A");

        assertFalse(move.isValidMove());
    }

    @Test
    public void playerMakesMoveSuccess() {
        final Player player1 = new Player(HUMAN, 'O');
        final Player player2 = new Player(COMPUTER, 'X');
        final Player player3 = new Player(HUMAN, 'C');
        final Character[][] boardGrid = new Character[10][10];
        boardGrid[0][1] = 'X';
        boardGrid[0][1] = 'A';
        boardGrid[1][1] = 'O';
        final Board board = new Board(boardGrid);
        final Game game = new GameBuilder(board, asList(player1, player2, player3)).turnCount(3).build();
        final BoardPosition boardPosition = new BoardPosition(2, 1);

        final Game actualGame = gameService.playerMakesMove(game, boardPosition);

        boardGrid[2][1] = 'O';
        final Board expectedBoard = new Board(boardGrid);

        assertThat(actualGame, allOf(hasProperty(PROP_BOARD, samePropertyValuesAs(expectedBoard)),
                                     hasProperty(PROP_PLAYER_LIST, containsInAnyOrder(player1, player2, player3)),
                                     hasProperty(PROP_TURN_COUNT, equalTo(4)),
                                     hasProperty(PROP_WINNER, nullValue()),
                                     hasProperty(PROP_GAME_OVER, equalTo(false)),
                                     hasProperty(PROP_ACTIVE_PLAYER, equalTo(player2)))
        );
    }

    @Test(expected = IllegalStateException.class)
    public void playerMakesMoveOccupied() {
        final Player player1 = new Player(HUMAN, 'O');
        final Player player2 = new Player(HUMAN, 'X');
        final Player player3 = new Player(COMPUTER, 'A');
        final Character[][] boardGrid = new Character[10][10];
        boardGrid[0][1] = 'X';
        final Board board = new Board(boardGrid);
        final Game game = new GameBuilder(board, asList(player1, player2, player3)).turnCount(1).build();
        final BoardPosition boardPosition = new BoardPosition(0, 1);

        gameService.playerMakesMove(game, boardPosition);
    }

    @Test(expected = IllegalStateException.class)
    public void playerMakesMoveInvalid() {
        final Player player1 = new Player(HUMAN, 'O');
        final Player player2 = new Player(HUMAN, 'X');
        final Player player3 = new Player(COMPUTER, 'A');
        final Board board = new Board(10);
        final Game game = new GameBuilder(board, asList(player1, player2, player3)).turnCount(99).build();
        final BoardPosition boardPosition = new BoardPosition(10, 10);

        gameService.playerMakesMove(game, boardPosition);
    }

    @Test
    public void playerMakesMoveWinner() {
        final Player player1 = new Player(HUMAN, 'A');
        final Player player2 = new Player(COMPUTER, 'B');
        final Character[][] boardGrid = new Character[3][3];
        boardGrid[0][0] = 'A';
        boardGrid[0][1] = 'B';
        boardGrid[1][1] = 'A';
        boardGrid[2][1] = 'B';
        final Board board = new Board(boardGrid);
        final Game game = new GameBuilder(board, asList(player1, player2)).turnCount(4).build();
        final BoardPosition boardPosition = new BoardPosition(2, 2);

        final Game actualGame = gameService.playerMakesMove(game, boardPosition);

        boardGrid[2][2] = 'A';
        final Board expectedBoard = new Board(boardGrid);

        assertThat(actualGame, allOf(hasProperty(PROP_BOARD, samePropertyValuesAs(expectedBoard)),
                                     hasProperty(PROP_PLAYER_LIST, containsInAnyOrder(player1, player2)),
                                     hasProperty(PROP_TURN_COUNT, equalTo(4)),
                                     hasProperty(PROP_WINNER, equalTo(player1)),
                                     hasProperty(PROP_GAME_OVER, equalTo(true)),
                                     hasProperty(PROP_ACTIVE_PLAYER, equalTo(player1)))
        );
    }

    @Test
    public void playerMakesMoveTurnsExceeded() {
        final Player player1 = new Player(HUMAN, 'A');
        final Player player2 = new Player(COMPUTER, 'B');
        final Character[][] boardGrid = new Character[3][3];
        boardGrid[0][0] = 'A';
        boardGrid[0][2] = 'B';
        final Board board = new Board(boardGrid);
        final Game game = new GameBuilder(board, asList(player1, player2)).turnCount(8).build();
        final BoardPosition boardPosition = new BoardPosition(2, 2);

        final Game actualGame = gameService.playerMakesMove(game, boardPosition);

        boardGrid[2][2] = 'A';
        final Board expectedBoard = new Board(boardGrid);

        assertThat(actualGame, allOf(hasProperty(PROP_BOARD, samePropertyValuesAs(expectedBoard)),
                                     hasProperty(PROP_PLAYER_LIST, containsInAnyOrder(player1, player2)),
                                     hasProperty(PROP_TURN_COUNT, equalTo(9)),
                                     hasProperty(PROP_WINNER, nullValue()),
                                     hasProperty(PROP_GAME_OVER, equalTo(true)))
        );
    }

    @Test
    public void playerMakesMoveAfterGameIsOver() {
        final Player player1 = new Player(HUMAN, 'O');
        final Player player2 = new Player(HUMAN, 'X');
        final Player player3 = new Player(COMPUTER, 'A');
        final Board board = new Board(10);
        final Game game = new GameBuilder(board, asList(player1, player2, player3)).turnCount(100).build();
        final BoardPosition boardPosition = new BoardPosition(10, 10);

        final Game actualGame = gameService.playerMakesMove(game, boardPosition);

        assertThat(actualGame, sameInstance(game));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(gameConfiguration, computerPlayerEngine);
    }
}
