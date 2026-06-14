package project.model.weapon;

import jtamaro.en.Graphic;
import jtamaro.en.Sequence;
import project.model.Position;

public interface Weapon {

  /**
   * it's used to render the graphic of the weapon
   *
   * @return the image of the weapon
   */
  public Graphic render();

  /**
   * it is used to know how many projectiles the weapon has composed
   *
   * @return the number of the projectiles
   */
  public int numberProjectiles();

  /**
   * it is used to know where the button is on the cartesian world
   *
   * @return the position of the button
   */
  public Position buttonPosition();

  /**
   * it uses to know where the player has clicked
   *
   * @return a sequence of position selected by the player
   */
  public Sequence<Position> positionsHit();

  /**
   * it's used to know all the position of all projectiles
   *
   * @param hittables which corresponds to all the positions that the player clicked on
   * @return a sequence of all bullets, which can be hit or miss
   */
  public Sequence<Projectile> projectiles(
      Sequence<Position> pos, Sequence<Position> hittables, Sequence<Projectile> alreadyHit);

  /**
   * create a new weapon with the new attack position
   *
   * @param position the new attack position
   * @return a new weapon
   */
  public Weapon weaponWithCorrectPlacement(Position position);
}
