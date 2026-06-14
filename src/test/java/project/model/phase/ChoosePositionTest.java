package project.model.phase;

import static jtamaro.en.Colors.GREEN;
import static jtamaro.en.Sequences.of;

import jtamaro.en.Sequence;
import jtamaro.en.io.Coordinate;
import jtamaro.en.io.KeyboardChar;
import org.junit.Assert;
import org.junit.Test;
import project.configuration.ConstantsUtils;
import project.model.Game;
import project.model.Position;
import project.model.boat.Boat;
import project.model.players.Human;
import project.model.players.Player;
import project.model.weapon.HitProjectile;
import project.model.weapon.SingleBomb;
import project.model.weapon.Weapon;
import project.ui.board.Board;

public class ChoosePositionTest {

  @Test
  public void handleMouseTest() {
    Coordinate coordOut1 = new Coordinate(0, 0);
    Coordinate coordOut2 = new Coordinate(100, 340);
    Coordinate coordOut3 = new Coordinate(230, 524);
    Coordinate coordIn1 = new Coordinate(126, 424);
    Coordinate coordIn2 = new Coordinate(300, 176);
    Board board = new Board(ConstantsUtils.BOARD_LEFT_CORNER_CARTESIAN_POSITION); // 125,425
    Position pos = Position.coordinateToPosition(coordIn1);
    Weapon singleBomb = new SingleBomb(ConstantsUtils.WEAPON_BUTTON_POSITION, pos);
    Boat newDestroyer =
        new Boat(pos, Boat.startingHittablePositions(pos, 2, false), false, false, 2, GREEN);
    Sequence<Boat> newBoats =
        of(
            newDestroyer,
            ConstantsUtils.SUBMARINE,
            ConstantsUtils.BATTLESHIP,
            ConstantsUtils.CARRIER);
    Player newPlayer =
        new Human(ConstantsUtils.G1.playerName(), newBoats, singleBomb, of(new HitProjectile(pos)));
    Sequence<Player> players = of(newPlayer, ConstantsUtils.G2);
    Game defaultGame = new Game(new ChoosePosition(), board, 0, players, false);
    Game inGame = new Game(new AfterAttack(), board, 0, players, false);
    Assert.assertNotEquals(inGame, defaultGame.handleMouse(coordOut2));
    Assert.assertEquals(defaultGame, defaultGame.handleMouse(coordOut1));
    Assert.assertEquals(defaultGame, defaultGame.handleMouse(coordOut3));
    Assert.assertEquals(inGame.roundPhase(), defaultGame.handleMouse(coordIn1).roundPhase());
    Assert.assertEquals(inGame.roundPhase(), defaultGame.handleMouse(coordIn2).roundPhase());
  }

  @Test
  public void handleKeyTest() {
    Board board = new Board(ConstantsUtils.BOARD_LEFT_CORNER_CARTESIAN_POSITION); // 125,425
    Sequence<Player> players = ConstantsUtils.PLAYERS;
    Game defaultGame = new Game(new ChoosePosition(), board, 0, players, false);
    Game resetGame = new Game(new Mode(), board, 0, players, false);
    Game quitGame = new Game(new ChoosePosition(), board, 0, players, true);
    KeyboardChar q = new KeyboardChar('q');
    KeyboardChar r = new KeyboardChar('r');
    KeyboardChar l = new KeyboardChar('l');
    KeyboardChar s = new KeyboardChar('s');
    KeyboardChar a = new KeyboardChar('a');
    Assert.assertEquals(quitGame, defaultGame.handleKey(q));
    Assert.assertEquals(resetGame, defaultGame.handleKey(r));
    Assert.assertEquals(defaultGame, defaultGame.handleKey(l));
    Assert.assertNotEquals(resetGame, defaultGame.handleKey(s));
    Assert.assertNotEquals(resetGame, defaultGame.handleKey(a));
  }
}
