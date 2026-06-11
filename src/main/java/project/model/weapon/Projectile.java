package project.model.weapon;

import jtamaro.en.Graphic;
import project.model.Position;
import project.ui.visitor.ProjectileVisitor;
import project.ui.cartesianWorldGraphics.CartesianWorldPosition;

public interface Projectile {

    /**
     * it uses to render the graphic of the projectile
     * @return the image of the hit or miss projectile
     */
    public Graphic acceptVisitor(ProjectileVisitor v);

    /**
     * It is used to know where the projectile is on the Board, to know if the player has hit any boats
     * @return the position of the projectile
     */
    public Position position();

    /**
     * it is used to know where the projectile is on the cartesian world to render it
     * @return the position of the projectile
     */
    public CartesianWorldPosition renderingPosition();
}