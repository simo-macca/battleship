package project.model.players;

import static jtamaro.en.Sequences.of;

import org.junit.Assert;
import org.junit.Test;
import jtamaro.en.Sequence;
import project.configuration.ConstantsUtils;
import project.model.Position;
import project.model.players.directions.Right;
import project.model.weapon.HitProjectile;
import project.model.weapon.Projectile;
import project.model.weapon.SingleBomb;
import project.model.weapon.Weapon;

public class RandomComputerTest {

    @Test public void updateCompTest() {
        Position hitPos = new Position(0, 0);
        Player oldComp = new RandomComputer("comp", ConstantsUtils.BOATS, ConstantsUtils.DEFAULT_WEAPON, of(new HitProjectile(hitPos)), new Right(), false, hitPos, of(of(hitPos, new Position(1, 0))));
        Weapon currentWeapon = new SingleBomb(ConstantsUtils.WEAPON_BUTTON_POSITION, hitPos);
        Sequence<Projectile> projectiles = of(new HitProjectile(hitPos));
        Sequence<Sequence<Position>> positions = of(of(new Position(1, 0)));
        Player newComp = new RandomComputer("comp", ConstantsUtils.BOATS, currentWeapon, projectiles, new Right(), false, hitPos, positions);
        Assert.assertEquals(newComp.playerName(), oldComp.updateComp(currentWeapon, projectiles, positions).playerName());
        Assert.assertEquals(newComp.boats(), oldComp.updateComp(currentWeapon, projectiles, positions).boats());
        Assert.assertEquals(newComp.selectedWeapon(), oldComp.updateComp(currentWeapon, projectiles, positions).selectedWeapon());
        Assert.assertEquals(newComp.weapons(), oldComp.updateComp(currentWeapon, projectiles, positions).weapons());
    }

    @Test public void updateOpponent() {
        Position hitPos = new Position(0, 0);
        Player oldComp = new RandomComputer("comp", ConstantsUtils.BOATS, ConstantsUtils.DEFAULT_WEAPON, of(new HitProjectile(hitPos)), new Right(), false, hitPos, of(of(hitPos, new Position(1, 0))));
        Weapon currentWeapon = new SingleBomb(ConstantsUtils.WEAPON_BUTTON_POSITION, hitPos);
        Player newComp = new RandomComputer("comp", ConstantsUtils.BOATS.rest(), currentWeapon, of(new HitProjectile(hitPos)), new Right(), false, hitPos, of(of(hitPos, new Position(1, 0))));
        Assert.assertEquals(newComp.playerName(), oldComp.updateOpponent(oldComp, ConstantsUtils.BOATS.rest(), currentWeapon).playerName());
        Assert.assertEquals(newComp.boats(), oldComp.updateOpponent(oldComp, ConstantsUtils.BOATS.rest(), currentWeapon).boats());
        Assert.assertEquals(newComp.selectedWeapon(), oldComp.updateOpponent(oldComp, ConstantsUtils.BOATS.rest(), currentWeapon).selectedWeapon());
        Assert.assertEquals(newComp.weapons(), oldComp.updateOpponent(oldComp, ConstantsUtils.BOATS.rest(), currentWeapon).weapons());
    }
}