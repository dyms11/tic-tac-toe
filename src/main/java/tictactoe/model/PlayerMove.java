package tictactoe.model;

public class PlayerMove {
    private final BoardPosition position;
    private final boolean escape;

    private PlayerMove(final PlayerMoveBuilder builder) {
        this.position = builder.position;
        this.escape = builder.escape;
    }

    public BoardPosition getPosition() {
        return position;
    }

    public boolean isEscape() {
        return escape;
    }

    public boolean isValidMove() {
        return position != null;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerMove)) {
            return false;
        }

        final PlayerMove that = (PlayerMove) o;

        return escape == that.escape
                && (position != null ? position.equals(that.position) : that.position == null);
    }

    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (escape ? 1 : 0);
        return result;
    }

    public static class PlayerMoveBuilder {
        private BoardPosition position;
        private boolean escape;

        public PlayerMoveBuilder position(final BoardPosition position) {
            this.position = position;
            return this;
        }

        public PlayerMoveBuilder escape(final boolean escape) {
            this.escape = escape;
            return this;
        }

        public PlayerMove build() {
            return new PlayerMove(this);
        }
    }
}
