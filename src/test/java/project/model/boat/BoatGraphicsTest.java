package project.model.boat;

import static jtamaro.en.Colors.BLUE;
import static jtamaro.en.Colors.GREEN;
import static jtamaro.en.Colors.MAGENTA;
import static jtamaro.en.Colors.YELLOW;
import static jtamaro.en.Sequences.of;

import jtamaro.en.Sequence;
import org.junit.Assert;
import org.junit.Test;
import project.Toolbelt;
import project.configuration.ConstantsUtils;
import project.ui.boat.BoatGraphics;
import project.ui.cartesianWorldGraphics.CartesianWorldPosition;

public class BoatGraphicsTest {

  private static final BoatGraphics DESTROYER_H =
      new BoatGraphics(
          new CartesianWorldPosition(0, 0),
          Toolbelt.square(ConstantsUtils.TILE_LENGTH, GREEN),
          false,
          false,
          2,
          GREEN);
  private static final BoatGraphics DESTROYER_V =
      new BoatGraphics(
          new CartesianWorldPosition(238, 243),
          Toolbelt.square(ConstantsUtils.TILE_LENGTH, GREEN),
          false,
          true,
          2,
          GREEN);
  private static final BoatGraphics SUBMARINE_H =
      new BoatGraphics(
          new CartesianWorldPosition(127, 342),
          Toolbelt.square(ConstantsUtils.TILE_LENGTH, BLUE),
          false,
          false,
          3,
          BLUE);
  private static final BoatGraphics SUBMARINE_V =
      new BoatGraphics(
          new CartesianWorldPosition(246, 255),
          Toolbelt.square(ConstantsUtils.TILE_LENGTH, BLUE),
          false,
          true,
          3,
          BLUE);
  private static final BoatGraphics BATTLESHIP_H =
      new BoatGraphics(
          new CartesianWorldPosition(139, 327),
          Toolbelt.square(ConstantsUtils.TILE_LENGTH, YELLOW),
          false,
          false,
          4,
          YELLOW);
  private static final BoatGraphics BATTLESHIP_V =
      new BoatGraphics(
          new CartesianWorldPosition(300, 400),
          Toolbelt.square(ConstantsUtils.TILE_LENGTH, YELLOW),
          false,
          true,
          4,
          YELLOW);
  private static final BoatGraphics CARRIER_H =
      new BoatGraphics(
          new CartesianWorldPosition(200, 350),
          Toolbelt.square(ConstantsUtils.TILE_LENGTH, MAGENTA),
          false,
          false,
          5,
          MAGENTA);
  private static final BoatGraphics CARRIER_V =
      new BoatGraphics(
          new CartesianWorldPosition(177, 260),
          Toolbelt.square(ConstantsUtils.TILE_LENGTH, MAGENTA),
          false,
          true,
          5,
          MAGENTA);
  public static final Sequence<BoatGraphics> BOATS_H =
      of(DESTROYER_H, SUBMARINE_H, BATTLESHIP_H, CARRIER_H);
  public static final Sequence<BoatGraphics> BOATS_V =
      of(DESTROYER_V, SUBMARINE_V, BATTLESHIP_V, CARRIER_V);

  @Test
  public void isMouseOverBoatTest() {
    Assert.assertFalse(DESTROYER_H.isMouseOverBoat(new CartesianWorldPosition(1, 1)));
  }
}
