package tictactoe.service;

import org.junit.Test;
import tictactoe.model.Board;
import tictactoe.model.BoardPosition;
import tictactoe.model.PlayerMove;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertThat;

public class ComputerPlayerEngineTest {
    private final ComputerPlayerEngine computerPlayerEngine = new ComputerPlayerEngine();

    @Test
    public void getComputerPlayerMove() {
        final Board board = new Board(createBoardGrid());

        final PlayerMove nextPosition = computerPlayerEngine.getComputerPlayerMove(board);

        final BoardPosition expectedPosition = new BoardPosition(4, 4);
        assertThat(nextPosition, allOf(hasProperty("position", equalTo(expectedPosition)),
                                       hasProperty("escape", equalTo(false)),
                                       hasProperty("validMove", equalTo(true))));
    }

    @Test
    public void getComputerPlayerMoveAfterMove() {
        final Character[][] boardGrid = createBoardGrid();
        boardGrid[4][4] = 'A';

        final Board board = new Board(boardGrid);
        final PlayerMove nextPosition = computerPlayerEngine.getComputerPlayerMove(board);

        final BoardPosition expectedPosition = new BoardPosition(8, 8);
        assertThat(nextPosition, allOf(hasProperty("position", equalTo(expectedPosition)),
                                       hasProperty("escape", equalTo(false)),
                                       hasProperty("validMove", equalTo(true))));
    }

    private Character[][] createBoardGrid() {
        final Character[][] boardGrid = new Character[10][10];
        boardGrid[0][0] = 'A';
        boardGrid[8][1] = 'B';
        boardGrid[0][9] = 'C';
        boardGrid[1][1] = 'A';
        boardGrid[8][0] = 'B';
        boardGrid[1][9] = 'C';
        boardGrid[2][2] = 'A';
        boardGrid[8][2] = 'B';
        boardGrid[2][9] = 'C';
        boardGrid[3][3] = 'A';
        boardGrid[8][3] = 'B';
        boardGrid[3][9] = 'C';
        boardGrid[5][5] = 'A';
        boardGrid[8][4] = 'B';
        boardGrid[5][9] = 'C';
        boardGrid[6][6] = 'A';
        boardGrid[8][5] = 'B';
        boardGrid[6][9] = 'C';

        return boardGrid;
    }

}
