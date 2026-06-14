package project.model.weapon;

import static project.ui.cartesianWorldGraphics.CartesianWorldPosition.*;

import jtamaro.en.Graphic;
import project.model.Position;
import project.ui.visitor.ProjectileVisitor;
import project.configuration.ConstantsUtils;
import project.ui.cartesianWorldGraphics.CartesianWorldPosition;

/**
 * it represents the miss projectile ❌
 * @param position that corresponds to where the projectile is positioned in the background
 */

public record MissProjectile(Position position) implements Projectile {

    @Override
    public CartesianWorldPosition renderingPosition(){
        CartesianWorldPosition position = positionToPhysicalBoardPosition(directConversion(ConstantsUtils.BOARD_LEFT_CORNER_POSITION), this.position(), ConstantsUtils.TILE_LENGTH);
        return new CartesianWorldPosition(position.x() + ConstantsUtils.TILE_LENGTH/2, position.y() - ConstantsUtils.TILE_LENGTH/2);
    }

    @Override
    public Graphic acceptVisitor(ProjectileVisitor v){
        return v.visitMissProjectile(this);
    }
}