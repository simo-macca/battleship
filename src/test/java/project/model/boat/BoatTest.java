package project.model.boat;

import static jtamaro.en.Colors.BLUE;
import static jtamaro.en.Colors.GREEN;
import static jtamaro.en.Colors.MAGENTA;
import static jtamaro.en.Colors.YELLOW;
import static jtamaro.en.Sequences.of;
import static jtamaro.en.Sequences.concat;
import static jtamaro.en.Sequences.empty;

import org.junit.Assert;
import org.junit.Test;
import jtamaro.en.Sequence;
import project.model.Position;

public class BoatTest {

    @Test
    public void startingHittablePositionsTest(){
        Sequence<Position> dPosH = of(
            new Position(0, 0),
            new Position(1, 0)
        );
        Sequence<Position> dPosV = of(
            new Position(0, 0),
            new Position(0, 1)
        );
        Sequence<Position> sPosH = of(
            new Position(1, 0),
            new Position(2, 0),
            new Position(3, 0)
        );
        Sequence<Position> sPosV = of(
            new Position(1, 0),
            new Position(1, 1),
            new Position(1, 2)
        );
        Sequence<Position> bPosH = of(
            new Position(2, 0),
            new Position(3, 0),
            new Position(4, 0),
            new Position(5, 0)
        );
        Sequence<Position> bPosV = of(
            new Position(1, 0),
            new Position(1, 1),
            new Position(1, 2),
            new Position(1, 3)
        );
        Sequence<Position> cPosH = of(
            new Position(2, 0),
            new Position(3, 0),
            new Position(4, 0),
            new Position(5, 0),
            new Position(6, 0)
        );
        Sequence<Position> cPosV = of(
            new Position(1, 0),
            new Position(1, 1),
            new Position(1, 2),
            new Position(1, 3),
            new Position(1, 4)
        );
        Assert.assertEquals(dPosH, Boat.startingHittablePositions(new Position(0, 0), 2, false));
        Assert.assertEquals(dPosV, Boat.startingHittablePositions(new Position(0, 0), 2, true));
        
        Assert.assertEquals(sPosH, Boat.startingHittablePositions(new Position(1, 0), 3, false));
        Assert.assertEquals(sPosV, Boat.startingHittablePositions(new Position(1, 0), 3, true));
        
        Assert.assertEquals(bPosH, Boat.startingHittablePositions(new Position(2, 0), 4, false));
        Assert.assertEquals(bPosV, Boat.startingHittablePositions(new Position(1, 0), 4, true));
        
        Assert.assertEquals(cPosH, Boat.startingHittablePositions(new Position(2, 0), 5, false));
        Assert.assertEquals(cPosV, Boat.startingHittablePositions(new Position(1, 0), 5, true));
    }

    private static final Boat DESTROYER_H = new Boat(new Position(0, 0), Boat.startingHittablePositions(new Position(0, 0), 2, false), false, false, 2, GREEN);
    private static final Boat SUBMARINE_H = new Boat(new Position(0, 0), Boat.startingHittablePositions(new Position(0, 0), 3, false), false, false, 3, BLUE);
    private static final Boat BATTLESHIP_H = new Boat(new Position(0, 0), Boat.startingHittablePositions(new Position(0, 0), 4, false), false, false, 4, YELLOW);
    private static final Boat CARRIER_H = new Boat(new Position(0, 0), Boat.startingHittablePositions(new Position(0, 0), 5, false), false, false, 5, MAGENTA);

    private static final Boat DESTROYER_V = new Boat(new Position(0, 0), Boat.startingHittablePositions(new Position(0, 0), 2, true), false, true, 2, GREEN);
    private static final Boat SUBMARINE_V = new Boat(new Position(0, 0), Boat.startingHittablePositions(new Position(0, 0), 3, true), false, true, 3, BLUE);
    private static final Boat BATTLESHIP_V = new Boat(new Position(0, 0), Boat.startingHittablePositions(new Position(0, 0), 4, true), false, true, 4, YELLOW);
    private static final Boat CARRIER_V = new Boat(new Position(0, 0), Boat.startingHittablePositions(new Position(0, 0), 5, true), false, true, 5, MAGENTA);

    private static final Boat SUNK_DESTROYER = new Boat(new Position(0, 0), empty(), false, false, 2, GREEN);
    private static final Boat SUNK_SUBMARINE = new Boat(new Position(0, 0), empty(), false, false, 3, BLUE);
    private static final Boat SUNK_BATTLESHIP = new Boat(new Position(0, 0), empty(), false, false, 4, YELLOW);
    private static final Boat SUNK_CARRIER = new Boat(new Position(0, 0), empty(), false, false, 5, MAGENTA);

    private static final Sequence<Boat> VERTICALS = of(DESTROYER_V, SUBMARINE_V, BATTLESHIP_V, CARRIER_V);
    private static final Sequence<Boat> HORIZONTALS = of(DESTROYER_H, SUBMARINE_H, BATTLESHIP_H, CARRIER_H);

    @Test
    public void allHittablePositionsOnBoardTest(){
        Sequence<Position> verticalResult = 
            concat(
                DESTROYER_V.hittablePositions(),
                concat(
                    SUBMARINE_V.hittablePositions(),
                    concat(
                        BATTLESHIP_V.hittablePositions(),
                        CARRIER_V.hittablePositions()
                    )
                )
            );

        Sequence<Position> horizontalResult = 
            concat(
                DESTROYER_H.hittablePositions(),
                concat(
                    SUBMARINE_H.hittablePositions(),
                    concat(
                        BATTLESHIP_H.hittablePositions(),
                        CARRIER_H.hittablePositions()
                    )
                )
            );

        Assert.assertEquals(verticalResult, Boat.allHittablePositionsOnBoard(VERTICALS));
        Assert.assertEquals(horizontalResult, Boat.allHittablePositionsOnBoard(HORIZONTALS));
    }

    @Test
    public void hitTest(){
        Assert.assertTrue(DESTROYER_H.hit(new Position(1, 0)));
        Assert.assertFalse(CARRIER_V.hit(new Position(9, 7)));
        Assert.assertTrue(SUBMARINE_H.hit(new Position(1, 0)));
        Assert.assertFalse(BATTLESHIP_H.hit(new Position(1, 1)));
        Assert.assertTrue(CARRIER_H.hit(new Position(4, 0)));
        Assert.assertFalse(DESTROYER_V.hit(new Position(2, 2)));
    }
    

    @Test
    public void sunkenTest(){
        Assert.assertEquals(DESTROYER_H, DESTROYER_H.sunken());
        Assert.assertEquals(DESTROYER_V, DESTROYER_V.sunken());
        Assert.assertEquals(SUBMARINE_H, SUBMARINE_H.sunken());
        Assert.assertEquals(SUBMARINE_V, SUBMARINE_V.sunken());
        Assert.assertEquals(BATTLESHIP_H, BATTLESHIP_H.sunken());
        Assert.assertEquals(BATTLESHIP_V, BATTLESHIP_V.sunken());
        Assert.assertEquals(CARRIER_H, CARRIER_H.sunken());
        Assert.assertEquals(CARRIER_V, CARRIER_V.sunken());
        Assert.assertEquals(SUNK_DESTROYER.sunkBoat(), SUNK_DESTROYER.sunken());
        Assert.assertEquals(SUNK_SUBMARINE.sunkBoat(), SUNK_SUBMARINE.sunken());
        Assert.assertEquals(SUNK_BATTLESHIP.sunkBoat(), SUNK_BATTLESHIP.sunken());
        Assert.assertEquals(SUNK_CARRIER.sunkBoat(), SUNK_CARRIER.sunken());
    }
}
