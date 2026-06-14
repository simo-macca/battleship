package project.model.phase;

import jtamaro.en.Graphic;
import jtamaro.en.io.KeyboardChar;
import project.configuration.ConstantsUtils;
import project.model.*;
import project.model.weapon.*;
import project.ui.phase.PhaseUI;

/**
 * ChooseWeapon is the phase where the player can choose the weapon he wants to use. It render the
 * game before the player decides where to strike.
 */
public record ChooseWeapon() implements Phase {

  private static final Weapon SINGLEBOMB =
      new SingleBomb(ConstantsUtils.WEAPON_BUTTON_POSITION, new Position(0, 0));

  public Game handleKey(KeyboardChar keyChar, Game game) {
    return Character.toUpperCase(keyChar.getChar()) == 'S'
        ? game.currentPlayer().startPlayerTurns(game)
        : Character.toUpperCase(keyChar.getChar()) == '\n'
            ? game.currentPlayer().startCompTurns(game)
            : game;
  }

  public Graphic render(Game game) {
    return PhaseUI.renderChooseWeapon(game, SINGLEBOMB);
  }
}
