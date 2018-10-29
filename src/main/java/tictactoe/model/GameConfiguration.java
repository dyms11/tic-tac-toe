package tictactoe.model;

public class GameConfiguration {
    public static final int NUMBER_OF_PLAYERS = 3;
    private final Character[] playerMarks;
    private final int playgroundSize;

    public GameConfiguration(final Character[] playerMarks, final int playgroundSize) {
        if (playgroundSize < 1 || playerMarks.length != NUMBER_OF_PLAYERS) {
            throw new IllegalArgumentException("Invalid values on parameters.");
        }

        this.playerMarks = playerMarks.clone();
        this.playgroundSize = playgroundSize;
    }

    public Character[] getPlayerMarks() {
        return playerMarks.clone();
    }

    public int getPlaygroundSize() {
        return playgroundSize;
    }
}
