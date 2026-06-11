package project.model.phase;

import jtamaro.en.Graphic;
import project.model.Game;
import jtamaro.en.io.Coordinate;
import jtamaro.en.io.KeyboardChar;

/**
 * Phase rappresent the all step about the game. The main goal of it is to change dinamiically the frame of the game
 * @see Mode
 * @see PlayerSettinga
 * @see ChooseWeapon
 * @see ChoosePosition
 * @see AfterAttack
 * @see RoundEnded
 */

public interface Phase {

    /**
     * it's used by all the phase to render the graphic of itself
     * @param game is the current phase game
     * @return the screen of the phase in which the player is
     */
    public Graphic render(Game game);
    
    /**
     * Handle a key press. In this game, we can handle the key irrespective of the phase.
     * @param keyChar the key that was pressed
     * @param game game before handling the key
     * @return the game after handling the key press
     */
    public default Game handleKey(KeyboardChar keyChar, Game game) {
        return game;
    }

    /**
     * Handle a mouse click. The handling depends on the current phase.
     * @param coordinate which use to know where the mouse is
     * @param game game before handling the mouse click
     * @return the game after handling the mouse click
     */
    public default Game handleMouse(Coordinate coordinate, Game game) {
        return game;
    }

    /**
     * Handle a mouse when it is pressed and moved. The handling depends on the current phase.
     * @param coordinate which use to know where the mouse is
     * @param game game before handling the mouse move
     * @return the game after handling the mouse move
     */
    public default Game mouseMoveHandler(Coordinate coordinate, Game game) {
        return game;
    }

    /**
     * Handle a mouse when it is released. The handling depends on the current phase.
     * @param coordinate which use to know where the mouse is
     * @param game game before handling the mouse released
     * @return the game after handling the mouse released
     */
    public default Game mouseReleaseHandler(Coordinate coordinate, Game game) {
        return game;
    }
}