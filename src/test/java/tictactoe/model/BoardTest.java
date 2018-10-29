package tictactoe.model;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BoardTest {
    @Test(expected = IllegalArgumentException.class)
    public void createBoardInvalidSize() {
        new Board(-1);
    }

    @Test
    public void getSize() {
        final Board board = new Board(createBoardGrid());
        assertThat(board.getSize(), is(10));
    }

    @Test
    public void getAvailablePositionsPreFilled() {
        final Character[][] boardGrid = new Character[4][4];
        boardGrid[0][0] = 'A';
        boardGrid[3][2] = 'B';
        boardGrid[2][1] = 'B';

        final Board board = new Board(boardGrid);
        final Set<BoardPosition> expectedPositions = new HashSet<>();
        expectedPositions.add(new BoardPosition(0, 1));
        expectedPositions.add(new BoardPosition(0, 2));
        expectedPositions.add(new BoardPosition(0, 3));
        expectedPositions.add(new BoardPosition(1, 0));
        expectedPositions.add(new BoardPosition(1, 1));
        expectedPositions.add(new BoardPosition(1, 2));
        expectedPositions.add(new BoardPosition(1, 3));
        expectedPositions.add(new BoardPosition(2, 0));
        expectedPositions.add(new BoardPosition(2, 2));
        expectedPositions.add(new BoardPosition(2, 3));
        expectedPositions.add(new BoardPosition(3, 0));
        expectedPositions.add(new BoardPosition(3, 1));
        expectedPositions.add(new BoardPosition(3, 3));

        final Set<BoardPosition> availablePositions = board.getAvailablePositions();

        assertThat(availablePositions, is(expectedPositions));
    }

    @Test
    public void getAvailablePositionsEmpty() {
        final Set<BoardPosition> expectedPositions = new HashSet<>();
        expectedPositions.add(new BoardPosition(0, 0));
        expectedPositions.add(new BoardPosition(0, 1));
        expectedPositions.add(new BoardPosition(0, 2));
        expectedPositions.add(new BoardPosition(1, 0));
        expectedPositions.add(new BoardPosition(1, 1));
        expectedPositions.add(new BoardPosition(1, 2));
        expectedPositions.add(new BoardPosition(2, 0));
        expectedPositions.add(new BoardPosition(2, 1));
        expectedPositions.add(new BoardPosition(2, 2));

        final Board board = new Board(3);
        final Set<BoardPosition> availablePositions = board.getAvailablePositions();

        assertThat(availablePositions, is(expectedPositions));
    }

    @Test
    public void getBoardGrid() {
        final Character[][] expectedGrid = new Character[10][10];
        expectedGrid[0][0] = 'X';
        expectedGrid[1][1] = 'X';
        expectedGrid[2][2] = 'O';
        expectedGrid[2][3] = 'O';
        expectedGrid[4][4] = 'X';
        expectedGrid[6][6] = 'O';

        final Board board = new Board(createBoardGrid());
        final Character[][] actualGrid = board.getBoardGrid();

        assertThat(expectedGrid, is(actualGrid));
    }

    @Test
    public void isPositionOccupiedTrue() {
        final Board board = new Board(createBoardGrid());
        final boolean positionOcuppied = board.isPositionOccupied(0, 0);
        assertTrue(positionOcuppied);
    }

    @Test
    public void isPositionOccupiedFalse() {
        final Board board = new Board(createBoardGrid());
        final boolean positionOcuppied = board.isPositionOccupied(8, 0);
        assertFalse(positionOcuppied);
    }

    @Test
    public void isPositionOccupiedInvalid() {
        final Board board = new Board(createBoardGrid());
        final boolean positionOcuppied = board.isPositionOccupied(-1, -1);
        assertFalse(positionOcuppied);
    }

    @Test
    public void isPositionValidTrue() {
        final Board board = new Board(createBoardGrid());
        final boolean positionValid = board.isPositionValid(1, 1);
        assertTrue(positionValid);
    }

    @Test
    public void isPositionValidFalse() {
        final Board board = new Board(createBoardGrid());
        final boolean positionValid = board.isPositionValid(-1, -1);
        assertFalse(positionValid);
    }

    @Test
    public void getMarkAt() {
        final Board board = new Board(createBoardGrid());
        final char mark = board.getMarkAt(2, 3);
        assertThat(mark, is('O'));
    }

    @Test
    public void getMarkAtEmpty() {
        final Board board = new Board(createBoardGrid());
        final char mark = board.getMarkAt(7, 7);
        assertThat(mark, is(' '));
    }

    @Test
    public void checkForAWinnerTrue() {
        final Character[][] boardGrid = new Character[4][4];
        boardGrid[2][0] = 'X';
        boardGrid[2][1] = 'X';
        boardGrid[2][2] = 'X';
        boardGrid[0][1] = 'O';
        boardGrid[3][3] = 'O';
        boardGrid[2][3] = 'X';
        final Board board = new Board(boardGrid);

        final BoardPosition currentMove = new BoardPosition(2, 3);

        assertTrue(board.checkForAWinner(currentMove));
    }

    @Test
    public void checkForAWinnerFalse() {
        final Board board = new Board(createBoardGrid());
        final BoardPosition currentMove = new BoardPosition(5, 0);

        assertFalse(board.checkForAWinner(currentMove));
    }

    @Test
    public void checkRowTrue() {
        final Character[][] boardGrid = new Character[4][4];
        boardGrid[2][0] = 'X';
        boardGrid[2][1] = 'X';
        boardGrid[2][2] = 'X';
        boardGrid[2][3] = 'X';
        boardGrid[0][1] = 'O';
        boardGrid[3][3] = 'O';
        final Board board = new Board(boardGrid);

        assertTrue(board.checkRow(2));
    }

    @Test
    public void checkRowEmpty() {
        final Board board = new Board(8);
        IntStream.range(0, 8)
                 .forEach(row -> assertFalse(board.checkRow(row)));
    }

    @Test
    public void checkRowFalse() {
        final Board board = new Board(createBoardGrid());
        IntStream.range(0, 10)
                 .forEach(row -> assertFalse(board.checkRow(row)));
    }

    @Test
    public void checkRowInvalid() {
        final Board board = new Board(createBoardGrid());
        assertFalse(board.checkRow(-1));
    }

    @Test
    public void checkColumnTrue() {
        final Character[][] boardGrid = new Character[6][6];
        boardGrid[0][0] = 'X';
        boardGrid[5][5] = 'A';
        boardGrid[1][0] = 'X';
        boardGrid[3][3] = 'O';
        boardGrid[2][0] = 'X';
        boardGrid[4][2] = 'O';
        boardGrid[3][0] = 'X';
        boardGrid[2][1] = 'O';
        boardGrid[4][0] = 'X';
        boardGrid[4][5] = 'A';
        boardGrid[5][0] = 'X';
        final Board board = new Board(boardGrid);

        assertTrue(board.checkColumn(0));
    }

    @Test
    public void checkColumnFalse() {
        final Board board = new Board(createBoardGrid());
        IntStream.range(0, 10)
                 .forEach(col -> assertFalse(board.checkColumn(col)));
    }

    @Test
    public void checkColumnInvalid() {
        final Board board = new Board(createBoardGrid());
        assertFalse(board.checkRow(21));
    }

    @Test
    public void checkDiagonalLeftToRightTrue() {
        final Character[][] boardGrid = new Character[5][5];
        boardGrid[0][0] = 'X';
        boardGrid[2][0] = 'A';
        boardGrid[1][1] = 'X';
        boardGrid[4][3] = 'O';
        boardGrid[2][2] = 'X';
        boardGrid[4][2] = 'O';
        boardGrid[3][3] = 'X';
        boardGrid[2][1] = 'A';
        boardGrid[4][4] = 'X';
        final Board board = new Board(boardGrid);

        assertTrue(board.checkDiagonalLeftToRight(3, 3));
    }

    @Test
    public void checkDiagonalLeftToRightFalseMatch() {
        final Board board = new Board(createBoardGrid());
        assertFalse(board.checkDiagonalLeftToRight(0, 0));
    }

    @Test
    public void checkDiagonalLeftToRightFalseNotMatch() {
        final Board board = new Board(createBoardGrid());
        assertFalse(board.checkDiagonalLeftToRight(3, 0));
    }

    @Test
    public void checkDiagonalLeftToRightInvalid() {
        final Board board = new Board(createBoardGrid());
        assertFalse(board.checkDiagonalLeftToRight(0, 30));
    }

    @Test
    public void checkDiagonalRightToLeftTrue() {
        final Character[][] boardGrid = new Character[6][6];
        boardGrid[0][5] = 'X';
        boardGrid[2][0] = 'A';
        boardGrid[1][4] = 'X';
        boardGrid[4][3] = 'O';
        boardGrid[2][3] = 'X';
        boardGrid[4][2] = 'O';
        boardGrid[3][2] = 'X';
        boardGrid[2][1] = 'A';
        boardGrid[4][1] = 'X';
        boardGrid[5][5] = 'A';
        boardGrid[5][0] = 'X';
        final Board board = new Board(boardGrid);

        assertTrue(board.checkDiagonalRightToLeft(2, 3));
    }

    @Test
    public void checkDiagonalRightToLeftFalseMatch() {
        final Board board = new Board(createBoardGrid());
        assertFalse(board.checkDiagonalRightToLeft(0, 9));
    }

    @Test
    public void checkDiagonalRightToLeftFalseNotMatch() {
        final Board board = new Board(createBoardGrid());
        assertFalse(board.checkDiagonalRightToLeft(3, 9));
    }

    @Test
    public void checkDiagonalRightToLeftFalseInvalid() {
        final Board board = new Board(createBoardGrid());
        assertFalse(board.checkDiagonalRightToLeft(-1, 10));
    }

    @Test
    public void testToString() {
        final Board board = new Board(createBoardGrid());

        final String expectedString =
                "- - - - - - - - - - - - - - - - - - - - -\n" +
                        "| X |   |   |   |   |   |   |   |   |   | \n" +
                        "- - - - - - - - - - - - - - - - - - - - -\n" +
                        "|   | X |   |   |   |   |   |   |   |   | \n" +
                        "- - - - - - - - - - - - - - - - - - - - -\n" +
                        "|   |   | O | O |   |   |   |   |   |   | \n" +
                        "- - - - - - - - - - - - - - - - - - - - -\n" +
                        "|   |   |   |   |   |   |   |   |   |   | \n" +
                        "- - - - - - - - - - - - - - - - - - - - -\n" +
                        "|   |   |   |   | X |   |   |   |   |   | \n" +
                        "- - - - - - - - - - - - - - - - - - - - -\n" +
                        "|   |   |   |   |   |   |   |   |   |   | \n" +
                        "- - - - - - - - - - - - - - - - - - - - -\n" +
                        "|   |   |   |   |   |   | O |   |   |   | \n" +
                        "- - - - - - - - - - - - - - - - - - - - -\n" +
                        "|   |   |   |   |   |   |   |   |   |   | \n" +
                        "- - - - - - - - - - - - - - - - - - - - -\n" +
                        "|   |   |   |   |   |   |   |   |   |   | \n" +
                        "- - - - - - - - - - - - - - - - - - - - -\n" +
                        "|   |   |   |   |   |   |   |   |   |   | \n" +
                        "- - - - - - - - - - - - - - - - - - - - -\n";

        final String boardString = board.toString();
        assertThat(boardString, is(expectedString));
    }

    private Character[][] createBoardGrid() {
        final Character[][] boardGrid = new Character[10][10];
        boardGrid[0][0] = 'X';
        boardGrid[1][1] = 'X';
        boardGrid[2][2] = 'O';
        boardGrid[2][3] = 'O';
        boardGrid[4][4] = 'X';
        boardGrid[6][6] = 'O';

        return boardGrid;
    }

}
