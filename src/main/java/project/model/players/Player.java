package project.model.players;

import static jtamaro.en.Sequences.cons;
import static jtamaro.en.Sequences.empty;
import static jtamaro.en.Sequences.reverse;

import project.Toolbelt;
import project.model.Game;
import jtamaro.en.Sequence;
import project.model.Position;
import project.model.boat.Boat;
import project.model.weapon.Weapon;
import project.model.phase.RoundEnded;
import project.model.weapon.Projectile;
import project.model.phase.PlayerSetting;

public interface Player {

    // common parameters
    String playerName();
    Sequence<Boat> boats();
    Weapon selectedWeapon();
    Sequence<Projectile> weapons();

    // common method with different implementations
    /**
     * it uses to set a new sequence of boats inside the player
     * @param boats a new sequence of boats
     * @return a new player with the boats updated
     */
    public Player setBoats(Sequence<Boat> boats);

    /**
     * it uses by the dynamic dispatch for the playerSettingPhase
     * @param settingPhase a default playerSettingPhase
     * @param game tue current game
     * @return a new game with the boat positions updated
     */
    public Game playerSetting(PlayerSetting settingPhase, Game game);

    /**
     * it uses by the dynamic dispatch for the chooseWeaponPhase
     * @param game the current game
     * @return a new game with the selectedWeapon updated
     */
    public Game startPlayerTurns(Game game);

    /**
     * it uses by the dynamic dispatch for the chooseWeaponPhase
     * @param game the current game
     * @return a new game with the selectedWeapon updated
     */
    public Game startCompTurns(Game game);

    /**
     * it uses by the dynamic dispatch for the choosePostionPhase
     * @param game the current game
     * @return a new game with the choosePositon updated
     */
    public Game choosingPosition(Game game);

    /**
     * it uses by the dynamic dispatch for the afterAttackPhase
     * @param game the current game
     * @return a new game with the new player or the same player based on whether the player has hit or not.
     */
    public Game attacking(Game game);

    /**
     * it uses to go to the next game
     * @param hit if the player has hit or no
     * @param game the current game
     * @return a new game with the new player or the same player based on whether the player has hit or not.
     */
    public Game nextGame(boolean hit, Game game);

    /**
     * it uses to update the opponent after the player play
     * @param oldPlayer the old player
     * @param newBoats the new sequence of boats
     * @param newWeapon the newly selected weapon
     * @return a new update with the new boats and the newly selected weapon
     */
    public Player updateOpponent(Player oldPlayer, Sequence<Boat> newBoats, Weapon newWeapon);

    /**
     * it uses by the computer to change its hitBefore
     * @return a new Player with the hitBefore updated
     */
    public Player changeHitBefore();

    /**
     * it uses by the computer to change its direction
     * @return a new Player with the direction updated
     */
    public Player changeDirComp();

    /**
     * it uses by the computer to change its lastHitPos
     * @return a new Player with the lastHitPos updated
     */
    public Player changeLastHitPos(Position hitPos);

    /**
     * it uses by the computer to change itself
     * @return a new Player with itself updated
     */
    Player updateComp(Weapon currentWeapon, Sequence<Projectile> projectiles, Sequence<Sequence<Position>> updateHittablePos);

    // common method with same implementations
    /**
     * it's used to know if the current player has won
     * @param opponent which corresponds to an opponent
     * @return true or false whether the sequence of the opponent's boat is empty
     */
    public static boolean hasWon(Player opponent){
        boolean won = true;
        Sequence<Boat> opponentBoats = opponent.boats();
        for(Boat boat: opponentBoats){
            won = won && boat.hittablePositions().isEmpty();
        }
        return won;
    }

    /**
     * it uses to update a new projectile
     * @param game the current game
     * @param weapon the current selected weapon
     * @param opponentBoats the enemy boats
     * @return a new sequence of projectiles with the hitProjectile or missProjectile based on whether the player has hit or not.
     */
    public static Sequence<Projectile> addWeapon(Game game, Weapon weapon, Sequence<Boat> opponentBoats) {
        Sequence<Position> pos = Boat.allHittablePositionsOnBoard(opponentBoats);
        return weapon.projectiles(weapon.positionsHit(), pos, game.currentPlayer().weapons());
    }

    /**
     * it's used to know whether the player, when clicking, has hit any boats.
     * @param game is the current phase game
     * @return whether the player hit a boat
     */
    public static boolean hit(Game game) {
        Player player = game.currentPlayer();
        Sequence<Projectile> projectiles = player.weapons();
        int numberProjectile = player.selectedWeapon().numberProjectiles() - 1;
        Player enemy = game.getOpponent();
        Sequence<Boat> enemyBoats = enemy.boats();
        while (numberProjectile >= 0) {
            Position hitPos = Toolbelt.get(numberProjectile, projectiles).position();
            for (Boat boat : enemyBoats) {
                if (boat.hit(hitPos)) {
                    return true;
                }
            }
            numberProjectile--;
        }
        return false;
    }

    /**
     * it's used to update the player whether he hit the boat
     * @param game is the current phase game
     * @return a new game in the last phase, RoundEnded, if the current player has won, or a new game with the player updated in the next phase with the
     */
    public static Game updatePlayer(Game game) {
        Player enemy = game.getOpponent();
        Sequence<Boat> enemyBoats = Player.updateBoats(game, enemy.boats());
        Player updateEnemy = enemy.updateOpponent(enemy, enemyBoats, enemy.selectedWeapon());
        Sequence<Player> updatePlayers = Toolbelt.set(game.advancePlayer(), updateEnemy, game.players());
        Game winnerGame = new Game(new RoundEnded(), game.board(), game.playerIndex(), updatePlayers, game.quit());
        return winnerGame.isWinner(winnerGame.currentPlayer())
            ? winnerGame
            : game.currentPlayer().nextGame(Player.hit(game), winnerGame);
    }

    /**
     * it's used to update the current player's boat sequence if he hit any boat.
     * @param game is the current phase game
     * @param boats the current player's boat sequence
     * @return a new sequence of boats from the current player in which, in the hittable positions, there is no longer the one that was just hit
     */
    public static Sequence<Boat> updateBoats(Game game, Sequence<Boat> boats) {
        Sequence<Boat> newBoats = empty();
        Player player = game.currentPlayer();
        Sequence<Projectile> projectiles = player.weapons();
        int numberProjectile = player.selectedWeapon().numberProjectiles() - 1;
        while (numberProjectile >= 0) {
            Position hitPos = Toolbelt.get(numberProjectile, projectiles).position();
            for (Boat boat : boats) {
                if (boat.hit(hitPos)) {
                    Sequence<Position> newHittablePositions = Toolbelt.remove(hitPos, boat.hittablePositions());
                    Boat finalBoat = boat.updateHittablePos(newHittablePositions).sunken();
                    newBoats = cons(finalBoat, newBoats);
                } else {
                    newBoats = cons(boat, newBoats);
                }
            }
            numberProjectile--;
        }
        return reverse(newBoats);
    }
}