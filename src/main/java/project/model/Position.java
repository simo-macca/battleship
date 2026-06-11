package project.model;

import static jtamaro.en.Sequences.cons;
import static jtamaro.en.Sequences.empty;
import static jtamaro.en.Sequences.reverse;

import jtamaro.en.Sequence;
import jtamaro.en.io.Coordinate;

/**
 * Position represents the virtual positions on the logical grid; go from (0, 0) to (9, 9):
 * @param x the x-position
 * @param y the y-position
 */
public record Position(int x, int y) {

    /**
     * it uses to change coordinate to position
     * @param coordinate a old coordinate
     * @return a new position with the old coordinate
     */
    public static Position coordinateToPosition(Coordinate coordinate){
        return new Position(coordinate.x(), coordinate.y());
    }

    /**
     * it uses to know if the position is inside or outside the board
     * @return if the position is valid or no
     */
    public boolean isValidPosition() {
        return x >= 0 && x <= 9 && y >= 0 && y <= 9;
    }

    /**
     * it uses to generate all the possible board position
     * @return a m2d matrix
     */
    public static Sequence<Sequence<Position>> hittablePositions() {
        Sequence<Sequence<Position>> row = empty();
        Position pos;
        for (int i = 0; i < 10; i++) {
            Sequence<Position> col = empty();
            for (int j = 0; j < 9; j++) {
                pos = new Position(j, i);
                col = cons(pos, col);
            }
            pos = new Position(9, i);
            row = cons(cons(pos, col), row);
        }
        return reverse(row);
    }
}