package project.model.players.directions;

import project.model.Position;

public record Right() implements Direction {

  public Position nextPosition(Position pos) {
    return new Position(pos.x() + 1, pos.y());
  }

  @Override
  public Direction changeDir() {
    return new Down();
  }
}
