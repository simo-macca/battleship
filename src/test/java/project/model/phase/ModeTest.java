package project.model.phase;

import org.junit.Test;
import org.junit.Assert;

import project.configuration.ConstantsUtils;
import project.model.Game;
import jtamaro.en.Sequence;
import project.ui.board.Board;
import project.ui.boat.BoatGraphics;
import jtamaro.en.io.KeyboardChar;
import project.model.players.Player;

public class ModeTest {

    @Test
    public void handleKeyTest() {
        Board board = new Board(ConstantsUtils.BOARD_LEFT_CORNER_CARTESIAN_POSITION); // 125,425
        Sequence<Player> playersUtent = ConstantsUtils.PLAYERS;
        Sequence<BoatGraphics> boats = Mode.BOATS;
        Sequence<BoatGraphics> defaults = Mode.DEFAULTS;
        Phase playerSettingPhase = new PlayerSetting(Mode.DESTROYER, boats, defaults, -1, 4, false);
        Game quitGame = new Game(new Mode(), board, 0, playersUtent, true);
        Game defaultGame = new Game(new Mode(), board, 0, playersUtent, false);
        Game utentGame = new Game(playerSettingPhase, board, 0, playersUtent, false);
        Game comptuerGame = new Game(new ChooseDifficulty(), board, 0, playersUtent, false);
        KeyboardChar q = new KeyboardChar('q');
        KeyboardChar u = new KeyboardChar('u');
        KeyboardChar c = new KeyboardChar('c');
        KeyboardChar z = new KeyboardChar('z');
        Assert.assertEquals(quitGame, defaultGame.handleKey(q));
        Assert.assertEquals(utentGame, defaultGame.handleKey(u));
        Assert.assertEquals(defaultGame, defaultGame.handleKey(z));
        Assert.assertEquals(comptuerGame, defaultGame.handleKey(c));
    }
}