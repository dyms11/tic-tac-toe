package tictactoe.presenter;

import tictactoe.model.Game;
import tictactoe.model.PlayerMove;
import tictactoe.service.GameService;
import tictactoe.view.TicTacToeView;

public class TicTacToePresenter {
    private final GameService gameService;
    private final TicTacToeView view;

    public TicTacToePresenter(final GameService gameService, final TicTacToeView ticTacToeView) {
        this.gameService = gameService;
        this.view = ticTacToeView;
    }

    public Game startNewGame() {
        final Game game = gameService.startNewGame();
        view.printGameStart(game);
        return game;
    }

    public boolean tryAgain() {
        final String tryAgainResponse = view.inputTryAgain();
        return tryAgainResponse.equalsIgnoreCase("y");
    }

    public Game playerMakesMove(final Game game) {
        final PlayerMove move = gameService.nextPlayerMove(game, view::getPlayerMove);
        if (move.isEscape()) {
            return gameService.exitGame(game);
        }
        if (!move.isValidMove() || game.isGameOver()) {
            view.warnInvalidMove();
            return game;
        }

        final Game gameAfterMove = gameService.playerMakesMove(game, move.getPosition());
        if (!gameAfterMove.isGameOver()) {
            view.printBoard(gameAfterMove.getBoard());
            view.printTurn(gameAfterMove.getActivePlayer());
        }

        return gameAfterMove;
    }

    public void announceResults(final Game game) {
        if (game.isGameOver()) {
            view.printBoard(game.getBoard());
            view.announceResults(game.getWinner(), game.isEscape());
        }
    }

    public void goodBye() {
        view.printGoodBye();
    }
}
