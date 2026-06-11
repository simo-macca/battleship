package project.model.players.directions;

import project.model.Position;

public record Up() implements Direction {
    
    public Position nextPosition(Position pos) {
        return new Position(pos.x(), pos.y() - 1);
    }

    @Override
    public Direction changeDir() {
        return new Right();
    }
}