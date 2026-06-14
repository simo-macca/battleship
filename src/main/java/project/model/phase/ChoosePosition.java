package project.model.phase;

import static project.ui.cartesianWorldGraphics.CartesianWorldPosition.directConversion;

import project.model.*;
import project.Toolbelt;
import jtamaro.en.Graphic;
import jtamaro.en.Sequence;
import project.model.weapon.*;
import project.ui.board.Board;
import project.ui.phase.PhaseUI;
import jtamaro.en.io.Coordinate;
import project.model.players.Human;
import project.model.players.Player;
import project.configuration.ConstantsUtils;

/**
 * BeforeAttack renders the board of the player not playing with boats covered unless they have been sunk.
 * BeforeAttack renders the bombs of the player that is currently playing
*/
public record ChoosePosition() implements Phase {

    /**
     * it's used to update a player adding the current position into the projectiles' sequence
     */
    public Game handleMouse(Coordinate coordinate, Game game) {
        Position mouse = Position.coordinateToPosition(coordinate);
        Player currentPlayer = Toolbelt.get(game.playerIndex(), game.players());
        Player opponent = game.getOpponent();
        Board gameBoard = game.board();
        if(Board.isMouseOver(directConversion(mouse), directConversion(ConstantsUtils.BOARD_LEFT_CORNER_POSITION), Board.BOARD_LENGTH, Board.BOARD_LENGTH) && !Toolbelt.isInSequence(gameBoard.boardPositionMouseOn(directConversion(mouse)), Toolbelt.map((a) -> a.renderingPosition(), currentPlayer.weapons()))) {
            Weapon currentlyUsed =
                currentPlayer.selectedWeapon().weaponWithCorrectPlacement(gameBoard.boardPositionMouseOn(directConversion(mouse)).fromCartesianToVirtual(gameBoard.boardPosition()));
            Sequence<Projectile> projectiles = Player.addWeapon(game, currentlyUsed, opponent.boats());
            Player newP = new Human(
                currentPlayer.playerName(), 
                currentPlayer.boats(), 
                currentlyUsed, 
                projectiles
            );
            return new Game(new AfterAttack(), gameBoard, game.playerIndex(), game.updateCurrentPlayer(newP), game.quit());
        }
        return game;
    }

    public Graphic render(Game game) {
        return PhaseUI.renderChoosePosition(game);
    }
}