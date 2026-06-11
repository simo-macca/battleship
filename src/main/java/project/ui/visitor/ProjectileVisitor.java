package project.ui.visitor;

import jtamaro.en.Graphic;
import project.model.weapon.HitProjectile;
import project.model.weapon.MissProjectile;

public interface ProjectileVisitor {
    
    public Graphic visitHitProjectile(HitProjectile projectile);
    public Graphic visitMissProjectile(MissProjectile projectile);
}