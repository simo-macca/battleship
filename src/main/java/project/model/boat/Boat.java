package project.model.boat;

import static jtamaro.en.Sequences.concat;
import static jtamaro.en.Sequences.cons;
import static jtamaro.en.Sequences.empty;
import static jtamaro.en.Sequences.first;
import static jtamaro.en.Sequences.isEmpty;
import static jtamaro.en.Sequences.rest;

import jtamaro.en.Color;
import jtamaro.en.Sequence;
import project.Toolbelt;
import project.model.Position;
import project.ui.boat.BoatGraphics;
import project.ui.cartesianWorldGraphics.CartesianWorldPosition;

/**
 * Boats represent all the boat, both utents and computers. All of them have:
 *
 * @param startingPosition the first position of the first component of the boat
 * @param hittablePositions all the possible hittable positions of this boat
 * @param isSunk if it has sunken or no
 * @param isVertical if is vertical or no
 * @param numberOfUnits the number of the boats' unit
 * @param color the color of the boat
 */
public record Boat(
    Position startingPosition,
    Sequence<Position> hittablePositions,
    boolean isSunk,
    boolean isVertical,
    int numberOfUnits,
    Color color) {

  /**
   * give all the possible hittable positions in according to the sequence of boats
   *
   * @param boats sequence of boats whose hittable positions you want to know about
   * @return all the possible hittable positions of all the boats
   */
  public static Sequence<Position> allHittablePositionsOnBoard(Sequence<Boat> boats) {
    return isEmpty(boats)
        ? empty()
        : concat(first(boats).hittablePositions(), allHittablePositionsOnBoard(rest(boats)));
  }

  /**
   * give all the possible hittable positions in according to the first position
   *
   * @param startingPosition the first position
   * @param boatLength the length of the boat
   * @param vertical if it's vertical
   * @return all the possible hittable positions in according to the first position
   */
  public static Sequence<Position> startingHittablePositions(
      Position startingPosition, int boatLength, boolean vertical) {
    Position nextPos =
        vertical
            ? new Position(startingPosition.x(), startingPosition.y() + 1)
            : new Position(startingPosition.x() + 1, startingPosition.y());
    return cons(
        startingPosition,
        boatLength == 1 ? empty() : startingHittablePositions(nextPos, boatLength - 1, vertical));
  }

  /**
   * it uses to know if the boat is hit
   *
   * @param pos a board position
   * @return if the boat is hit or no
   */
  public boolean hit(Position pos) {
    return Toolbelt.isInSequence(pos, hittablePositions);
  }

  /**
   * it uses to know if the boat is sunken or no
   *
   * @return if the boat is sunken or no
   */
  public Boat sunken() {
    return hittablePositions.isEmpty() ? sunkBoat() : this;
  }

  /**
   * it uses to update the hittable position of this boat
   *
   * @param hitPos a new sequence of hittable position
   * @return a new boat with the hittable position of this boat updated
   */
  public Boat updateHittablePos(Sequence<Position> hitPos) {
    return new Boat(
        this.startingPosition,
        hitPos,
        this.isSunk,
        this.isVertical,
        this.numberOfUnits,
        this.color);
  }

  /**
   * it uses to convert the logic boat on a graphic boat
   *
   * @param position a position where the boat is
   * @return a new BoatGraphic with the same parameter of this boat
   */
  public BoatGraphics fromLogicBoatToBoatGraphic(CartesianWorldPosition position) {
    return new BoatGraphics(
        position,
        BoatGraphics.boatGraphic(numberOfUnits, isSunk, isVertical, color),
        isSunk,
        isVertical,
        numberOfUnits,
        color);
  }

  /**
   * it uses to create a sunk boat
   *
   * @return this sunken boat
   */
  public Boat sunkBoat() {
    return new Boat(startingPosition, hittablePositions, true, isVertical, numberOfUnits, color);
  }
}
