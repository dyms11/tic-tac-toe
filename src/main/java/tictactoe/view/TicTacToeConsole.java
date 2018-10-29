package tictactoe.view;

import tictactoe.model.Board;
import tictactoe.model.Game;
import tictactoe.model.Player;

import java.io.InputStream;
import java.util.Scanner;
import java.util.stream.IntStream;

import static tictactoe.model.GameConfiguration.NUMBER_OF_PLAYERS;

public class TicTacToeConsole implements TicTacToeView {
    private final Scanner scanner;

    public TicTacToeConsole(final InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
    }

    @Override
    public String inputTryAgain() {
        for(int index = 0; index < 5; index++) {
            System.out.print("Would you like to start a new game? (Y/N): ");
            final String response = scanner.next();
            if (response.matches("(?i)[y|n]")) {
                return response.toUpperCase();
            }
            System.out.println();
            System.err.println("Invalid input.");
        }
        return "";
    }

    @Override
    public void printBoard(final Board board) {
        System.out.println();
        System.out.println();
        System.out.println(board);
        System.out.println();
    }

    @Override
    public void printGameStart(final Game game) {
        final int playgroundSize = game.getBoard().getSize();
        System.out.printf("Started new game with #players=%d and playgroundSize=%d%n", NUMBER_OF_PLAYERS, playgroundSize);

        IntStream.range(0, NUMBER_OF_PLAYERS)
                 .forEach(playerNo -> System.out.printf("--Turn #%d ->  %s%n", playerNo + 1, game.playerAt(playerNo)));
        printBoard(game.getBoard());
        printTurn(game.getActivePlayer());
    }

    @Override
    public String getPlayerMove() {
        System.out.print("Insert coordinates of the position to mark: ");
        return scanner.next();
    }

    @Override
    public void printTurn(final Player player) {
        System.out.println("It's your turn " + player + "!");
    }

    @Override
    public void warnInvalidMove() {
        System.out.println();
        System.err.println("The position you tried to mark is invalid or already occupied!");
    }

    @Override
    public void announceResults(final Player winner, final boolean escape) {
        if (escape) {
            System.out.println("\nEXITING THE GAME!!!\nNo winner!");
        } else if (winner == null) {
            System.out.println("GAME IS OVER!!!\nBoard is full. It's a Draw!");
        } else {
            System.out.println("GAME IS OVER!!!\n" + winner + " has won the game!");
        }

    }

    @Override
    public void printGoodBye() {
        System.out.println("GOOD BYE!!!");
    }
}
