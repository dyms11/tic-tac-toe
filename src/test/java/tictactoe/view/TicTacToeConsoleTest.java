package tictactoe.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import tictactoe.model.Board;
import tictactoe.model.Game;
import tictactoe.model.Game.GameBuilder;
import tictactoe.model.Player;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static tictactoe.model.PlayerType.COMPUTER;
import static tictactoe.model.PlayerType.HUMAN;

@RunWith(MockitoJUnitRunner.class)
public class TicTacToeConsoleTest {
    private static final String TRY_AGAIN_MESSAGE = "Would you like to start a new game? (Y/N): ";
    private static final String TRY_AGAIN_FAIL_MESSAGE = TRY_AGAIN_MESSAGE + "\nInvalid input.\n";

    private ByteArrayOutputStream outContent;

    @Before
    public void setup() {
        outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void inputTryAgainYes() {
        final TicTacToeConsole ticTacToeConsole = createConsole("y");

        final String tryAgain = ticTacToeConsole.inputTryAgain();

        assertThat(tryAgain, is("Y"));
        assertEquals(TRY_AGAIN_MESSAGE, outContent.toString());
    }

    @Test
    public void inputTryAgainNo() {
        final TicTacToeConsole ticTacToeConsole = createConsole("N");

        final String tryAgain = ticTacToeConsole.inputTryAgain();

        assertThat(tryAgain, is("N"));
        assertEquals(TRY_AGAIN_MESSAGE, outContent.toString());
    }

    @Test
    public void inputTryAgainSeveralAttempts() {
        final TicTacToeConsole ticTacToeConsole = createConsole("other\nmore\nY");

        final String tryAgain = ticTacToeConsole.inputTryAgain();

        assertThat(tryAgain, is("Y"));
        assertEquals(TRY_AGAIN_FAIL_MESSAGE + TRY_AGAIN_FAIL_MESSAGE + TRY_AGAIN_MESSAGE, outContent.toString());
    }

    @Test
    public void inputTryAgainExceededAttempts() {
        final TicTacToeConsole ticTacToeConsole = createConsole("other\nmore\nmore\nmore\nmore\nmore");

        final String tryAgain = ticTacToeConsole.inputTryAgain();

        assertThat(tryAgain, is(""));
        final String expectedOutput = String.join("", nCopies(5, TRY_AGAIN_FAIL_MESSAGE));
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void printBoard() {
        final TicTacToeConsole ticTacToeConsole = createConsole("");
        final Board board = new Board(5);

        ticTacToeConsole.printBoard(board);

        assertEquals("\n\n" +
                             "- - - - - - - - - - -\n" +
                             "|   |   |   |   |   | \n" +
                             "- - - - - - - - - - -\n" +
                             "|   |   |   |   |   | \n" +
                             "- - - - - - - - - - -\n" +
                             "|   |   |   |   |   | \n" +
                             "- - - - - - - - - - -\n" +
                             "|   |   |   |   |   | \n" +
                             "- - - - - - - - - - -\n" +
                             "|   |   |   |   |   | \n" +
                             "- - - - - - - - - - -\n" +
                             "\n\n", outContent.toString());
    }

    @Test
    public void printGameStart() {
        final Board board = new Board(5);
        final Player player1 = new Player(HUMAN, 'C');
        final Player player2 = new Player(HUMAN, '*');
        final Player player3 = new Player(COMPUTER, '4');
        final Game game = new GameBuilder(board, asList(player1, player2, player3)).build();

        final TicTacToeConsole ticTacToeConsole = createConsole("");

        ticTacToeConsole.printGameStart(game);

        assertEquals("Started new game with #players=3 and playgroundSize=5\n" +
                             "--Turn #1 ->  Player{playerType=HUMAN, mark=C}\n" +
                             "--Turn #2 ->  Player{playerType=HUMAN, mark=*}\n" +
                             "--Turn #3 ->  Player{playerType=COMPUTER, mark=4}\n" +
                             "\n" +
                             "\n" +
                             "- - - - - - - - - - -\n" +
                             "|   |   |   |   |   | \n" +
                             "- - - - - - - - - - -\n" +
                             "|   |   |   |   |   | \n" +
                             "- - - - - - - - - - -\n" +
                             "|   |   |   |   |   | \n" +
                             "- - - - - - - - - - -\n" +
                             "|   |   |   |   |   | \n" +
                             "- - - - - - - - - - -\n" +
                             "|   |   |   |   |   | \n" +
                             "- - - - - - - - - - -\n" +
                             "\n" +
                             "\n" +
                             "It's your turn Player{playerType=HUMAN, mark=C}!\n", outContent.toString());
    }

    @Test
    public void getPlayerMove() {
        final TicTacToeConsole ticTacToeConsole = createConsole("2,2");

        final String playerMove = ticTacToeConsole.getPlayerMove();

        assertThat(playerMove, is("2,2"));
        assertEquals("Insert coordinates of the position to mark: ", outContent.toString());
    }

    @Test
    public void printTurn() {
        final Player player = new Player(COMPUTER, '4');
        final TicTacToeConsole ticTacToeConsole = createConsole("");

        ticTacToeConsole.printTurn(player);

        assertEquals("It's your turn Player{playerType=COMPUTER, mark=4}!\n", outContent.toString());
    }

    @Test
    public void warnInvalidMove() {
        final TicTacToeConsole ticTacToeConsole = createConsole("");

        ticTacToeConsole.warnInvalidMove();

        assertEquals("\nThe position you tried to mark is invalid or already occupied!\n", outContent.toString());
    }

    @Test
    public void announceResultsDraw() {
        final TicTacToeConsole ticTacToeConsole = createConsole("");

        ticTacToeConsole.announceResults(null, false);

        assertEquals("GAME IS OVER!!!\nBoard is full. It's a Draw!\n", outContent.toString());
    }

    @Test
    public void announceResultsWinner() {
        final Player player = new Player(HUMAN, 'X');
        final TicTacToeConsole ticTacToeConsole = createConsole("");

        ticTacToeConsole.announceResults(player, false);

        assertEquals("GAME IS OVER!!!\n" +
                             "Player{playerType=HUMAN, mark=X} has won the game!\n", outContent.toString());
    }

    @Test
    public void announceResultsEscape() {
        final Player player = new Player(HUMAN, 'X');
        final TicTacToeConsole ticTacToeConsole = createConsole("");

        ticTacToeConsole.announceResults(player, true);

        assertEquals("\nEXITING THE GAME!!!\nNo winner!\n", outContent.toString());
    }

    @Test
    public void goodBye() {
        final TicTacToeConsole ticTacToeConsole = createConsole("");

        ticTacToeConsole.printGoodBye();

        assertEquals("GOOD BYE!!!\n", outContent.toString());
    }

    private TicTacToeConsole createConsole(final String data) {
        final InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        return new TicTacToeConsole(inputStream);
    }
}
