package tictactoe.model;

public class Player {
    private final PlayerType playerType;
    private final char mark;

    public Player(final PlayerType playerType, final char mark) {
        if (playerType == null) {
            throw new IllegalArgumentException("Invalid player parameters.");
        }
        this.playerType = playerType;
        this.mark = mark;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public char getMark() {
        return mark;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }

        final Player player = (Player) o;

        return mark == player.mark && playerType == player.playerType;
    }

    @Override
    public int hashCode() {
        int result = playerType.hashCode();
        result = 31 * result + (int) mark;
        return result;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerType=" + playerType +
                ", mark=" + mark +
                '}';
    }
}
