package project.model.players;

import jtamaro.en.Sequence;
import project.model.Game;
import project.model.Position;
import project.model.boat.Boat;
import project.model.players.directions.Direction;
import project.model.weapon.Projectile;
import project.model.weapon.Weapon;

public class RandomComputer extends Computer {

  public RandomComputer(
      String playerName,
      Sequence<Boat> boats,
      Weapon selectedWeapon,
      Sequence<Projectile> weapons,
      Direction dir,
      boolean hitBefore,
      Position lastHitPos,
      Sequence<Sequence<Position>> hittablePosition) {
    super(playerName, boats, selectedWeapon, weapons, dir, hitBefore, lastHitPos, hittablePosition);
  }

  @Override
  public Game choosingPosition(Game game) {
    Position pos =
        Computer.choosePosition(
                game.currentPlayer().selectedWeapon().numberProjectiles(),
                this.hittablePosition(),
                10)
            .first();
    Game updateGame = updatePosition(game, pos);
    boolean hit = Player.hit(updateGame);
    return hit ? Player.updatePlayer(updateGame) : nextGame(hit, updateGame);
  }

  @Override
  public Player updateComp(
      Weapon currentWeapon,
      Sequence<Projectile> projectiles,
      Sequence<Sequence<Position>> updateHittablePos) {
    return new RandomComputer(
        this.playerName(),
        this.boats(),
        currentWeapon,
        projectiles,
        this.dir(),
        this.hitBefore(),
        this.lastHitPos(),
        updateHittablePos);
  }

  @Override
  public Player updateOpponent(Player oldPlayer, Sequence<Boat> newBoats, Weapon newWeapon) {
    return new RandomComputer(
        oldPlayer.playerName(),
        newBoats,
        newWeapon,
        oldPlayer.weapons(),
        this.dir(),
        this.hitBefore(),
        this.lastHitPos(),
        this.hittablePosition());
  }

  @Override
  public Player setBoats(Sequence<Boat> boats) {
    return new RandomComputer(
        this.playerName(),
        boats,
        this.selectedWeapon(),
        this.weapons(),
        this.dir(),
        this.hitBefore(),
        this.lastHitPos(),
        this.hittablePosition());
  }
}
