package tictactoe.model;

public class BoardPosition {
    private final int row;
    private final int col;

    public BoardPosition(final int row, final int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BoardPosition)) {
            return false;
        }

        final BoardPosition that = (BoardPosition) o;

        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }
}
