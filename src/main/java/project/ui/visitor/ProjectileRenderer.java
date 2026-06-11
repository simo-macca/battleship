package project.ui.visitor;

import static jtamaro.en.Colors.*;
import static jtamaro.en.Points.*;
import static jtamaro.en.Graphics.*;

import project.Toolbelt;
import jtamaro.en.Graphic;
import project.model.weapon.HitProjectile;
import project.model.weapon.MissProjectile;
import project.configuration.ConstantsUtils;

public class ProjectileRenderer implements ProjectileVisitor{

    @Override
    public Graphic visitHitProjectile(HitProjectile projectile) {
        Graphic cross = overlay(
            rotate(45, rectangle(2, 9, RED)),
            rotate(-45, rectangle(2, 9, RED))
        );
        Graphic background = overlay(cross, Toolbelt.circle(5.6, WHITE));
        return pin(CENTER, overlay(
            background,
            Toolbelt.circle(8, RED))
        );
    }

    @Override
    public Graphic visitMissProjectile(MissProjectile projectile) {
        double diagonal = ((ConstantsUtils.TILE_LENGTH - 8) * Math.sqrt(2)) - 5;
        return overlay(
            rotate(45, rectangle(2, diagonal, RED)),
            rotate(-45, rectangle(2, diagonal, RED))
        );
    }
}