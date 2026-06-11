package project.model.players.directions;

import project.model.Position;

public interface Direction {

    /**
     * it uses to know the next position in according with the current direction
     * @param pos the old pos
     * @return the next position based on the previous one
     */
    public Position nextPosition(Position pos);

    /**
     * it uses to change the current dir
     * @return the ne direction
     */
    public Direction changeDir();
}