package project.model.phase;

import project.model.*;
import jtamaro.en.Graphic;
import project.ui.phase.PhaseUI;
import jtamaro.en.io.KeyboardChar;

/**
 * AfterAttack renders the board of the player not playing with boats covered unless they have been sunk.
 * AfterAttack renders the bombs of the player that is currently playing
 */
public record AfterAttack() implements Phase {

    public Game handleKey(KeyboardChar keyChar, Game game) {
        return Character.toUpperCase(keyChar.getChar()) == 'N' ? game.currentPlayer().attacking(game) : game;
    }

    public Graphic render(Game game) {
        return PhaseUI.renderAfterAttack(game);
    }
}