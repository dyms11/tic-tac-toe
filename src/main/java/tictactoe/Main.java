package tictactoe;

import tictactoe.model.GameConfiguration;
import tictactoe.presenter.TicTacToePresenter;
import tictactoe.service.ComputerPlayerEngine;
import tictactoe.service.ConfigurationLoader;
import tictactoe.service.GameService;
import tictactoe.view.TicTacToeConsole;
import tictactoe.view.TicTacToeView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class Main {

    public static void main(final String[] args) {
        final Optional<GameConfiguration> gameConfiguration = loadConfiguration(args);

        if (gameConfiguration.isPresent()) {
            final ComputerPlayerEngine computerPlayerEngine = new ComputerPlayerEngine();
            final GameService gameService = new GameService(gameConfiguration.get(), computerPlayerEngine);
            final TicTacToeView ticTacToeView = new TicTacToeConsole(System.in);
            final TicTacToePresenter ticTacToePresenter = new TicTacToePresenter(gameService, ticTacToeView);
            final ConsoleApp consoleApp = new ConsoleApp(ticTacToePresenter);

            consoleApp.play();
        }
    }

    private static Optional<GameConfiguration> loadConfiguration(final String[] args) {
        final ConfigurationLoader configurationLoader = new ConfigurationLoader();

        if (args.length > 0) {
            return configurationLoader.loadGameConfiguration(args[0]);
        }

        return configurationLoader.loadGameConfiguration();
    }
}
