package project.ui.phase;

import static jtamaro.en.Colors.rgb;
import static jtamaro.en.Colors.BLUE;
import static jtamaro.en.Colors.GREEN;
import static jtamaro.en.Sequences.of;
import static jtamaro.en.Graphics.pin;
import static jtamaro.en.Colors.MAGENTA;
import static jtamaro.en.Sequences.rest;
import static jtamaro.en.Sequences.empty;
import static jtamaro.en.Sequences.cons;
import static jtamaro.en.Points.TOP_LEFT;
import static jtamaro.en.Sequences.concat;
import static jtamaro.en.Sequences.first;
import static jtamaro.en.Graphics.compose;
import static jtamaro.en.Graphics.overlay;
import static jtamaro.en.Sequences.reverse;
import static jtamaro.en.Sequences.isEmpty;
import static jtamaro.en.Points.BOTTOM_LEFT;
import static jtamaro.en.Points.BOTTOM_RIGHT;
import static jtamaro.en.Graphics.emptyGraphic;

import project.Toolbelt;
import project.ui.GameUI;
import jtamaro.en.Graphic;
import project.model.Game;
import jtamaro.en.Sequence;
import project.ui.board.Board;
import jtamaro.en.CartesianWorld;
import project.model.weapon.Weapon;
import project.model.players.Player;
import project.ui.boat.BoatGraphics;
import project.model.phase.PlayerSetting;
import project.configuration.ConstantsUtils;
import project.ui.cartesianWorldGraphics.CartesianWorldTuples;
import project.ui.cartesianWorldGraphics.CartesianWorldPosition;

public class PhaseUI {

    private final static Graphic R_GRAPHIC = GameUI.instruction("R", "reset the game");
    private final static Graphic Q_GRAPHIC = GameUI.instruction("Q", "quit the application");
    private final static Graphic RETURN_GRAPHIC = GameUI.instruction("RETURN", "go on");
    private final static Graphic S_GRAPHIC = GameUI.instruction("S", "select the single bomb");
    private final static Graphic START_GAME_GRAPHIC = GameUI.instruction("N", "start the game");
    private final static Graphic NEXT_PLAYER_GRAPHIC = GameUI.instruction("N", "the next player");

    public static final CartesianWorldPosition DEFAULT_POSITION_DESTROYER = new CartesianWorldPosition(20, 140);
    public static final BoatGraphics DESTROYER = new BoatGraphics(DEFAULT_POSITION_DESTROYER , BoatGraphics.boatGraphic(2, true, false, GREEN), true, false, 2, GREEN);
    public static final CartesianWorldPosition DEFAULT_POSITION_SUBMARINE = new CartesianWorldPosition(20, 105);
    public static final BoatGraphics SUBMARINE = new BoatGraphics(DEFAULT_POSITION_SUBMARINE, BoatGraphics.boatGraphic(3, true, false, BLUE), true, false, 3, BLUE);
    public static final CartesianWorldPosition DEFAULT_POSITION_BATTLESHIP = new CartesianWorldPosition(20, 70);
    public static final BoatGraphics BATTLESHIP = new BoatGraphics(DEFAULT_POSITION_BATTLESHIP, BoatGraphics.boatGraphic(4, true, false, MAGENTA), true, false, 4, MAGENTA);
    public static final CartesianWorldPosition DEFAULT_POSITION_CARRIER = new CartesianWorldPosition(20, 35);
    public static final BoatGraphics CARRIER = new BoatGraphics(DEFAULT_POSITION_CARRIER, BoatGraphics.boatGraphic(5, true, false, rgb(255,165,0)), true, false, 5, rgb(255,165,0));
    public static final Sequence<BoatGraphics> BOATS = of(DESTROYER, SUBMARINE, BATTLESHIP, CARRIER);

    public static Graphic generalHeader(String playerName){
        return pin(
                TOP_LEFT,
                compose(
                    pin(TOP_LEFT, GameUI.string(playerName, 30)),
                    pin(TOP_LEFT, GameUI.verticalSepare(GameUI.FULL_HEIGTH/3))
                )
            );
    }

    public static Sequence<Graphic> generalFooter(){
        return of(Q_GRAPHIC, R_GRAPHIC);
    }

    public static Sequence<Graphic> addSepare(Sequence<Graphic> graphics){
        return isEmpty(graphics)?
                empty():
                cons(
                    GameUI.verticalSepare(GameUI.FULL_HEIGTH/60),
                    cons(
                        first(graphics),
                        addSepare(rest(graphics))
                    )
                );
    }

    public static Graphic generateFooter(Sequence<Graphic> footerTexts){
        return pin(
            BOTTOM_RIGHT,
            compose(
                pin(
                    BOTTOM_LEFT,
                    Toolbelt.reduce(
                        emptyGraphic(),
                        (a, b) -> compose(pin(TOP_LEFT, a), pin(BOTTOM_LEFT, b)),
                        addSepare(footerTexts)
                    )
                ), 
                pin(BOTTOM_LEFT, GameUI.verticalSepare(GameUI.FULL_HEIGTH/3))
            )
        );
    }

    public static Graphic backCanvas(String playerName, Sequence<Graphic> footerGraphics){
        return pin(
                BOTTOM_LEFT,
                compose(
                    generateFooter(footerGraphics),
                    pin(
                        BOTTOM_RIGHT,
                        compose(
                            generalHeader(playerName), 
                            pin(TOP_LEFT,GameUI.canvas())
                        )
                    )
                )
            );
    }

    public static Graphic backgroundWithGrid(String playerName, Sequence<Graphic> footerGraphics){
        return new CartesianWorld()
                .place(0, 0, backCanvas(playerName, footerGraphics))
                .place(
                    ConstantsUtils.BOARD_LEFT_CORNER_POSITION.x(), 
                    ConstantsUtils.BOARD_LEFT_CORNER_POSITION.y(), 
                    Board.grid()
                )
                .asGraphic();
    }

    public static Graphic playerSettingsBackground(String playerName, int playerIndex){
        return backgroundWithGrid(
                playerName, 
                concat(generalFooter(), of(S_GRAPHIC, playerIndex == 0? NEXT_PLAYER_GRAPHIC: START_GAME_GRAPHIC))
            );
    }

    public static Graphic chooseWeaponBackground(String playerName){
        return backgroundWithGrid(
                playerName, 
                concat(generalFooter(), of(RETURN_GRAPHIC, S_GRAPHIC))
            );
    }

    public static Graphic choosePositionBackground(String playerName){
        return backgroundWithGrid(
                playerName, 
                concat(generalFooter(), of(S_GRAPHIC, NEXT_PLAYER_GRAPHIC))
            );
    }

    public static Graphic afterAttackBackground(String playerName, boolean n){
        return backgroundWithGrid(
                playerName, 
                concat(generalFooter(), of(S_GRAPHIC, n? GameUI.instruction("N", "go to the next turn") : GameUI.instruction("N", "change the player")))
            );
    }

    public static Graphic roundEndedBackground(String playerName, boolean n){
        return afterAttackBackground(playerName, n);
    }

    public static Graphic renderChooseMode(){
        return GameUI.mode();
    }

    public static Graphic renderChooseDifficulty(){
        return GameUI.chooseDifficulty();
    }

    public static Graphic renderPlayerSettings(Game game, PlayerSetting settings){
        return CartesianWorldTuples.renderGame(
            new CartesianWorld()
                .place(
                    0, 
                    0, 
                    pin(BOTTOM_LEFT, playerSettingsBackground(game.currentPlayer().playerName(), game.playerIndex()))
                ),
            CartesianWorldTuples.boatsToTuples(reverse(settings.boats()))
        );
    }

    public static Graphic renderChooseWeapon(Game game, Weapon weapon){
        return placeBoatsAndWeapons(
                chooseWeaponBackground(game.currentPlayer().playerName()),
                game, 
                BoatGraphics.fromLogicBoatsToPhisicalBoats(game.getOpponent().boats())
            )
            .asGraphic();
    }

    public static Graphic renderChoosePosition(Game game){
        return placeBoatsAndWeapons(
                choosePositionBackground(game.currentPlayer().playerName()),
                game, 
                concat(BOATS, BoatGraphics.fromLogicBoatsToPhisicalBoats(game.getOpponent().boats()))
            )
            .asGraphic();
    }

    public static Graphic renderAfterAttack(Game game){
        return placeBoatsAndWeapons(
                afterAttackBackground(game.currentPlayer().playerName(), Player.hit(game)),
                game, 
                concat(BOATS, BoatGraphics.fromLogicBoatsToPhisicalBoats(game.getOpponent().boats()))
            )
            .asGraphic();
    }
/** easter egg */
    public static Graphic renderRoundEnded(Game game){
        return overlay(
            GameUI.winner(game.currentPlayer().playerName()),
            placeBoatsAndWeapons(
                pin(BOTTOM_LEFT, roundEndedBackground(game.currentPlayer().playerName(), Player.hit(game))),
                game, 
                concat(BOATS, BoatGraphics.fromLogicBoatsToPhisicalBoats(game.getOpponent().boats()))
            )
            .asGraphic()
        );
    }

    public static CartesianWorld placeBoatsAndWeapons(Graphic background, Game game, Sequence<BoatGraphics> boats){
        return CartesianWorldTuples.placeBoatsAndProjectiles(
                new CartesianWorld()
                    .place(0, 0, background),
                game.currentPlayer().weapons(),
                boats
        );
    }
}