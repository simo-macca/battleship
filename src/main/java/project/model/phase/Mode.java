package project.model.phase;

import static jtamaro.en.Colors.BLUE;
import static jtamaro.en.Colors.GREEN;
import static jtamaro.en.Colors.MAGENTA;
import static jtamaro.en.Colors.rgb;
import static jtamaro.en.Sequences.of;

import jtamaro.en.Graphic;
import jtamaro.en.Sequence;
import jtamaro.en.io.KeyboardChar;
import project.model.Game;
import project.ui.boat.BoatGraphics;
import project.ui.cartesianWorldGraphics.CartesianWorldPosition;
import project.ui.phase.PhaseUI;

/**
 * Modes render the "Main page" of the game where the player can choose the game mode, i.e., if play
 * with a bot or with a player.
 */
public record Mode() implements Phase {

  public static final BoatGraphics DESTROYER =
      new BoatGraphics(
          new CartesianWorldPosition(50, 130),
          BoatGraphics.boatGraphic(2, true, false, GREEN),
          true,
          false,
          2,
          GREEN);
  public static final BoatGraphics SUBMARINE =
      new BoatGraphics(
          new CartesianWorldPosition(120, 130),
          BoatGraphics.boatGraphic(3, true, false, BLUE),
          true,
          false,
          3,
          BLUE);
  public static final BoatGraphics BATTLESHIP =
      new BoatGraphics(
          new CartesianWorldPosition(220, 130),
          BoatGraphics.boatGraphic(4, true, false, MAGENTA),
          true,
          false,
          4,
          MAGENTA);
  public static final BoatGraphics CARRIER =
      new BoatGraphics(
          new CartesianWorldPosition(340, 130),
          BoatGraphics.boatGraphic(5, true, false, rgb(255, 165, 0)),
          true,
          false,
          5,
          rgb(255, 165, 0));
  public static final Sequence<BoatGraphics> DEFAULTS =
      of(DESTROYER, SUBMARINE, BATTLESHIP, CARRIER);
  public static final Sequence<BoatGraphics> BOATS = of(DESTROYER, SUBMARINE, BATTLESHIP, CARRIER);

  public Game handleKey(KeyboardChar keyChar, Game game) {
    return Character.toUpperCase(keyChar.getChar()) == 'U'
        ? game.changePhase(new PlayerSetting(DESTROYER, BOATS, DEFAULTS, -1, 4, false))
        : Character.toUpperCase(keyChar.getChar()) == 'C'
            ? game.changePhase(new ChooseDifficulty())
            : game;
  }

  public Graphic render(Game game) {
    return PhaseUI.renderChooseMode();
  }
}
