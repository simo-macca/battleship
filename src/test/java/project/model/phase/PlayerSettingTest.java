package project.model.phase;

import static jtamaro.en.Colors.BLUE;
import static jtamaro.en.Colors.GREEN;
import static jtamaro.en.Colors.MAGENTA;
import static jtamaro.en.Colors.YELLOW;
import static jtamaro.en.Sequences.of;

import org.junit.Assert;
import org.junit.Test;
import jtamaro.en.Sequence;
import jtamaro.en.io.KeyboardChar;
import project.configuration.ConstantsUtils;
import project.model.Game;
import project.model.players.Player;
import project.ui.board.Board;
import project.ui.boat.BoatGraphics;
import project.ui.cartesianWorldGraphics.CartesianWorldPosition;

public class PlayerSettingTest {

    @Test
    public void handleKeyTest() {
        Board board = new Board(ConstantsUtils.BOARD_LEFT_CORNER_CARTESIAN_POSITION); // 125,425
        Sequence<Player> players = ConstantsUtils.PLAYERS;
        BoatGraphics destroyer = new BoatGraphics(new CartesianWorldPosition(50, 130), BoatGraphics.boatGraphic(2, true, false, GREEN), true, false, 2, GREEN);
        BoatGraphics submarine = new BoatGraphics(new CartesianWorldPosition(120, 130), BoatGraphics.boatGraphic(3, true, false, BLUE), true, false, 3, BLUE);
        BoatGraphics battleship = new BoatGraphics(new CartesianWorldPosition(220, 130), BoatGraphics.boatGraphic(4, true, false, MAGENTA), true, false, 4, MAGENTA);
        BoatGraphics carrier = new BoatGraphics(new CartesianWorldPosition(340, 130), BoatGraphics.boatGraphic(5, true, false, YELLOW), true, false, 5, YELLOW);
        Sequence<BoatGraphics> defaults = of(destroyer, submarine, battleship, carrier);
        Sequence<BoatGraphics> boats = of(destroyer, submarine, battleship, carrier);
        Phase playerSettingPhase = new PlayerSetting(Mode.DESTROYER, boats, defaults, -1, 4, false);
        Game actualGame = new Game(playerSettingPhase, board, 0, players, false);
        Game resetGame = new Game(new Mode(), board, 0, players, false);
        Game nextPlayerGame = new Game(playerSettingPhase, board, 0, players, false);
        Game nextPhaseGame = new Game(new ChooseWeapon(), board, 0, players, false);
        Game quitGame = new Game(playerSettingPhase, board, 0, players, true);
        KeyboardChar q = new KeyboardChar('q');
        KeyboardChar r = new KeyboardChar('r');
        KeyboardChar n = new KeyboardChar('n');
        KeyboardChar z = new KeyboardChar('z');
        Assert.assertEquals(quitGame, actualGame.handleKey(q));
        Assert.assertEquals(resetGame, actualGame.handleKey(r));
        Assert.assertEquals(actualGame, actualGame.handleKey(z));
        Assert.assertEquals(nextPlayerGame, actualGame.handleKey(n));
        Assert.assertEquals(nextPhaseGame, actualGame.changePhase(new ChooseWeapon()));
    }
}