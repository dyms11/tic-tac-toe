package tictactoe;

import org.junit.Test;
import tictactoe.model.GameConfiguration;
import tictactoe.presenter.TicTacToePresenter;
import tictactoe.service.ComputerPlayerEngine;
import tictactoe.service.GameService;
import tictactoe.view.TicTacToeConsole;
import tictactoe.view.TicTacToeView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ConsoleAppIT {

    @Test
    public void play() {
        final String inputStream = "1,1\n1,2\n1,3\n1,4\n1,5\n" +
                        "2,1\n2,2\n2,3\n2,4\n2,5\n" +
                        "3,1\n3,2\n3,3\n3,4\n3,5\n" +
                        "4,1\n4,2\n4,3\n4,4\n4,5\n" +
                        "5,1\n5,2\n5,3\n5,4\n5,5\nN\n";

        final ConsoleApp consoleApp = createConsoleApp(inputStream);

        consoleApp.play();
    }

    @Test
    public void playEscape() {
        final String inputStream = "EXIt\nn\n";

        final ConsoleApp consoleApp = createConsoleApp(inputStream);

        consoleApp.play();
    }

    private ConsoleApp createConsoleApp(final String data) {
        final GameConfiguration gameConfiguration = new GameConfiguration(new Character[] {'A', 'B', 'C'}, 5);
        final ComputerPlayerEngine computerPlayerEngine = new ComputerPlayerEngine();
        final GameService gameService = new GameService(gameConfiguration, computerPlayerEngine);
        final InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        final TicTacToeView ticTacToeView = new TicTacToeConsole(inputStream);
        final TicTacToePresenter ticTacToePresenter = new TicTacToePresenter(gameService, ticTacToeView);

        return new ConsoleApp(ticTacToePresenter);
    }
}
