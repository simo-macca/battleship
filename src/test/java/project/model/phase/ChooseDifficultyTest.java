package project.model.phase;

import jtamaro.en.Sequence;
import jtamaro.en.io.KeyboardChar;
import org.junit.Assert;
import org.junit.Test;
import project.configuration.ConstantsUtils;
import project.model.Game;
import project.model.players.Player;
import project.ui.board.Board;
import project.ui.boat.BoatGraphics;

public class ChooseDifficultyTest {

  @Test
  public void handleKeyTest() {
    Board board = new Board(ConstantsUtils.BOARD_LEFT_CORNER_CARTESIAN_POSITION); // 125,425
    Sequence<Player> playersUtent = ConstantsUtils.PLAYERS;
    Sequence<Player> playersRandomComp = ConstantsUtils.VS_RANDOM_COMP;
    Sequence<Player> playersLogicComp = ConstantsUtils.VS_LOGIC_COMP;
    Sequence<BoatGraphics> boats = Mode.BOATS;
    Sequence<BoatGraphics> defaults = Mode.DEFAULTS;
    Phase playerSettingPhase = new PlayerSetting(Mode.DESTROYER, boats, defaults, -1, 4, false);
    Game actualGame = new Game(new ChooseDifficulty(), board, 0, playersRandomComp, false);
    Game randomCompGame = new Game(playerSettingPhase, board, 0, playersRandomComp, false);
    Game logicCompGame = new Game(playerSettingPhase, board, 0, playersLogicComp, false);
    Game resetGame = new Game(new Mode(), board, 0, playersUtent, false);
    Game quitGame = new Game(new ChooseDifficulty(), board, 0, playersRandomComp, true);
    KeyboardChar q = new KeyboardChar('q');
    KeyboardChar e = new KeyboardChar('e');
    KeyboardChar h = new KeyboardChar('h');
    KeyboardChar r = new KeyboardChar('r');
    KeyboardChar c = new KeyboardChar('c');
    Assert.assertEquals(quitGame, actualGame.handleKey(q));
    Assert.assertEquals(resetGame, actualGame.handleKey(r));
    Assert.assertEquals(actualGame, actualGame.handleKey(c));
    Assert.assertEquals(logicCompGame, actualGame.handleKey(h));
    Assert.assertEquals(randomCompGame, actualGame.handleKey(e));
  }
}
