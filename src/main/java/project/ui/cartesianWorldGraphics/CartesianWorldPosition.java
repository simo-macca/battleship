package project.ui.cartesianWorldGraphics;

import project.model.Position;
import project.configuration.ConstantsUtils;

public record CartesianWorldPosition(int x, int y) {

    public static CartesianWorldPosition positionToPhisicalBoardPosition(CartesianWorldPosition boardPosition, Position virtualPosition, int tileLenght){
        return new CartesianWorldPosition(
            boardPosition.x() + virtualPosition.x()*tileLenght + 1,
            boardPosition.y() - virtualPosition.y()*tileLenght - 1
        );
    }

    public static CartesianWorldPosition directConversion(Position position){
        return new CartesianWorldPosition(position.x(), position.y());
    }

    public Position fromCartesianToVirtual(CartesianWorldPosition boardPosition){
        return new Position((x - boardPosition.x())/ConstantsUtils.TILE_LENGTH , (boardPosition.y() - y)/ConstantsUtils.TILE_LENGTH);
    }
}