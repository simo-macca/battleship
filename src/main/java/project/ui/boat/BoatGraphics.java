package project.ui.boat;

import static jtamaro.en.Graphics.pin;
import static jtamaro.en.Graphics.above;
import static jtamaro.en.Graphics.beside;
import static jtamaro.en.Points.TOP_LEFT;
import static jtamaro.en.Sequences.first;
import static jtamaro.en.Sequences.range;
import static jtamaro.en.Sequences.reduce;
import static jtamaro.en.Graphics.rectangle;
import static jtamaro.en.Colors.TRANSPARENT;
import static jtamaro.en.Graphics.emptyGraphic;
import static project.ui.cartesianWorldGraphics.CartesianWorldPosition.*;

import project.Toolbelt;
import jtamaro.en.Color;
import jtamaro.en.Graphic;
import jtamaro.en.Sequence;
import project.ui.board.Board;
import project.model.Position;
import project.model.boat.Boat;
import project.configuration.ConstantsUtils;
import project.ui.cartesianWorldGraphics.CartesianWorldPosition;

/**
 * BoardGraphics are the same boats as the players but are rendered on the canvas, all have:
 * @param position the first position
 * @param graphic the boat graphic
 * @param sunk if it is sunken or no
 * @param vertical if it is vertical or no
 * @param units the number of the boat's unit
 * @param color its color
 */

public record BoatGraphics(CartesianWorldPosition position, Graphic graphic, boolean sunk, boolean vertical, int units, Color color) {

    public int length(){
        return ConstantsUtils.TILE_LENGTH*units;
    }

    public int height(){
        return ConstantsUtils.TILE_LENGTH;
    }

    public boolean overlaps(BoatGraphics boat){

        if(this.position.equals(boat.position)){
            return true;
        }

        if(!this.vertical && !boat.vertical){
            return overlapSameOrientation(
                this.position.y(),
                boat.position.y(),
                this.position.x(),
                boat.position.x(),
                this.length(),
                boat.length());
        } else if(this.vertical && boat.vertical){
            return overlapSameOrientation(
                this.position.x(),
                boat.position.x(),
                this.position.y(),
                boat.position.y(),
                this.length(),
                boat.length());
        } else if(!this.vertical && boat.vertical){
            return overlapVH(boat, this);
        } else if(this.vertical && !boat.vertical){
            return overlapVH(this, boat);
        }
        return false;
    }

    public static boolean overlapVH(BoatGraphics vertical, BoatGraphics horizontal){
        return horizontal.position.y() <= vertical.position.y() && horizontal.position.y() > vertical.position.y() - vertical.length()
                ? horizontal.position.x() < vertical.position.x()
                    ? vertical.position.x() - horizontal.position.x() < horizontal.length()
                    : horizontal.position.x() == vertical.position.x()
                : false;
    }

    public static boolean overlapSameOrientation(int x1, int x2, int y1, int y2, int l1, int l2){
        return x1 == x2
                ? y1 < y2
                    ? y2 - y1 < l1
                    : y1 - y2 < l2
                : false;
    }

    public static Graphic boatGraphic(int units, boolean sunk, boolean vertical, Color color){
        Graphic separe = rectangle(1, 1, TRANSPARENT);
        Graphic unit = Toolbelt.square(ConstantsUtils.TILE_LENGTH - 1, color);
        return sunk?
            vertical? 
                pin(TOP_LEFT, reduce(emptyGraphic(), (a, b) -> above(above(unit, separe), b), range(units))):
                pin(TOP_LEFT, reduce(emptyGraphic(), (a, b) -> beside(beside(unit, separe), b), range(units))):
            emptyGraphic();
    }

    public static Sequence<BoatGraphics> fromLogicBoatsToPhysicalBoats(Sequence<Boat> boats){
        return Toolbelt.map(
            (a) -> a.fromLogicBoatToBoatGraphic(
                    positionToPhysicalBoardPosition(
                        directConversion(ConstantsUtils.BOARD_LEFT_CORNER_POSITION), 
                        a.startingPosition(), 
                        ConstantsUtils.TILE_LENGTH)
                    ),
            boats
        );
    }

    public BoatGraphics rotatedBoatGraphic(){
        return new BoatGraphics(position, BoatGraphics.boatGraphic(units, sunk, !vertical, color), sunk, !vertical, units, color);
    }

    public static BoatGraphics boatClickedOn(CartesianWorldPosition pos, Sequence<BoatGraphics> boats){
        Sequence<BoatGraphics> b = Toolbelt.filter((a) -> a.isMouseOverBoat(pos), boats);
        int index = b.isEmpty()? -1: Toolbelt.getIndex(first(b), boats);
        return index != -1? Toolbelt.get(index, boats): null;
    }

    public boolean isMouseOverBoat(CartesianWorldPosition mousePos) {
        int h = vertical? length(): height();
        int w = vertical? height(): length();
        return Board.isMouseOver(mousePos, position, h, w);
    }

    public BoatGraphics movedPosition(CartesianWorldPosition pos){
        return new BoatGraphics(pos, graphic, sunk, vertical, units, color);
    }

    public Boat createVirtual(Board board){
        Position positionOnVirtualBoard = this.position.fromCartesianToVirtual(board.boardPosition());
        return new Boat(
            positionOnVirtualBoard, 
            Boat.startingHittablePositions(positionOnVirtualBoard, units, vertical), 
            false, 
            vertical, 
            units,
            color
        );
    }

    public boolean isBoatGraphicInsideBoard(int units, CartesianWorldPosition analyzedPosition, Board board){
        return units == 0?
            true:
            board.isPositionOverBoard(analyzedPosition)?
                isBoatGraphicInsideBoard(
                    units - 1, 
                    vertical?
                        new CartesianWorldPosition(analyzedPosition.x(), analyzedPosition.y() - ConstantsUtils.TILE_LENGTH):
                        new CartesianWorldPosition(analyzedPosition.x() + ConstantsUtils.TILE_LENGTH, analyzedPosition.y()),
                    board
                ):
                false;
    }

    public static Sequence<Boat> boatGraphicsToLogicBoats(Sequence<BoatGraphics> boats, Board board){
        return Toolbelt.map(
                (a) -> a.createVirtual(board),
                boats
            );
    }
}