package project.model.phase;

import jtamaro.en.Graphic;
import project.model.Game;
import project.ui.phase.PhaseUI;

/**
 * RoundEnded is the phase activated only if the current player has won. It renders the screen with the writing of the winner of the game
 */

public record RoundEnded() implements Phase {

    public Graphic render(Game game) {
        return PhaseUI.renderRoundEnded(game);
    }
}