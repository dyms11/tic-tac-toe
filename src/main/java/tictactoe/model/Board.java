package tictactoe.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.Collections.nCopies;

public class Board {
    private static final int MIN_BOARD_SIZE = 3;
    private final int size;
    private final Character[][] boardGrid;
    private final Set<BoardPosition> availablePositions;

    public Board(final Character[][] boardGrid) {
        if (boardGrid.length < MIN_BOARD_SIZE) {
            throw new IllegalArgumentException("Board size should be greater than " + MIN_BOARD_SIZE);
        }
        this.size = boardGrid.length;
        this.boardGrid = boardGrid;
        this.availablePositions = buildAvailablePositions(size);
    }

    public Board(final int size) {
        if (size < MIN_BOARD_SIZE) {
            throw new IllegalArgumentException("Board size should be greater than " + MIN_BOARD_SIZE);
        }
        this.size = size;
        this.boardGrid = new Character[size][size];
        this.availablePositions = buildAvailablePositions(size);
    }

    public int getSize() {
        return size;
    }

    public Character[][] getBoardGrid() {
        return IntStream.range(0, size)
                        .mapToObj(row -> boardGrid[row].clone())
                        .toArray(Character[][]::new);
    }

    public Set<BoardPosition> getAvailablePositions() {
        return new HashSet<>(availablePositions);
    }


    public boolean isPositionOccupied(final int row, final int col) {
        return isPositionValid(row, col) && boardGrid[row][col] != null;
    }

    public boolean isPositionValid(final int row, final int col) {
        return isDimensionValid(row) && isDimensionValid(col);
    }

    public boolean checkForAWinner(final BoardPosition boardPosition) {
        final int row = boardPosition.getRow();
        final int col = boardPosition.getCol();

        return checkRow(row)
                || checkColumn(col)
                || checkDiagonalLeftToRight(row, col)
                || checkDiagonalRightToLeft(row, col);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Board)) {
            return false;
        }

        final Board board = (Board) o;

        return size == board.size
                && Arrays.deepEquals(boardGrid, board.boardGrid)
                && availablePositions.equals(board.availablePositions);
    }

    @Override
    public int hashCode() {
        int result = size;
        result = 31 * result + Arrays.deepHashCode(boardGrid);
        return result;
    }

    @Override
    public String toString() {
        final int repeateHR = size * 2 + 1;
        final String horizontalLine = String.join(" ", nCopies(repeateHR, "-"));

        final StringBuilder sb = new StringBuilder();
        sb.append(horizontalLine);
        sb.append("\n");
        for (int row = 0; row < size; row++) {
            sb.append("| ");
            for (int col = 0; col < size; col++) {
                sb.append(getMarkAt(row, col));
                sb.append(" | ");
            }
            sb.append("\n");
            sb.append(horizontalLine);
            sb.append("\n");
        }

        return new String(sb);
    }

    char getMarkAt(final int row, final int col) {
        return boardGrid[row][col] != null ? boardGrid[row][col] : ' ';
    }

    boolean checkRow(final int row) {
        return isDimensionValid(row) &&
                IntStream.range(1, size)
                         .allMatch(col -> compareTwoCells(row, col, row, col - 1));
    }

    boolean checkColumn(final int col) {
        return isDimensionValid(col) &&
                IntStream.range(1, size)
                         .allMatch(row -> compareTwoCells(row, col, row - 1, col));
    }

    boolean checkDiagonalLeftToRight(final int row, final int col) {
        return row == col && isPositionValid(row, col) &&
                IntStream.range(1, size)
                         .allMatch(cellIndex -> compareTwoCells(cellIndex, cellIndex, cellIndex - 1, cellIndex - 1));

    }

    boolean checkDiagonalRightToLeft(final int row, final int col) {
        if (isPositionValid(row, col) && getRightToLeftDiagonalCol(row) != col) {
            return false;
        }

        for (int iRow = 1; iRow < size; iRow++) {
            final int iCol = getRightToLeftDiagonalCol(iRow);
            final int prevCol = getRightToLeftDiagonalCol(iRow - 1);

            if (!compareTwoCells(iRow, iCol, iRow - 1, prevCol)) {
                return false;
            }
        }
        return true;
    }

    private int getRightToLeftDiagonalCol(final int row) {
        return size - 1 - row;
    }

    private boolean compareTwoCells(final int currRow, final int currCol, final int prevRow, final int prevCol) {
        final Character currentCell = boardGrid[currRow][currCol];
        final Character previousCell = boardGrid[prevRow][prevCol];

        return currentCell != null && currentCell == previousCell;
    }

    private boolean isDimensionValid(final int dimension) {
        return dimension >= 0 && dimension < size;
    }

    private Set<BoardPosition> buildAvailablePositions(final int size) {
        final Set<BoardPosition> boardPositions = new HashSet<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (!isPositionOccupied(row, col)) {
                    boardPositions.add(new BoardPosition(row, col));
                }
            }
        }

        return boardPositions;
    }
}
