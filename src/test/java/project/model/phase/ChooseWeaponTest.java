package project.model.phase;

import static jtamaro.en.Sequences.empty;
import static jtamaro.en.Sequences.of;

import jtamaro.en.Sequence;
import jtamaro.en.io.KeyboardChar;
import org.junit.Assert;
import org.junit.Test;
import project.configuration.ConstantsUtils;
import project.model.Game;
import project.model.Position;
import project.model.players.Human;
import project.model.players.Player;
import project.model.weapon.SingleBomb;
import project.model.weapon.Weapon;
import project.ui.board.Board;

public class ChooseWeaponTest {

  // BOARD
  private static final Board BOARD =
      new Board(ConstantsUtils.BOARD_LEFT_CORNER_CARTESIAN_POSITION); // 125,425
  // WEAPON
  private static final Weapon SINGLEBOMB =
      new SingleBomb(ConstantsUtils.WEAPON_BUTTON_POSITION, new Position(0, 0));
  // PLAYER
  private static final Human START_PLAYER =
      new Human(ConstantsUtils.G1.playerName(), empty(), SINGLEBOMB, empty());
  private static final Sequence<Player> START_UTENT = ConstantsUtils.PLAYERS;
  private static final Sequence<Player> PLAYER_START = of(START_PLAYER, ConstantsUtils.G2);
  // GAME
  private static final Game DEFAULT_GAME =
      new Game(new ChooseWeapon(), BOARD, 0, START_UTENT, false);
  private static final Game QUIT_GAME = new Game(new ChooseWeapon(), BOARD, 0, START_UTENT, true);
  private static final Game START_GAME =
      new Game(new ChoosePosition(), BOARD, 0, PLAYER_START, false);

  @Test
  public void handleKeyTest() {
    Game resetGame = new Game(new Mode(), BOARD, 0, START_UTENT, false);
    KeyboardChar q = new KeyboardChar('q');
    KeyboardChar s = new KeyboardChar('s');
    KeyboardChar r = new KeyboardChar('r');
    KeyboardChar c = new KeyboardChar('c');
    Assert.assertEquals(QUIT_GAME, DEFAULT_GAME.handleKey(q));
    Assert.assertEquals(START_GAME, DEFAULT_GAME.handleKey(s));
    Assert.assertEquals(resetGame, DEFAULT_GAME.handleKey(r));
    Assert.assertEquals(DEFAULT_GAME, DEFAULT_GAME.handleKey(c));
  }

  @Test
  public void startTurnsTest() {
    Assert.assertEquals(START_GAME, DEFAULT_GAME.currentPlayer().startPlayerTurns(DEFAULT_GAME));
  }
}
