package project.ui;

import static jtamaro.en.Colors.*;
import static jtamaro.en.Points.*;
import static jtamaro.en.Graphics.*;
import static jtamaro.en.Sequences.*;

import jtamaro.en.Fonts;
import project.Toolbelt;
import jtamaro.en.Color;
import jtamaro.en.Graphic;
import jtamaro.en.Sequence;
import project.configuration.ConstantsUtils;

public class GameUI {

    public final static int FULL_WIDTH = ConstantsUtils.WIDTH;
    public final static int FULL_HEIGTH = ConstantsUtils.HEIGTH;
    public final static String DEFAULT_FONT = Fonts.SANS_SERIF;
    public final static Graphic DEFAULT_SEPARE = verticalSepare(5);
    public final static Graphic Q_GRAPHIC = GameUI.instruction("Q", "quit the application");
    private final static Graphic R_GRAPHIC = GameUI.instruction("R", "reset the game");
    private final static Graphic E_GRAPHIC = GameUI.instruction("E", "select easy difficulty");
    private final static Graphic H_GRAPHIC = GameUI.instruction("H", "select hard difficulty");

    /**
     * it's used to create a grid's background
     * @return a light-blue background
     */
    public static Graphic canvas() {
        Sequence<Integer> radius = of(2, 8, 10, 65, 70, 125, 130, 185, 190, 240, 245);
        Sequence<Color> colors = of(
            WHITE,
            rgb(79, 143, 255),
            rgb(146, 179, 227),
            rgb(69, 124, 222),
            rgb(151, 185, 234),
            rgb(58, 106, 189),
            rgb(155, 190, 241),
            rgb(48, 87, 156),
            rgb(169, 196, 248),
            rgb(38, 69, 123),
            // rgb(165, 202, 255)
            rgb(79, 143, 255)
        );
        Sequence<Graphic> graphic = map(a -> Toolbelt.circle(a.first(), a.second()), zip(radius, colors));
        Graphic background = rectangle(FULL_WIDTH, FULL_HEIGTH, rgb(46, 68, 123));
        Graphic radar = overlay(Toolbelt.overlays(graphic), background);
        Graphic eyeRadar = pin(BOTTOM_LEFT, triangle(FULL_WIDTH/2-5, FULL_WIDTH/2-5, 30, rgb(79, 143, 255, .5)));
        return compose(eyeRadar, radar);
    }

    public static Graphic modeBackground(){
        Color backgroundColor = rgb(0, 0, 0);
        return compose(
            pin(TOP_LEFT, modeHeader(BLACK, rgb(89, 176, 234))),
            pin(TOP_LEFT, rectangle(FULL_WIDTH, FULL_HEIGTH, backgroundColor))
        );
    }

    /**
     * it's used only in the mode's phase to generate a black background
     * @return a mode phase's background
     */
    public static Graphic mode() {
        Graphic imagesSepare = verticalSepare(FULL_HEIGTH/60);
        Graphic SELECT_MODE_GRAPHIC = text("Select one mode to start the game:", GameUI.DEFAULT_FONT, FULL_HEIGTH/40, WHITE);
        Graphic U_GRAPHIC = GameUI.instruction("U", "play against your friend");
        Graphic C_GRAPHIC = GameUI.instruction("C", "play against the computer");
        return overlay(
                above(
                    above(
                        verticalSepare(FULL_HEIGTH/3),
                        overlay(
                            Toolbelt.aboves(of(utent(), verticalSepare(35), computer())),
                            verticalSepare(FULL_HEIGTH/3)
                        )
                    ),
                    overlay(
                        Toolbelt.aboves(
                            of(
                                SELECT_MODE_GRAPHIC, 
                                imagesSepare,
                                U_GRAPHIC,
                                DEFAULT_SEPARE,
                                C_GRAPHIC,
                                DEFAULT_SEPARE,
                                Q_GRAPHIC,
                                verticalSepare(30),
                                GameUI.creator(),
                                DEFAULT_SEPARE,
                                GameUI.version()
                            )
                        ),
                        verticalSepare(FULL_HEIGTH/3)
                    )
                ),
                modeBackground()
            );
    }

    public static Graphic chooseDifficulty(){
        Graphic imagesSepare = verticalSepare(FULL_HEIGTH/60);
        Graphic SELECT_DIFFICULTY_GRAPHIC = text("Select one difficulty to start the game:", GameUI.DEFAULT_FONT, 15, WHITE);
        return overlay(
                above(
                    above(
                        verticalSepare(FULL_HEIGTH/3),
                        overlay(
                            Toolbelt.aboves(of(easy(), verticalSepare(40), hard())),
                            verticalSepare(FULL_HEIGTH/3)
                        )
                    ),
                    overlay(
                        Toolbelt.aboves(
                            of(
                                SELECT_DIFFICULTY_GRAPHIC, 
                                imagesSepare,
                                E_GRAPHIC,
                                DEFAULT_SEPARE,
                                H_GRAPHIC,
                                DEFAULT_SEPARE,
                                R_GRAPHIC,
                                DEFAULT_SEPARE,
                                Q_GRAPHIC
                            )
                        ),
                        verticalSepare(FULL_HEIGTH/3)
                    )
                ),
                modeBackground()
            );
    }

    private static Graphic modeHeader(Color bgColor, Color textColor) {
        return overlay(
            text("BATTLESHIP", DEFAULT_FONT, FULL_HEIGTH/12, textColor),
            rectangle(FULL_WIDTH, FULL_HEIGTH/3, bgColor)
        );
    }

    public static Graphic verticalSepare(int height){
        return rectangle(height, height, TRANSPARENT);
    }

    /**
     * it's used to create a graphic with a specific string
     * @param name the string that is to be displayed
     * @param fontSize the font size of the string
     * @return a name graphic to a black background
     */
    public static Graphic string(String name, double fontSize) {
        return compose(
            pin(BOTTOM_CENTER, text(name, DEFAULT_FONT, fontSize, WHITE)),
            pin(BOTTOM_CENTER, rectangle(FULL_WIDTH, FULL_HEIGTH/8 + FULL_HEIGTH/60, rgb(165, 202, 255, .5)))
        );
    }

    /**
     * it's the image on the main page that corresponds to the people vs. people game
     * @return the image of two users next to each other
     */
    public static Graphic utent() {
        Graphic face = pin(CENTER, Toolbelt.circle(15, WHITE));
        Graphic body = pin(TOP_CENTER, circularSector(20, 180, WHITE));
        Graphic vs = text("VS", DEFAULT_FONT, 20, WHITE);
        Graphic utent = compose(face, body);
        return pin(TOP_LEFT, Toolbelt.besides(of(utent, vs, rectangle(4, 0, TRANSPARENT), utent)));
    }

    /**
     * it's the image on the main page that corresponds to the people vs. computer game
     * @return the image of a user and a computer next to each other
     */
    public static Graphic computer() {
        Graphic screen = overlay(rectangle(30, 25, BLACK), rectangle(35, 30, rgb(89, 176, 234)));
        Graphic console = rectangle(40, 10, rgb(89, 176, 234));
        Graphic comp = above(screen, console);
        Graphic face = pin(CENTER, Toolbelt.circle(15, WHITE));
        Graphic body = pin(TOP_CENTER, circularSector(20, 180, WHITE));
        Graphic vs = text("VS", DEFAULT_FONT, 20, WHITE);
        Graphic utent = compose(face, body);
        return Toolbelt.besides(of(utent, vs, verticalSepare(4), comp));
    }

    public static Graphic modeButtonBackground(String text, Color backColor, Color borderColor){
        return overlay(
                overlay(
                text(text, DEFAULT_FONT, FULL_HEIGTH/30, BLACK),
                rectangle(FULL_WIDTH/5 + FULL_WIDTH/50, FULL_HEIGTH/15, backColor)
            ),
            rectangle(FULL_WIDTH/5 + FULL_WIDTH/50 + 10, FULL_HEIGTH/15 + 10, borderColor)
        );
    }

    /**
     * it represents the easy mode button
     * @return a green button 
     */
    public static Graphic easy() {
        return modeButtonBackground("EASY", rgb(90, 232, 132), rgb(0, 95, 5));
    }

    /**
     * it represents the hard mode button
     * @return a red button 
     */
    public static Graphic hard() {
        return modeButtonBackground("HARD", rgb(255, 63, 63), rgb(230, 0, 0));
    }

    /**
     * it represents the name of all the creator of the game
     * @return three strings that all the name of the creator
     */
    public static Graphic creator() {
        Graphic created = beside(rectangle(10,0,TRANSPARENT), text("Created by:", DEFAULT_FONT, 10, WHITE));
        Graphic author1 = beside(rectangle(10,0,TRANSPARENT), text("Maccario Simone", DEFAULT_FONT, 10, WHITE));
        Graphic author2 = beside(rectangle(10,0,TRANSPARENT), text("Sasanelli Alessandra", DEFAULT_FONT, 10, WHITE));
        return Toolbelt.aboves(of(created, DEFAULT_SEPARE, author1, DEFAULT_SEPARE, author2));
    }

    /**
     * it represents the version of the game
     * @return a string with the version of the game
     */
    public static Graphic version() {
        return text("Version 1.0.0", DEFAULT_FONT, 10, WHITE);
    }

    /**
     * it's used in all phases to generate all possible instructions that the player needs
     * @param button which corresponds to keyboard input
     * @param action which corresponds to the action if the button is pressed
     * @return a specific instruction for a specific button
     */
    public static final Graphic instruction(String button, String action) {
        return text("Press " + button + " button to " + action, DEFAULT_FONT, 10, WHITE);
    }

    /**
     * it's used in the last phase to generate a winner string
     * @param winner which corresponds to the winner player
     * @return a winner player string
     */
    public static final Graphic winner(String winner) {
        return overlay(
            pin(BOTTOM_CENTER, text(winner + " is the winner", DEFAULT_FONT, 25, YELLOW)),// rgb(59, 16, 96))),
            pin(BOTTOM_CENTER, rectangle(FULL_WIDTH, FULL_HEIGTH, rgb(165, 202, 255, .8)))
            // pin(BOTTOM_CENTER, rectangle(310, 30, rgb(165, 202, 255, .5)))
        );
    }
}