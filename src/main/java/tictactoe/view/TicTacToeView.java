package tictactoe.view;

import tictactoe.model.Board;
import tictactoe.model.Game;
import tictactoe.model.Player;

public interface TicTacToeView {
    String inputTryAgain();

    void printBoard(Board board);

    void printGameStart(Game game);

    String getPlayerMove();

    void printTurn(Player player);

    void warnInvalidMove();

    void announceResults(Player winner, boolean escape);

    void printGoodBye();
}
