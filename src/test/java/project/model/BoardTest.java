package project.model;

import org.junit.Assert;
import org.junit.Test;

import project.configuration.ConstantsUtils;
import project.ui.board.Board;
import project.ui.cartesianWorldGraphics.CartesianWorldPosition;

public class BoardTest {

    private static final Board BOARD = new Board(ConstantsUtils.BOARD_LEFT_CORNER_CARTESIAN_POSITION); // 125,425
    private static final int BOARD_TILE_LENGTH = ConstantsUtils.TILE_LENGTH;
    private static final int BOARD_LENGTH = BOARD_TILE_LENGTH * 10;
    private static final CartesianWorldPosition MOUSE_IN_1 = new CartesianWorldPosition(234, 345);
    private static final CartesianWorldPosition MOUSE_OUT_1 = new CartesianWorldPosition(0, 0);
    private static final CartesianWorldPosition MOUSE_IN_2 = new CartesianWorldPosition(126, 424);
    private static final CartesianWorldPosition MOUSE_OUT_2 = new CartesianWorldPosition(453, 43);
    private static final CartesianWorldPosition MOUSE_IN_3 = new CartesianWorldPosition(374, 176);
    private static final CartesianWorldPosition MOUSE_IN_4 = new CartesianWorldPosition(126, 176);
    private static final CartesianWorldPosition MOUSE_IN_5 = new CartesianWorldPosition(374, 424);
    
    @Test
    public void isMouseOverBoardTest() {
        Assert.assertTrue(Board.isMouseOver(MOUSE_IN_1, BOARD.boardPosition(), BOARD_LENGTH, BOARD_LENGTH));
        Assert.assertFalse(Board.isMouseOver(MOUSE_OUT_1, BOARD.boardPosition(), BOARD_LENGTH, BOARD_LENGTH));
        Assert.assertTrue(Board.isMouseOver(MOUSE_IN_2, BOARD.boardPosition(), BOARD_LENGTH, BOARD_LENGTH));
        Assert.assertFalse(Board.isMouseOver(MOUSE_OUT_2, BOARD.boardPosition(), BOARD_LENGTH, BOARD_LENGTH));
        Assert.assertTrue(Board.isMouseOver(MOUSE_IN_3, BOARD.boardPosition(), BOARD_LENGTH, BOARD_LENGTH));
    }

    @Test
    public void boardPositionMouseOnTest() {
        Assert.assertEquals(new CartesianWorldPosition(226, 274), BOARD.boardPositionMouseOn(MOUSE_IN_1));
        Assert.assertEquals(new CartesianWorldPosition(126,199), BOARD.boardPositionMouseOn(MOUSE_IN_2));
        Assert.assertEquals(new CartesianWorldPosition(351,424), BOARD.boardPositionMouseOn(MOUSE_IN_3));
        Assert.assertEquals(new CartesianWorldPosition(126,424), BOARD.boardPositionMouseOn(MOUSE_IN_4));
        Assert.assertEquals(new CartesianWorldPosition(351,199), BOARD.boardPositionMouseOn(MOUSE_IN_5));
    }
}