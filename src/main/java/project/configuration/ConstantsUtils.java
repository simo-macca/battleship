package project.configuration;

import static jtamaro.en.Colors.*;
import static jtamaro.en.Graphics.*;
import static jtamaro.en.Sequences.*;

import jtamaro.en.Graphic;
import jtamaro.en.Sequence;
import project.Toolbelt;
import project.model.Position;
import project.model.boat.*;
import project.model.players.Human;
import project.model.players.LogicComputer;
import project.model.players.Player;
import project.model.players.RandomComputer;
import project.model.players.directions.Right;
import project.model.weapon.SingleBomb;
import project.model.weapon.Weapon;
import project.ui.cartesianWorldGraphics.CartesianWorldPosition;

public final class ConstantsUtils {

  // ------------------------- Board Configurations --------------------------------
  // Position of the top left corner of the board in the cartesian world
  // To be changed when implementing graphics
  public static final Position BOARD_LEFT_CORNER_POSITION =
      new Position(125, 425); // NONONONONO TOCCCARE SE NO TE MENO DI BOTTE
  public static final CartesianWorldPosition BOARD_LEFT_CORNER_CARTESIAN_POSITION =
      new CartesianWorldPosition(125, 425); // NONONONONO TOCCCARE SE NO TE MENO DI BOTTE
  // Length of a tile of the board
  public static final int TILE_LENGTH = 25;

  // ------------------------- Weapons Configurations ------------------------------
  // Background of the weapons button
  public static final int WEAPON_BACKGROUND_LENGTH = 70;
  public static final Graphic WEAPON_BACKGROUND =
      overlay(
          Toolbelt.square(WEAPON_BACKGROUND_LENGTH - 10, WHITE),
          Toolbelt.square(WEAPON_BACKGROUND_LENGTH, rgb(100, 100, 100)));
  // Position of the weapon button on the chosen weapon phase
  public static final Position WEAPON_BUTTON_POSITION = new Position(30, 160);

  // ------------------------- Initial Game Settings ------------------------------
  // initial set of player and boats
  public static final Position DEFAULT_POSITION_DESTROYER = new Position(20, 140);
  public static final Boat DESTROYER =
      new Boat(
          DEFAULT_POSITION_DESTROYER,
          Boat.startingHittablePositions(DEFAULT_POSITION_DESTROYER, 2, false),
          true,
          false,
          2,
          GREEN);

  public static final Position DEFAULT_POSITION_SUBMARINE = new Position(20, 105);
  public static final Boat SUBMARINE =
      new Boat(
          DEFAULT_POSITION_SUBMARINE,
          Boat.startingHittablePositions(DEFAULT_POSITION_SUBMARINE, 3, false),
          true,
          false,
          3,
          BLUE);

  public static final Position DEFAULT_POSITION_BATTLESHIP = new Position(20, 70);
  public static final Boat BATTLESHIP =
      new Boat(
          DEFAULT_POSITION_BATTLESHIP,
          Boat.startingHittablePositions(DEFAULT_POSITION_BATTLESHIP, 4, false),
          true,
          false,
          3,
          MAGENTA);

  public static final Position DEFAULT_POSITION_CARRIER = new Position(20, 35);
  public static final Boat CARRIER =
      new Boat(
          DEFAULT_POSITION_CARRIER,
          Boat.startingHittablePositions(DEFAULT_POSITION_CARRIER, 5, false),
          true,
          false,
          3,
          rgb(255, 165, 0));

  public static final Sequence<Boat> BOATS = of(DESTROYER, SUBMARINE, BATTLESHIP, CARRIER);

  public static final Sequence<Sequence<Position>> HITTABLEPOSITIONS = Position.hittablePositions();

  public static final Weapon DEFAULT_WEAPON =
      new SingleBomb(WEAPON_BUTTON_POSITION, WEAPON_BUTTON_POSITION);
  public static final Player G1 =
      new Human("Captain Jack Sparrow", empty(), DEFAULT_WEAPON, empty());
  public static final Player G2 =
      new Human("Captain David Jones", empty(), DEFAULT_WEAPON, empty());
  public static final Player RANDOM_COMP =
      new RandomComputer(
          "Captain Random Computer",
          empty(),
          DEFAULT_WEAPON,
          empty(),
          new Right(),
          false,
          new Position(0, 0),
          HITTABLEPOSITIONS);
  public static final Player LOGIC_COMP =
      new LogicComputer(
          "Captain Logic Computer",
          empty(),
          DEFAULT_WEAPON,
          empty(),
          new Right(),
          false,
          new Position(0, 0),
          HITTABLEPOSITIONS);
  public static final Sequence<Player> PLAYERS = of(G1, G2);
  public static final Sequence<Player> VS_RANDOM_COMP = of(G1, RANDOM_COMP);
  public static final Sequence<Player> VS_LOGIC_COMP = of(G1, LOGIC_COMP);

  // ------------------------- Graphics configurations -----------------------------
  public static final int WIDTH = 500;
  public static final int HEIGTH = 600;
}
