package tictactoe;

import tictactoe.model.Game;
import tictactoe.presenter.TicTacToePresenter;

public class ConsoleApp {
    private final TicTacToePresenter ticTacToePresenter;

    public ConsoleApp(final TicTacToePresenter ticTacToePresenter) {
        this.ticTacToePresenter = ticTacToePresenter;
    }

    public void play() {
        Game game = ticTacToePresenter.startNewGame();

        while(true) {
            game = ticTacToePresenter.playerMakesMove(game);

            if (game.isGameOver()) {
                ticTacToePresenter.announceResults(game);
                if (!ticTacToePresenter.tryAgain()) {
                    ticTacToePresenter.goodBye();
                    break;
                }
                game = ticTacToePresenter.startNewGame();
            }
        }
    }
}
