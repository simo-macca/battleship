package project.ui.board;

import static jtamaro.en.Colors.*;
import static jtamaro.en.Points.*;
import static jtamaro.en.Graphics.*;
import static jtamaro.en.Sequences.of;
import static jtamaro.en.Sequences.cons;
import static jtamaro.en.Sequences.concat;
import static jtamaro.en.Sequences.replicate;

import project.Toolbelt;
import jtamaro.en.Graphic;
import jtamaro.en.Sequence;
import project.configuration.ConstantsUtils;
import project.ui.cartesianWorldGraphics.CartesianWorldPosition;

/**
 * @param boardPosition indicates the position of the top left corner of the board
 */
public record Board(CartesianWorldPosition boardPosition) {

    public static final int BOARD_TILE_LENGTH = ConstantsUtils.TILE_LENGTH;
    public static final int BOARD_LENGTH = BOARD_TILE_LENGTH * 10;

    /**
     * it uses to know if the mouse is on a grid of the game
     * @param mousePosition which corresponds to a mouse position
     * @param buttonPosition which corresponds to a button position or the grid position
     * @param height which corresponds to the height of the button/cell
     * @param width which corresponds to the width of the button/cell
     * @return true or false based on whether the mouse is inside the borders or not
     */
    public static boolean isMouseOver(CartesianWorldPosition mousePosition, CartesianWorldPosition buttonPosition, int height, int width) {
        return 
            mousePosition.x() > buttonPosition.x() && 
            mousePosition.x() < buttonPosition.x() + width &&
            mousePosition.y() > ConstantsUtils.HEIGTH - buttonPosition.y() && 
            mousePosition.y() < (ConstantsUtils.HEIGTH - buttonPosition.y()) + height;
    }

    /**
     * it uses to know if the mouse is over the board
     * @param position the mouse position
     * @return if is over the board
     */
    public boolean isPositionOverBoard(CartesianWorldPosition position){
        return Board.isMouseOver(position, boardPosition, Board.BOARD_LENGTH, Board.BOARD_LENGTH);
    }

    /**
     * it uses to know the specific mouse position, iff it is inside the border of the button
     * @param mousePosition which corresponds to a mouse position
     * @return the position of the mouse
     */
    public CartesianWorldPosition boardPositionMouseOn(CartesianWorldPosition mousePosition){
        assert isMouseOver(mousePosition, boardPosition, BOARD_LENGTH, BOARD_LENGTH);
        return new CartesianWorldPosition(
            computeBoardX(boardPosition.x(), mousePosition.x()),
            computeBoardY(boardPosition.y() - BOARD_LENGTH, ConstantsUtils.HEIGTH - mousePosition.y()));
    }

    public static int computeBoardX(int deltaX, int x){
        return ((x - deltaX)/BOARD_TILE_LENGTH)*BOARD_TILE_LENGTH + deltaX + 1;
    }

    public static int computeBoardY(int deltaY, int y){
        return ((y - deltaY)/BOARD_TILE_LENGTH)*BOARD_TILE_LENGTH + BOARD_TILE_LENGTH + deltaY - 1;
    }

    /**
     * it's used to create a grid for the main game
     * @return a 2d matrix grid 25x25
     */
    public static Graphic grid() {
        Graphic thinLine = rectangle(1, ConstantsUtils.TILE_LENGTH*10, rgb(219, 219, 219));
        Graphic tile = rectangle(ConstantsUtils.TILE_LENGTH - 1, 0, TRANSPARENT);
        Sequence<Graphic> verticalLines = replicate(beside(tile, thinLine), 10);
        return pin(TOP_LEFT,
            overlay(
                overlay(
                    Toolbelt.reduce(
                        emptyGraphic(), 
                        (a, b) -> beside(a, b), 
                        cons(
                            thinLine,
                            verticalLines
                        )
                    ),
                    Toolbelt.reduce(
                        emptyGraphic(), 
                        (a, b) -> above(a, b), 
                        concat(Toolbelt.map((a) -> rotate(90, a), verticalLines), of(rotate(90, thinLine)))
                    )
                ),
                rectangle(ConstantsUtils.TILE_LENGTH*10, ConstantsUtils.TILE_LENGTH*10, rgb(38, 69, 123, .7))
            )
        );
    }
}