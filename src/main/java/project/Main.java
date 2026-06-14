package project;

import jtamaro.en.IO;
import jtamaro.en.io.*;
import project.model.Game;

/**
 * This Main class is the main class of the game; it connects everything by constructing and
 * configuring an Interaction.
 */
public class Main {

  /**
   * The main entry point for the Battleship application. This method delegates to the method to
   * start the game.
   *
   * @param args command-line arguments (currently unused)
   */
  public static void main(String[] args) {
    play();
  }

  /** Build and run an Interaction for the Battleship Game with 2 players. */
  public static void play() {
    IO.interact(Game.create())
        .withRenderer(Game::render)
        .withKeyTypeHandler(Main::onKeyType)
        .withMousePressHandler(Main::onMousePress)
        .withMouseMoveHandler(Main::mouseMoveHandler)
        .withMouseReleaseHandler(Main::mouseReleaseHandler)
        .withStoppingPredicate(Main::close)
        .run();
  }

  /**
   * Handle a key press event. The game handles the event.
   *
   * @param game game before handling the event
   * @param keyChar the key that was pressed
   * @return game after handling the event
   */
  public static Game onKeyType(Game game, KeyboardChar keyChar) {
    return game.handleKey(keyChar);
  }

  /**
   * Handle a mouse-click event. The event is handled by the game (in this simple case, without
   * information about coordinates or buttons).
   *
   * @param game game before handling the event
   * @param coordinate the coordinate where the mouse was clicked
   * @param button the button that was clicked
   * @return game after handling the event
   */
  public static Game onMousePress(Game game, Coordinate coordinate, MouseButton button) {
    return game.handleMouse(coordinate);
  }

  /**
   * Handle a mouse-click event. The event is handled by the game (in this simple case, without
   * information about coordinates or buttons).
   *
   * @param game game before handling the event
   * @param coordinate the coordinate where the mouse was clicked
   * @return game after handling the event
   */
  public static Game mouseMoveHandler(Game game, Coordinate coordinate) {
    return game.mouseMoveHandler(coordinate);
  }

  /**
   * Handle a mouse-click event. The event is handled by the game (in this simple case, without
   * information about coordinates or buttons).
   *
   * @param game game before handling the event
   * @param coordinate the coordinate where the mouse was clicked
   * @param button the button that was clicked
   * @return game after handling the event
   */
  public static Game mouseReleaseHandler(Game game, Coordinate coordinate, MouseButton button) {
    return game.mouseReleaseHandler(coordinate);
  }

  /**
   * it's used to know if the player is playing
   *
   * @param game which corresponds to the current game
   * @return the state of the game, if it is true, the application quit automatically
   */
  public static boolean close(Game game) {
    return game.quit();
  }
}
