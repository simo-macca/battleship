package project.model.phase;

import jtamaro.en.Sequence;
import jtamaro.en.io.KeyboardChar;
import org.junit.Assert;
import org.junit.Test;
import project.configuration.ConstantsUtils;
import project.model.Game;
import project.model.players.Player;
import project.ui.board.Board;

public class RoundEndedTest {

  @Test
  public void handleKeyTest() {
    Board board = new Board(ConstantsUtils.BOARD_LEFT_CORNER_CARTESIAN_POSITION); // 125,425
    Sequence<Player> playersUtent = ConstantsUtils.PLAYERS;
    Game defaultGame = new Game(new RoundEnded(), board, 0, playersUtent, false);
    Game quitGame = new Game(new RoundEnded(), board, 0, playersUtent, true);
    Game resetGame = new Game(new Mode(), board, 0, playersUtent, false);
    KeyboardChar q = new KeyboardChar('q');
    KeyboardChar r = new KeyboardChar('r');
    KeyboardChar a = new KeyboardChar('a');
    KeyboardChar u = new KeyboardChar('u');
    Assert.assertEquals(quitGame, defaultGame.handleKey(q));
    Assert.assertEquals(resetGame, defaultGame.handleKey(r));
    Assert.assertEquals(defaultGame, defaultGame.handleKey(a));
    Assert.assertNotEquals(resetGame, defaultGame.handleKey(u));
  }
}
