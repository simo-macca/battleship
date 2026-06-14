package project.model.weapon;

import static project.ui.cartesianWorldGraphics.CartesianWorldPosition.*;

import jtamaro.en.Graphic;
import project.configuration.ConstantsUtils;
import project.model.Position;
import project.ui.cartesianWorldGraphics.CartesianWorldPosition;
import project.ui.visitor.ProjectileVisitor;

/**
 * it represents the hit projectile ⭕️
 *
 * @param position that corresponds to where the projectile is positioned in the background
 */
public record HitProjectile(Position position) implements Projectile {

  @Override
  public CartesianWorldPosition renderingPosition() {
    CartesianWorldPosition position =
        positionToPhysicalBoardPosition(
            directConversion(ConstantsUtils.BOARD_LEFT_CORNER_POSITION),
            this.position(),
            ConstantsUtils.TILE_LENGTH);
    return new CartesianWorldPosition(position.x() + 25 / 2, position.y() - 25 / 2);
  }

  @Override
  public Graphic acceptVisitor(ProjectileVisitor v) {
    return v.visitHitProjectile(this);
  }
}
