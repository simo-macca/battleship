package project.model.players.direction;

import org.junit.Assert;
import org.junit.Test;

import project.model.Position;
import project.model.players.directions.*;
import project.model.players.directions.Direction;

public class DirectionTest {
    
    private static final Direction RIGHT = new Right();
    private static final Direction DOWN = new Down();
    private static final Direction LEFT = new Left();
    private static final Direction UP = new Up();

    @Test
    public void changeDirTest() {
        Assert.assertEquals(DOWN, RIGHT.changeDir());
        Assert.assertEquals(LEFT, DOWN.changeDir());
        Assert.assertEquals(UP, LEFT.changeDir());
        Assert.assertEquals(RIGHT, UP.changeDir());
    }

    @Test
    public void nextPositionTest() {
        Position oldRight = new Position(5, 5);
        Position oldDown = new Position(2, 6);
        Position oldLeft = new Position(3, 4);
        Position oldUp = new Position(9, 6);
        Position newRight = new Position(6, 5);
        Position newDown = new Position(2, 7);
        Position newLeft = new Position(2, 4);
        Position newUp = new Position(9, 5);
        Assert.assertEquals(newRight, RIGHT.nextPosition(oldRight));
        Assert.assertEquals(newDown, DOWN.nextPosition(oldDown));
        Assert.assertEquals(newLeft, LEFT.nextPosition(oldLeft));
        Assert.assertEquals(newUp, UP.nextPosition(oldUp));
    }
}
