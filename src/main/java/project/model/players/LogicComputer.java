package project.model.players;

import static jtamaro.en.Sequences.of;

import jtamaro.en.Sequence;
import project.model.Game;
import project.model.Position;
import project.model.boat.Boat;
import project.model.phase.AfterAttack;
import project.model.players.directions.Direction;
import project.model.players.directions.Right;
import project.model.weapon.Projectile;
import project.model.weapon.Weapon;

public class LogicComputer extends Computer {

  public LogicComputer(
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
    if (!this.hitBefore()) {
      Position pos =
          Computer.choosePosition(
                  game.currentPlayer().selectedWeapon().numberProjectiles(),
                  this.hittablePosition(),
                  10)
              .first();
      Game updateGame = updatePosition(game, pos);
      boolean hit = Player.hit(updateGame);
      if (hit) {
        Player updateComp = updateGame.currentPlayer().changeHitBefore().changeLastHitPos(pos);
        Sequence<Player> updatePlayers = of(updateGame.getOpponent(), updateComp);
        updateGame =
            new Game(
                new AfterAttack(), game.board(), game.playerIndex(), updatePlayers, game.quit());
        return Player.updatePlayer(updateGame);
      } else {
        return nextGame(hit, updateGame);
      }
    } else {
      return choosingLogicComputer(game);
    }
  }

  @Override
  public Player updateComp(
      Weapon currentWeapon,
      Sequence<Projectile> projectiles,
      Sequence<Sequence<Position>> updateHittablePos) {
    return new LogicComputer(
        this.playerName(),
        this.boats(),
        currentWeapon,
        projectiles,
        this.dir(),
        this.hitBefore(),
        this.lastHitPos(),
        updateHittablePos);
  }

  public Game choosingLogicComputer(Game game) {
    Player updateComp;
    Game updateGame;
    Position oldPos =
        this.hitBefore() ? this.lastHitPos() : game.currentPlayer().weapons().first().position();
    Position nextPos = this.dir().nextPosition(oldPos);
    Direction actualDir = this.dir();
    while (!nextPos.isValidPosition()) {
      updateComp = game.currentPlayer().changeDirComp();
      updateGame = updatePlayersGame(game, updateComp);
      actualDir = actualDir.changeDir();
      nextPos = actualDir.nextPosition(oldPos);
    }
    updateGame = updatePosition(game, nextPos);
    Player currentPlayer = updateGame.currentPlayer();
    boolean hit = Player.hit(updateGame);
    updateComp =
        hit
            ? currentPlayer.changeLastHitPos(nextPos)
            : !hit && !(this.dir().changeDir().equals(new Right()))
                ? currentPlayer.changeDirComp()
                : currentPlayer.changeDirComp().changeHitBefore();
    updateGame = updatePlayersGame(updateGame, updateComp);
    return hit ? Player.updatePlayer(updateGame) : nextGame(hit, updateGame);
  }

  @Override
  public Player updateOpponent(Player oldPlayer, Sequence<Boat> newBoats, Weapon newWeapon) {
    return new LogicComputer(
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
    return new LogicComputer(
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
