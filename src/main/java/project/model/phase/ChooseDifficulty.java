package project.model.phase;

import jtamaro.en.Graphic;
import jtamaro.en.io.KeyboardChar;
import project.configuration.ConstantsUtils;
import project.model.Game;
import project.ui.phase.PhaseUI;

/**
 * ChooseDifficulty render the "Difficulty page" of the game where the player can choose the
 * difficulty of the computer
 */
public record ChooseDifficulty() implements Phase {

  public Game handleKey(KeyboardChar keyChar, Game game) {
    Phase playerSettingPhase =
        new PlayerSetting(Mode.DESTROYER, Mode.BOATS, Mode.DEFAULTS, -1, 4, false);
    return Character.toUpperCase(keyChar.getChar()) == 'E'
        ? new Game(
            playerSettingPhase,
            game.board(),
            game.playerIndex(),
            ConstantsUtils.VS_RANDOM_COMP,
            game.quit())
        : Character.toUpperCase(keyChar.getChar()) == 'H'
            ? new Game(
                playerSettingPhase,
                game.board(),
                game.playerIndex(),
                ConstantsUtils.VS_LOGIC_COMP,
                game.quit())
            : game;
  }

  public Graphic render(Game game) {
    return PhaseUI.renderChooseDifficulty();
  }
}
