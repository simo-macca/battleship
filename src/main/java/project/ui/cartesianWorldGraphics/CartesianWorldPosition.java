package project.ui.cartesianWorldGraphics;

import project.model.Position;
import project.configuration.ConstantsUtils;

public record CartesianWorldPosition(int x, int y) {

    public static CartesianWorldPosition positionToPhysicalBoardPosition(CartesianWorldPosition boardPosition, Position virtualPosition, int tileLength){
        return new CartesianWorldPosition(
            boardPosition.x() + virtualPosition.x() * tileLength + 1,
            boardPosition.y() - virtualPosition.y() * tileLength - 1
        );
    }

    public static CartesianWorldPosition directConversion(Position position){
        return new CartesianWorldPosition(position.x(), position.y());
    }

    public Position fromCartesianToVirtual(CartesianWorldPosition boardPosition){
        return new Position((x - boardPosition.x())/ConstantsUtils.TILE_LENGTH , (boardPosition.y() - y)/ConstantsUtils.TILE_LENGTH);
    }
}