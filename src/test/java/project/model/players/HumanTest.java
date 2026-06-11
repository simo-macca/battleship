package project.model.players;

import static jtamaro.en.Sequences.of;

import org.junit.Assert;
import org.junit.Test;
import project.configuration.ConstantsUtils;
import project.model.Game;
import project.model.Position;
import project.model.phase.AfterAttack;
import project.model.phase.AfterAttackTest;
import project.model.phase.ChoosePosition;
import project.model.phase.ChooseWeapon;
import project.model.weapon.HitProjectile;
import project.model.weapon.SingleBomb;
import project.model.weapon.Weapon;
import project.ui.board.Board;

public class HumanTest {
    
    private static final Weapon SINGLEBOMB = new SingleBomb(ConstantsUtils.WEAPON_BUTTON_POSITION, new Position(0, 0));

    @Test
    public void startPlayerTurnsTest() {
        Board board = new Board(ConstantsUtils.BOARD_LEFT_CORNER_CARTESIAN_POSITION);
        Player oldPlayer = new Human("G1", ConstantsUtils.BOATS, ConstantsUtils.DEFAULT_WEAPON, of(new HitProjectile(new Position(0, 0))));
        Game oldGame = new Game(new ChooseWeapon(), board, 0, of(oldPlayer, ConstantsUtils.G2), false);
        Player newPlayer = new Human("G1", ConstantsUtils.BOATS, SINGLEBOMB, of(new HitProjectile(new Position(0, 0))));
        Game newGame = new Game(new ChoosePosition(), board, 0, of(newPlayer, ConstantsUtils.G2), false);
        Assert.assertEquals(newGame, oldPlayer.startPlayerTurns(oldGame));
        Assert.assertEquals(oldGame, oldPlayer.startCompTurns(oldGame));
    }

    @Test
    public void attackingTest() {
        Assert.assertEquals(AfterAttackTest.NEXT_HIT_GAME, AfterAttackTest.ACTUAL_HIT_GAME.currentPlayer().attacking(AfterAttackTest.ACTUAL_HIT_GAME));
        Assert.assertEquals(AfterAttackTest.NEXT_MISS_GAME, AfterAttackTest.ACTUAL_MISS_GAME.currentPlayer().attacking(AfterAttackTest.ACTUAL_MISS_GAME));
    }

    @Test public void updateOpponent() {
        Position hitPos = new Position(30, 160);
        Player oldComp = new Human("G1", ConstantsUtils.BOATS, ConstantsUtils.DEFAULT_WEAPON, of(new HitProjectile(hitPos)));
        Weapon currentWeapon = new SingleBomb(ConstantsUtils.WEAPON_BUTTON_POSITION, hitPos);
        Player newComp = new Human("G1", ConstantsUtils.BOATS.rest(), currentWeapon, of(new HitProjectile(hitPos)));
        Assert.assertEquals(newComp, oldComp.updateOpponent(oldComp, ConstantsUtils.BOATS.rest(), currentWeapon));
    }

    @Test
    public void nextGameTest() {
        Game gameInput = new Game(new AfterAttack(), new Board(ConstantsUtils.BOARD_LEFT_CORNER_CARTESIAN_POSITION), 0, ConstantsUtils.PLAYERS, false);
        Game samePlayerGame = new Game(new ChooseWeapon(), new Board(ConstantsUtils.BOARD_LEFT_CORNER_CARTESIAN_POSITION), 0, ConstantsUtils.PLAYERS, false);
        Game nextPlayerGame = new Game(new ChooseWeapon(), new Board(ConstantsUtils.BOARD_LEFT_CORNER_CARTESIAN_POSITION), 1, ConstantsUtils.PLAYERS, false);
        Assert.assertEquals(samePlayerGame, ConstantsUtils.G1.nextGame(true, gameInput));
        Assert.assertEquals(nextPlayerGame, ConstantsUtils.G1.nextGame(false, gameInput));
    }
}