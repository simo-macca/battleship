package project.model.players;

import static jtamaro.en.Colors.rgb;
import static jtamaro.en.Colors.BLUE;
import static jtamaro.en.Sequences.of;
import static jtamaro.en.Colors.GREEN;
import static jtamaro.en.Colors.MAGENTA;
import static jtamaro.en.Sequences.cons;
import static jtamaro.en.Sequences.empty;
import static jtamaro.en.Sequences.reverse;

import java.util.*;
import project.Toolbelt;
import jtamaro.en.Color;
import project.model.Game;
import jtamaro.en.Sequence;
import project.model.Position;
import project.model.boat.Boat;
import jtamaro.en.io.KeyboardChar;
import project.model.weapon.Weapon;
import project.model.weapon.Projectile;
import project.model.weapon.SingleBomb;
import project.model.phase.ChooseWeapon;
import project.model.phase.PlayerSetting;
import project.model.phase.ChoosePosition;
import project.configuration.ConstantsUtils;
import project.model.players.directions.Direction;

/**
 * every computer must have:
 * @param playerName the name of the player
 * @param boats all the player's not sunk boats, if this is empty(), player 2 won
 * @param selectedWeapons which weapons he selected
 * @param weapons all the projectile he used in the game to render their position
 * @param dir the direction where he "looks"
 * @param hitBefore if in the last play it hit or no
 * @param lastHitPos the last position of when it hit
 * @param hittablePosition all the hittable position
*/

public abstract class Computer implements Player {

    private final String playerName;
    private final Sequence<Boat> boats;
    private final Weapon selectedWeapon;
    private final Sequence<Projectile> weapons;
    private final Direction dir;
    private final boolean hitBefore;
    private final Position lastHitPos;
    private final Sequence<Sequence<Position>> hittablePosition;

    public Computer(String playerName, Sequence<Boat> boats, Weapon selectedWeapon, Sequence<Projectile> weapons, Direction dir, boolean hitBefore, Position lastHitPos, Sequence<Sequence<Position>> hittablePosition) {
        this.playerName = playerName;
        this.boats = boats;
        this.selectedWeapon = selectedWeapon;
        this.weapons = weapons;
        this.dir = dir;
        this.hitBefore = hitBefore;
        this.lastHitPos = lastHitPos;
        this.hittablePosition = hittablePosition;
    }

    public String playerName() {
        return playerName;
    }

    public Sequence<Boat> boats() {
        return boats;
    }

    public Weapon selectedWeapon() {
        return selectedWeapon;
    }

    public Sequence<Projectile> weapons() {
        return weapons;
    }

    public Direction dir() {
        return dir;
    }

    public boolean hitBefore() {
        return hitBefore;
    }

    public Position lastHitPos() {
        return lastHitPos;
    }

    public Sequence<Sequence<Position>> hittablePosition() {
        return hittablePosition;
    }

    public static final Random RANDOM = new Random();

    public Game playerSetting(PlayerSetting playerPhase, Game game) {
        Sequence<Boat> newBoats = choosingBoats();
        Player newPlayer = game.getOpponent().updateOpponent(game.getOpponent(), newBoats, game.currentPlayer().selectedWeapon());
        Sequence<Player> updatePlayers = of(game.currentPlayer(), newPlayer);
        Game nextGame = new Game(new ChooseWeapon(), game.board(), game.advancePlayer(), updatePlayers, false);
        return new ChooseWeapon().handleKey(new KeyboardChar('S'), nextGame);
    }

    public static Sequence<Boat> choosingBoats() {
        Sequence<Boat> boats = empty();
        Sequence<Sequence<Position>> allPos = ConstantsUtils.HITTABLEPOSITIONS;
        Sequence<Color> colors = of(GREEN, BLUE, MAGENTA, rgb(255,165,0));
        for (int i = 2; i < 6; i++) {
            Sequence<Position> startPos = choosePosition(1, allPos, 10);
            boolean isVertical = RANDOM.nextInt(0, 2) == 0;
            Boat newBoat = new Boat(startPos.first(), Boat.startingHittablePositions(startPos.first(), i, isVertical), false, isVertical, i, Toolbelt.get(i-2, colors));
            for (Position boatPos : newBoat.hittablePositions()) {
                if (!boatPos.isValidPosition() || Toolbelt.isInSequence(boatPos, Boat.allHittablePositionsOnBoard(boats))) {
                    return choosingBoats();
                }
                allPos = Toolbelt.removeFromMatrix(boatPos, allPos);
            }
            boats = cons(newBoat, boats);
        }
        return reverse(boats);
    }

    public static Sequence<Position> choosePosition(int howMany, Sequence<Sequence<Position>> hittablePos, int numberRow) {
        Sequence<Position> pos = empty();
        Sequence<Sequence<Position>> allPosition = hittablePos;
        int i = 0;
        int r, c, max = numberRow;
        while (i < howMany) {
            r = RANDOM.nextInt(0, max);
            Sequence<Position> row = Toolbelt.get(r, allPosition);
            if(row.isEmpty()) {
                max--;
                return choosePosition(howMany, hittablePos, max);
            } else {
                c = RANDOM.nextInt(0, Toolbelt.length(row));
                Position col = Toolbelt.get(c, row);
                pos = cons(col, pos);
                allPosition = Toolbelt.removeFromMatrix(col, allPosition);
                i++;
            }
        }
        return reverse(pos);
    }

    @Override
    public Game startPlayerTurns(Game game) {
        return game;
    }

    public Game startCompTurns(Game game) {
        Weapon singleBomb = new SingleBomb(ConstantsUtils.WEAPON_BUTTON_POSITION, null);
        Player newComp = updateOpponent(game.currentPlayer(), game.currentPlayer().boats(), singleBomb);// new RandomComputer(this.playerName, this.boats, singleBomb, this.weapons, this.dir, this.hitBefore, this.lastHitPos, this.hittablePosition);
        Game nextPhaseGame = new Game(new ChoosePosition(), game.board(), game.playerIndex(), of(game.players().first(), newComp), game.quit());
        return game.currentPlayer().choosingPosition(nextPhaseGame);
    }

    public Game nextGame(boolean hit, Game game) {
        Game samePlayerGame = new Game(new ChooseWeapon(), game.board(), 1, game.players(), game.quit());
        Game nextPlayerGame = new Game(new ChooseWeapon(), game.board(), 0, game.players(), game.quit());
        return hit
            ? new ChooseWeapon().handleKey(new KeyboardChar('\n'), samePlayerGame)
            : nextPlayerGame;
    }

    @Override
    public Game attacking(Game game) {
        return startCompTurns(game);
    }

    @Override
    public Player changeDirComp() {
        return new LogicComputer(this.playerName(), this.boats(), this.selectedWeapon(), this.weapons(), this.dir().changeDir(), this.hitBefore(), this.lastHitPos(), this.hittablePosition());
    }

    @Override
    public Player changeHitBefore() {
        return new LogicComputer(this.playerName(), this.boats(), this.selectedWeapon(), this.weapons(), this.dir(), !this.hitBefore(), this.lastHitPos(), this.hittablePosition());
    }

    @Override
    public Player changeLastHitPos(Position hitPos) {
        return new LogicComputer(this.playerName(), this.boats(), this.selectedWeapon(), this.weapons(), this.dir(), this.hitBefore(), hitPos, this.hittablePosition());
    }

    public Game updatePlayersGame(Game game, Player comp) {
        Sequence<Player> updatePlayers = Toolbelt.set(game.playerIndex(), comp, game.players());
        return new Game(game.roundPhase(), game.board(), game.playerIndex(), updatePlayers, game.quit());
    }

    public Game updatePosition(Game game, Position pos) {
        Sequence<Sequence<Position>> updateHittablePos = Toolbelt.removeFromMatrix(pos, this.hittablePosition());
        Player currentPlayer = Toolbelt.get(game.playerIndex(), game.players());
        Player opponent = game.getOpponent();
        Weapon currentWeapon = currentPlayer.selectedWeapon().weaponWithCorrectPlacement(pos);
        Sequence<Projectile> projectiles = Player.addWeapon(game, currentWeapon, opponent.boats());
        Player updateComp = currentPlayer.updateComp(currentWeapon, projectiles, updateHittablePos);
        return updatePlayersGame(game, updateComp);
    }
}