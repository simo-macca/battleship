package project.model.weapon;

import static jtamaro.en.Colors.*;
import static jtamaro.en.Graphics.*;
import static jtamaro.en.Points.*;
import static jtamaro.en.Sequences.*;

import jtamaro.en.Graphic;
import jtamaro.en.Sequence;
import project.Toolbelt;
import project.configuration.ConstantsUtils;
import project.model.Position;

/**
 * it represents the default weapons with a single projectile
 *
 * @param buttonPosition that corresponds to where the button is positioned in the background
 * @param attackPosition that corresponds to the position that the player just selected
 */
public record SingleBomb(Position buttonPosition, Position attackPosition) implements Weapon {

  private static final int LENGTH = ConstantsUtils.WEAPON_BACKGROUND_LENGTH;

  @Override
  public int numberProjectiles() {
    return 1;
  }

  @Override
  public Sequence<Position> positionsHit() {
    return of(attackPosition);
  }

  @Override
  public Sequence<Projectile> projectiles(
      Sequence<Position> pos, Sequence<Position> hittables, Sequence<Projectile> alreadyHit) {
    Sequence<Position> h = Toolbelt.map((a) -> a.position(), alreadyHit);
    return isEmpty(pos)
        ? alreadyHit
        : !Toolbelt.isInSequence(first(pos), h)
            ? Toolbelt.isInSequence(first(pos), hittables)
                ? cons(new HitProjectile(first(pos)), projectiles(rest(pos), hittables, alreadyHit))
                : cons(
                    new MissProjectile(first(pos)), projectiles(rest(pos), hittables, alreadyHit))
            : projectiles(rest(pos), hittables, alreadyHit);
  }

  @Override
  public Weapon weaponWithCorrectPlacement(Position position) {
    return new SingleBomb(buttonPosition, position);
  }

  @Override
  public Graphic render() {
    // creation all component for the fuse
    Sequence<Graphic> explosions =
        replicate(pin(CENTER_RIGHT, rectangle(LENGTH / 5, LENGTH / 15, rgb(211, 118, 52))), 8);
    Graphic explosion =
        Toolbelt.composes(
            map(a -> rotate(a.first(), a.second()), zip(range(0, 181, 45), explosions)));
    // the explosion of the fuse
    Graphic explosionFuse =
        compose(
            circularSector(LENGTH / 10, 180, WHITE), pin(BOTTOM_CENTER, rotate(180, explosion)));
    // the rope of the fuse
    Graphic ropeFuse =
        rotate(
            45,
            pin(
                CENTER_RIGHT,
                rectangle((LENGTH * 7 / 20) * Math.sqrt(2) - 5, LENGTH / 10, rgb(80, 80, 80))));
    // composition of the fuse
    Graphic allFuse = compose(pin(CENTER, rotate(-45, explosionFuse)), ropeFuse);
    // composition of the bomb
    Graphic bomb =
        compose(
            Toolbelt.circle(LENGTH * 3 / 10, BLACK), // rgb(70, 70, 70)),
            pin(BOTTOM_LEFT, allFuse));
    return pin(TOP_LEFT, overlay(bomb, ConstantsUtils.WEAPON_BACKGROUND));
  }
}
