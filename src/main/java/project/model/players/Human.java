package project.model.players;

import project.configuration.ConstantsUtils;
import project.model.Game;
import project.model.Position;
import jtamaro.en.Sequence;
import project.model.boat.*;
import project.model.weapon.Weapon;
import project.model.phase.ChoosePosition;
import project.model.phase.ChooseWeapon;
import project.model.phase.PlayerSetting;
import project.model.weapon.Projectile;
import project.model.weapon.SingleBomb;

/**
 * every player must have:
 * @param playerName the name of the player
 * @param BOATS all the player's not sunk boats, if this is empty(), player 2 won
 * @param selectedWeapons which weapons he selected
 * @param weapons all the projectile he used in the game to render their position
*/

public record Human(String playerName,  Sequence<Boat> boats, Weapon selectedWeapon, Sequence<Projectile> weapons) implements Player {

    private static final Weapon SINGLEBOMB = new SingleBomb(ConstantsUtils.WEAPON_BUTTON_POSITION, new Position(0, 0));

    @Override
    public Game playerSetting(PlayerSetting settingPhase, Game game) {
        return settingPhase.changePlayer(game);
    }

    /**
     * it is used to  move to the next stage iff the weapon is selected
     * @param game is the current phase game
     * @return a new game in the next phase with the current player updated with the current weapon
     */
    @Override
    public Game startPlayerTurns(Game game) {
        Player current = game.currentPlayer();
        Player newP = new Human(
            current.playerName(), 
            current.boats(), 
            SINGLEBOMB, 
            current.weapons()
        );
        return new Game(
            new ChoosePosition(), 
            game.board(), 
            game.playerIndex(), 
            game.updateCurrentPlayer(newP),
            game.quit()
        );
    }

    @Override
    public Game startCompTurns(Game game) {
        return game;
    }

    @Override
    public Game choosingPosition(Game game) {
        return game;
    }

    @Override
    public Game attacking(Game game) {
        boolean hit = Player.hit(game);
        return hit
            ?Player.updatePlayer(game)
            : nextGame(hit, game);
    }

    @Override
    public Player updateOpponent(Player oldPlayer, Sequence<Boat> newBoats, Weapon newWeapon) {
        return new Human(oldPlayer.playerName(), newBoats, oldPlayer.selectedWeapon(), oldPlayer.weapons());
    }

    /**
     * it's used change the current player if the player doesn't hit the boat
     * @param game is the current phase game
     * @return a new game with the next player in the next phase and the current player updated
     */
    @Override
    public Game nextGame(boolean hit, Game game) {
        Game samePlayerGame = new Game(new ChooseWeapon(), game.board(), game.playerIndex(), game.players(), game.quit());
        Game nextPlayerGame = new Game(new ChooseWeapon(), game.board(), game.advancePlayer(), game.players(), game.quit());
        return hit 
            ? samePlayerGame
            : nextPlayerGame;
            // : new ChooseWeapon().handleKey(new KeyboardChar('S'), nextPlayerGame);
    }

    @Override
    public Player setBoats(Sequence<Boat> boats) {
        return new Human(this.playerName, boats, this.selectedWeapon, this.weapons);
    }

    @Override
    public Player changeHitBefore() {
        return this;
    }

    @Override
    public Player changeDirComp() {
        return this;
    }

    @Override
    public Player changeLastHitPos(Position hitPos) {
        return this;
    }

    @Override
    public Player updateComp(Weapon currentWeapon, Sequence<Projectile> projectiles, Sequence<Sequence<Position>> updateHittablePos) {
        return this;
    }
}