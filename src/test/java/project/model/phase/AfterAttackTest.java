package project.model.phase;

import static jtamaro.en.Colors.BLUE;
import static jtamaro.en.Colors.GREEN;
import static jtamaro.en.Colors.MAGENTA;
import static jtamaro.en.Colors.YELLOW;
import static jtamaro.en.Sequences.cons;
import static jtamaro.en.Sequences.empty;
import static jtamaro.en.Sequences.of;

import jtamaro.en.Sequence;
import jtamaro.en.io.KeyboardChar;
import org.junit.Assert;
import org.junit.Test;
import project.configuration.ConstantsUtils;
import project.model.Game;
import project.model.Position;
import project.model.boat.Boat;
import project.model.players.Human;
import project.model.players.Player;
import project.model.players.RandomComputer;
import project.model.players.directions.Right;
import project.model.weapon.HitProjectile;
import project.model.weapon.MissProjectile;
import project.model.weapon.Projectile;
import project.model.weapon.SingleBomb;
import project.model.weapon.Weapon;
import project.ui.board.Board;

public class AfterAttackTest {

  // Position startingPosition, Sequence<Position> hittablePositions, boolean sunk, boolean
  // isVertical, int numberOfUnits

  // PHASE
  private static final Phase RESET_PHASE = new Mode();
  private static final Phase NEXT_PHASE = new ChooseWeapon();
  private static final Phase ACTUAL_PHASE = new AfterAttack();
  // BOARD
  private static final Board BOARD =
      new Board(ConstantsUtils.BOARD_LEFT_CORNER_CARTESIAN_POSITION); // 125,425
  // POSITION
  private static final Position HIT_POSITION = new Position(126, 424);
  private static final Position SUBMARINE_POS = new Position(150, 375);
  private static final Position BATTLESHIP_POS = new Position(175, 350);
  private static final Position CARRIER_POS = new Position(200, 325);
  private static final Position MISS_POSITION = new Position(250, 225);
  // DEFAULT BOAT
  private static final Boat DESTROYER =
      new Boat(
          HIT_POSITION,
          Boat.startingHittablePositions(HIT_POSITION, 2, false),
          false,
          false,
          2,
          GREEN); // 2 units
  private static final Boat SUBMARINE =
      new Boat(
          SUBMARINE_POS,
          Boat.startingHittablePositions(SUBMARINE_POS, 3, false),
          false,
          false,
          3,
          BLUE); // 3 units
  private static final Boat BATTLESHIP =
      new Boat(
          BATTLESHIP_POS,
          Boat.startingHittablePositions(BATTLESHIP_POS, 4, false),
          false,
          false,
          4,
          MAGENTA); // 4 units
  private static final Boat CARRIER =
      new Boat(
          CARRIER_POS,
          Boat.startingHittablePositions(CARRIER_POS, 5, false),
          false,
          false,
          5,
          YELLOW); // 5 units
  private static final Sequence<Boat> BOATS = of(DESTROYER, SUBMARINE, BATTLESHIP, CARRIER);
  // UPDATE BOAT
  private static final Boat UPDATE_BOAT =
      new Boat(
          HIT_POSITION,
          Boat.startingHittablePositions(HIT_POSITION, 2, false).rest(),
          false,
          false,
          2,
          GREEN);
  private static final Sequence<Boat> UPDATE_BOATS = cons(UPDATE_BOAT, BOATS.rest());
  // DEFAULT WEAPON
  private static final Weapon DEFAULT_WEAPON =
      new SingleBomb(ConstantsUtils.WEAPON_BUTTON_POSITION, HIT_POSITION);
  private static final Sequence<Projectile> HIT_PROJECTILES = of(new HitProjectile(HIT_POSITION));
  private static final Sequence<Projectile> MISS_PROJECTILES =
      of(new MissProjectile(MISS_POSITION));
  // DEFAULT PLAYER
  private static final Player G1 = new Human("G1", BOATS, DEFAULT_WEAPON, HIT_PROJECTILES);
  private static final Player G2 = new Human("G2", BOATS, DEFAULT_WEAPON, MISS_PROJECTILES);
  private static final Player COM =
      new RandomComputer(
          "COM",
          BOATS,
          DEFAULT_WEAPON,
          MISS_PROJECTILES,
          new Right(),
          false,
          new Position(0, 0),
          ConstantsUtils.HITTABLEPOSITIONS);
  private static final Sequence<Player> PLAYERS = of(G1, G2, COM);
  // update PLAYER
  private static final Player UPDATE_PLAYER =
      new Human("G2", UPDATE_BOATS, DEFAULT_WEAPON, MISS_PROJECTILES);
  private static final Sequence<Player> UPDATE_PLAYERS = of(G1, UPDATE_PLAYER, COM);
  // DEFAULT GAME
  public static final Game ACTUAL_HIT_GAME = new Game(ACTUAL_PHASE, BOARD, 0, PLAYERS, false);
  private static final Game QUIT_GAME = new Game(ACTUAL_PHASE, BOARD, 0, PLAYERS, true);
  public static final Game ACTUAL_MISS_GAME = new Game(ACTUAL_PHASE, BOARD, 1, PLAYERS, false);
  private static final Game RESET_GAME =
      new Game(RESET_PHASE, BOARD, 0, ConstantsUtils.PLAYERS, false);
  // update GAME
  public static final Game NEXT_HIT_GAME = new Game(NEXT_PHASE, BOARD, 0, UPDATE_PLAYERS, false);
  public static final Game NEXT_MISS_GAME = new Game(NEXT_PHASE, BOARD, 1, PLAYERS, false);
  // KEYBOARDCHAR
  private static final KeyboardChar Q = new KeyboardChar('q');
  private static final KeyboardChar R = new KeyboardChar('r');
  private static final KeyboardChar N = new KeyboardChar('n');
  private static final KeyboardChar S = new KeyboardChar('s');

  @Test
  public void handleKeyTest() {
    Assert.assertEquals(QUIT_GAME, ACTUAL_HIT_GAME.handleKey(Q));
    Assert.assertEquals(RESET_GAME, ACTUAL_HIT_GAME.handleKey(R));
    Assert.assertEquals(NEXT_HIT_GAME, ACTUAL_HIT_GAME.handleKey(N));
    Assert.assertEquals(ACTUAL_HIT_GAME, ACTUAL_HIT_GAME.handleKey(S));
    Assert.assertEquals(NEXT_MISS_GAME, ACTUAL_MISS_GAME.handleKey(N));
  }

  @Test
  public void hitTest() {
    Assert.assertTrue(Player.hit(ACTUAL_HIT_GAME));
    Assert.assertFalse(Player.hit(ACTUAL_MISS_GAME));
  }

  @Test
  public void updatePlayerTest() {
    Assert.assertEquals(RESET_GAME, ACTUAL_HIT_GAME.handleKey(R));
    Assert.assertEquals(NEXT_HIT_GAME, ACTUAL_HIT_GAME.handleKey(N));
    Assert.assertEquals(NEXT_MISS_GAME, ACTUAL_MISS_GAME.handleKey(N));
  }

  @Test
  public void updateBoatsTest() {
    Assert.assertEquals(BOATS, ACTUAL_MISS_GAME.handleKey(N).getOpponent().boats());
    Assert.assertEquals(BOATS, ACTUAL_HIT_GAME.handleKey(N).currentPlayer().boats());
    Assert.assertEquals(UPDATE_BOATS, ACTUAL_HIT_GAME.handleKey(N).getOpponent().boats());
    Assert.assertEquals(empty(), ACTUAL_HIT_GAME.handleKey(R).getOpponent().boats());
    Assert.assertEquals(empty(), ACTUAL_HIT_GAME.handleKey(R).currentPlayer().boats());
  }

  @Test
  public void nextPlayerTest() {
    Assert.assertEquals(NEXT_HIT_GAME, ACTUAL_HIT_GAME.currentPlayer().attacking(ACTUAL_HIT_GAME));
    Assert.assertEquals(
        NEXT_MISS_GAME, ACTUAL_MISS_GAME.currentPlayer().attacking(ACTUAL_MISS_GAME));
  }
}
