    package project.model;

    import static jtamaro.en.Colors.BLUE;
import static jtamaro.en.Colors.GREEN;
import static jtamaro.en.Colors.MAGENTA;
import static jtamaro.en.Colors.YELLOW;
import static jtamaro.en.Sequences.*;

    import org.junit.Test;
    import org.junit.Assert;
    import jtamaro.en.Sequence;
    import project.model.phase.*;
    import project.model.players.Human;
    import project.model.players.Player;
    import project.model.players.RandomComputer;
    import project.model.players.directions.Right;
    import jtamaro.en.io.KeyboardChar;
    import project.model.weapon.Weapon;
    import project.ui.board.Board;
    import project.configuration.ConstantsUtils;
    import project.model.boat.Boat;
    import project.model.weapon.SingleBomb;

    public class GameTest {

    // PHASE
    private static final Phase MODE_PHASE = new Mode();
    // POSITION
    private static final Position POS = new Position(0, 0);
    //BOARD
    private static final Board BOARD = new Board(ConstantsUtils.BOARD_LEFT_CORNER_CARTESIAN_POSITION);
    //BOAT
    private static final Sequence<Boat> DESTROYER = replicate(new Boat(POS, of(POS), false, false, 2, GREEN),5);
    private static final Sequence<Boat> SUBMARINE = replicate(new Boat(POS, of(POS), false, false, 2, BLUE),4);
    private static final Sequence<Boat> BATTLESHIP = replicate(new Boat(POS, of(POS), false, false, 2, MAGENTA),2);
    private static final Sequence<Boat> CARRIER = replicate(new Boat(POS, of(POS), false, false, 2, YELLOW), 1);
    private static final Sequence<Boat> BOATS = concat(concat(concat(SUBMARINE, DESTROYER), BATTLESHIP), CARRIER);
    // WEAPON
    private static final Weapon DEFAULT_WEAPON = new SingleBomb(POS, POS);
    // PLAYER
    private static final Player G1 = new Human("G1", BOATS, DEFAULT_WEAPON, empty());
    private static final Player G2 = new Human("G2", BOATS, DEFAULT_WEAPON, empty());
    private static final Player G3 = new Human("Simone", BOATS, DEFAULT_WEAPON, empty());
    private static final Player G4 = new Human("Alessandra", BOATS, DEFAULT_WEAPON, empty());
    private static final Player COM = new RandomComputer("Computer", BOATS, DEFAULT_WEAPON, empty(), new Right(), false, new Position(0, 0), ConstantsUtils.HITTABLEPOSITIONS);
    private static final Sequence<Player> PLAYERS = of(G1, G2, G3, G4, COM);
    // GAME
    private static final Game GAME_1 = new Game(MODE_PHASE, BOARD, 0, PLAYERS, false);
    private static final Game GAME_2 = new Game(MODE_PHASE, BOARD, 1, PLAYERS, false);
    private static final Game GAME_3 = new Game(MODE_PHASE, BOARD, 2, PLAYERS, false);
    private static final Game GAME_4 = new Game(MODE_PHASE, BOARD, 3, PLAYERS, false);
    private static final Game GAME_5 = new Game(MODE_PHASE, BOARD, 4, PLAYERS, false);
    private static final Game GAME_6 = new Game(MODE_PHASE, BOARD, 0, PLAYERS, true);

    @Test
    public void createTest() {
        Assert.assertEquals(BOARD, GAME_1.board());
        Assert.assertEquals(0, GAME_1.playerIndex());
        Assert.assertEquals(PLAYERS, GAME_1.players());
        Assert.assertEquals(new Mode(), GAME_1.roundPhase());
        Assert.assertNotEquals(new RoundEnded(), GAME_1.roundPhase());
    }

    @Test
    public void currentPlayerTest() {
        Assert.assertEquals(G1, GAME_1.currentPlayer());
        Assert.assertEquals(G2, GAME_2.currentPlayer());
        Assert.assertEquals(G3, GAME_3.currentPlayer());
        Assert.assertEquals(G4, GAME_4.currentPlayer());
        Assert.assertEquals(COM, GAME_5.currentPlayer());
    }

    @Test
    public void isLastPlayerTest() {
        Assert.assertTrue(GAME_5.isLastPlayer());
        Assert.assertFalse(GAME_1.isLastPlayer());
        Assert.assertFalse(GAME_2.isLastPlayer());
        Assert.assertFalse(GAME_3.isLastPlayer());
        Assert.assertFalse(GAME_4.isLastPlayer());
    }

    @Test
    public void handleKeyTest() {
        KeyboardChar r = new KeyboardChar('r');
        KeyboardChar a = new KeyboardChar('a');
        KeyboardChar q = new KeyboardChar('q');
        Assert.assertEquals(Game.create(), GAME_1.handleKey(r));
        Assert.assertEquals(GAME_2, GAME_2.handleKey(a));
        Assert.assertEquals(GAME_6, GAME_1.handleKey(q));
    }

    @Test
    public void changePhaseTest() {
        Phase settingPhase = new PlayerSetting(Mode.DESTROYER, Mode.BOATS, Mode.DEFAULTS, -1, 4, false);
        Phase startedPhase = new ChooseWeapon();
        Phase beforePhase = new ChoosePosition();
        Phase afterPhase = new AfterAttack();
        Phase endedPhase = new RoundEnded();
        Game settingGame = new Game(settingPhase, BOARD, 0, PLAYERS, false);
        Game startedGame = new Game(startedPhase, BOARD, 0, PLAYERS, false);
        Game beforeGame = new Game(beforePhase, BOARD, 0, PLAYERS, false);
        Game afterGame = new Game(afterPhase, BOARD, 0, PLAYERS, false);
        Game endedGame = new Game(endedPhase, BOARD, 0, PLAYERS, false);
        Assert.assertEquals(settingGame, GAME_1.changePhase(settingPhase));
        Assert.assertEquals(endedGame, afterGame.changePhase(endedPhase));
        Assert.assertEquals(afterGame, beforeGame.changePhase(afterPhase));
        Assert.assertEquals(beforeGame, startedGame.changePhase(beforePhase));
        Assert.assertEquals(startedGame, settingGame.changePhase(startedPhase));
    }

    @Test
    public void isWinnerTest() {
        Sequence<Boat> boat = empty();
        Player G5 = new Human("G5", boat, DEFAULT_WEAPON, empty());
        Sequence<Player> players = of(G1, G5);
        Game game6 = new Game(MODE_PHASE, BOARD, 0, players, false);
        Assert.assertTrue(game6.isWinner(G5));
        Assert.assertFalse(GAME_2.isWinner(G2));
        Assert.assertFalse(GAME_3.isWinner(G3));
        Assert.assertFalse(GAME_4.isWinner(G4));
        Assert.assertFalse(GAME_5.isWinner(COM));
    }
}