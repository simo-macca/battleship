package project.ui.cartesianWorldGraphics;

import static jtamaro.en.Graphics.*;
import static jtamaro.en.Sequences.*;

import project.Toolbelt;
import jtamaro.en.Graphic;
import jtamaro.en.Sequence;
import jtamaro.en.CartesianWorld;
import project.ui.boat.BoatGraphics;
import project.model.weapon.Projectile;
import project.ui.visitor.ProjectileRenderer;

/**
     * This class declares a cartesian world tuple, formed of:
     * @param position which is the main cartesian world where the graphic will placed on
     * @param graphic which corresponds to the graphic to be placed
     * It declares methods to place graphics onto the cartesian world using the tuples or not
     */
public record CartesianWorldTuples(CartesianWorldPosition position, Graphic graphic) {

    public static CartesianWorld placeOntoCartesianWorld(CartesianWorld cw, Sequence<CartesianWorldTuples> tuples){
        return isEmpty(tuples)?
            cw.place(0, 0, emptyGraphic()):
            placeOntoCartesianWorld(placeGraphic(cw, first(tuples).position(), first(tuples).graphic), rest(tuples));
    }

    public static CartesianWorld placeBoatsAndProjectiles(CartesianWorld cw, Sequence<Projectile> projectiles, Sequence<BoatGraphics> boats){
        return placeOntoCartesianWorld(
            cw, 
            concat(boatsToTuples(boats), projectilesToTuples(projectiles))
        );
    }

    public static Graphic renderGame(CartesianWorld cw, Sequence<CartesianWorldTuples> tuples){
        return placeOntoCartesianWorld(cw, tuples).asGraphic();
    }

    /**
     * it's used by all the phase to generate some graphic of the GameUI
     * @param cw which is the main cartesian world where the graphic will placed
     * @param pos the right position where the graphic will placed
     * @param graphic which corresponds to the graph to be placed
     * @return a new cartesian world in which the graph is placed in the background
     */
    public static CartesianWorld placeGraphic(CartesianWorld cw, CartesianWorldPosition pos, Graphic graphic) {
        return cw.place(
            pos.x(),
            pos.y(),
            graphic
        );
    }

    public static Sequence<CartesianWorldTuples> boatsToTuples(Sequence<BoatGraphics> boats){
        return Toolbelt.map((a) -> boatTuple(a), boats);
    }

    public static CartesianWorldTuples boatTuple(BoatGraphics boat){
        return new CartesianWorldTuples(boat.position(), boat.graphic());
    }

    public static Sequence<CartesianWorldTuples> projectilesToTuples(Sequence<Projectile> projectile){
        return Toolbelt.map((a) -> projectileTuple(a), projectile);
    }

    public static CartesianWorldTuples projectileTuple(Projectile projectile){
        return new CartesianWorldTuples(projectile.renderingPosition(), projectile.acceptVisitor(new ProjectileRenderer()));
    }
}