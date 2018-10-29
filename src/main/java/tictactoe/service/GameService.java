package tictactoe.service;

import tictactoe.model.Board;
import tictactoe.model.BoardPosition;
import tictactoe.model.Game;
import tictactoe.model.Game.GameBuilder;
import tictactoe.model.GameConfiguration;
import tictactoe.model.Player;
import tictactoe.model.PlayerMove;
import tictactoe.model.PlayerMove.PlayerMoveBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static tictactoe.model.PlayerType.COMPUTER;
import static tictactoe.model.PlayerType.HUMAN;

public class GameService {
    private final GameConfiguration gameConfiguration;
    private final ComputerPlayerEngine computerPlayerEngine;

    public GameService(final GameConfiguration gameConfiguration, final ComputerPlayerEngine computerPlayerEngine) {
        this.gameConfiguration = gameConfiguration;
        this.computerPlayerEngine = computerPlayerEngine;
    }

    public Game startNewGame() {
        final Character[] playerMarks = gameConfiguration.getPlayerMarks();
        final List<Player> playerList = createPlayerList(playerMarks);
        final int boardSize = gameConfiguration.getPlaygroundSize();

        return new GameBuilder(new Board(boardSize), playerList).build();
    }

    public PlayerMove nextPlayerMove(final Game game, final Supplier<String> humanInteraction) {
        if (!game.isGameOver()) {
            final Board board = game.getBoard();
            final Player activePlayer = game.getActivePlayer();

            if (activePlayer.getPlayerType() == HUMAN) {
                return getHumanPlayerMove(humanInteraction, board);
            }
            return computerPlayerEngine.getComputerPlayerMove(board);
        }
        return new PlayerMoveBuilder().build();
    }

    public Game playerMakesMove(final Game game, final BoardPosition position) {
        if (!game.isGameOver()) {
            final Player activePlayer = game.getActivePlayer();
            final Board modifiedBoard = placeMarkAtBoardPosition(activePlayer.getMark(), position, game.getBoard());
            final GameBuilder gameBuilder = new GameBuilder(modifiedBoard, game.getPlayerList());
            final int turnCount = game.getTurnCount();

            if (modifiedBoard.checkForAWinner(position)) {
                return gameBuilder.winner(activePlayer).turnCount(turnCount).build();
            }

            return gameBuilder.turnCount(turnCount + 1).build();
        }

        return game;
    }

    public Game exitGame(final Game game) {
        return new GameBuilder(game.getBoard(), game.getPlayerList())
                .turnCount(game.getTurnCount())
                .escape(true)
                .build();
    }

    private List<Player> createPlayerList(final Character[] playerMarks) {
        final List<Player> playerList = new ArrayList<>();
        playerList.add(new Player(COMPUTER, playerMarks[0]));

        //create the rest of players HUMAN
        IntStream.range(1, playerMarks.length)
                 .mapToObj(index -> new Player(HUMAN, playerMarks[index]))
                 .forEach(playerList::add);

        //randomize player list
        Collections.shuffle(playerList);
        return playerList;
    }

    private PlayerMove getHumanPlayerMove(final Supplier<String> humanInteraction, final Board board) {
        final String humanInput = humanInteraction.get();

        if (humanInput.matches("(?i)exit")) {
            return new PlayerMoveBuilder().escape(true).build();
        }

        final BoardPosition position = getValidPosition(humanInput, board);
        return new PlayerMoveBuilder().position(position).build();
    }

    private BoardPosition getValidPosition(final String position, final Board board) {
        if (position.matches("\\d+,\\d+")) {
            final String[] posDimensions = position.split(",");
            final int row = Integer.valueOf(posDimensions[0]) - 1;
            final int col = Integer.valueOf(posDimensions[1]) - 1;

            if (board.isPositionValid(row, col) && !board.isPositionOccupied(row, col)) {
                return new BoardPosition(row, col);
            }
        }

        return null;
    }

    private Board placeMarkAtBoardPosition(final char mark, final BoardPosition boardPosition, final Board board) {
        final int row = boardPosition.getRow();
        final int col = boardPosition.getCol();

        if (!board.isPositionValid(row, col)) {
            throw new IllegalStateException(format("Position [%s][%s] is invalid.", row, col));
        }
        if (board.isPositionOccupied(row, col)) {
            throw new IllegalStateException(format("Position [%s][%s] is already occupied by another player.", row, col));
        }

        final Character[][] boardGrid = board.getBoardGrid();
        boardGrid[row][col] = mark;

        return new Board(boardGrid);
    }
}
