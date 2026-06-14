package project.model.phase;

import static jtamaro.en.Sequences.*;
import static project.ui.cartesianWorldGraphics.CartesianWorldPosition.*;

import jtamaro.en.Graphic;
import jtamaro.en.Sequence;
import jtamaro.en.io.Coordinate;
import jtamaro.en.io.KeyboardChar;
import project.Toolbelt;
import project.configuration.ConstantsUtils;
import project.model.*;
import project.model.boat.*;
import project.model.players.Player;
import project.ui.board.Board;
import project.ui.boat.BoatGraphics;
import project.ui.cartesianWorldGraphics.CartesianWorldPosition;
import project.ui.phase.PhaseUI;

/**
 * PlayerSetting phases render the board without any boat on it. Here the players can "create" their
 * dispositions In this phase of the game, the current player drugs the boat he wants to place on
 * the grid and drops it on the position he wants The done eyboard input lends on the player
 * settings of the second player if he has not choosen yet or to the choose weapon phase for the
 * first player to play
 */
public record PlayerSetting(
    BoatGraphics choosenBoat,
    Sequence<BoatGraphics> boats,
    Sequence<BoatGraphics> defaults,
    int selectedBoatIndex,
    int boatsRemainingToBePlaced,
    boolean clickedOnBoat)
    implements Phase {

  private record ThripleReturn(BoatGraphics boat, Sequence<BoatGraphics> boats, int boatIndex) {}

  public Game handleKey(KeyboardChar keyChar, Game game) {
    return Character.toUpperCase(keyChar.getChar()) == 'N' && boatsRemainingToBePlaced == 0
        ? game.getOpponent().playerSetting(this, game)
        : Character.toUpperCase(keyChar.getChar()) == 'S' ? rotateChoosenBoat(game) : game;
  }

  public ThripleReturn returnUpdatedBoatAndSequence(BoatGraphics newBoat) {
    int currentIndex = this.selectedBoatIndex;
    BoatGraphics currentBoat = this.choosenBoat;
    Sequence<BoatGraphics> newBoatSequence = this.boats;
    if (boatClicked()) {
      currentIndex = Toolbelt.getIndex(currentBoat, newBoatSequence);
      currentBoat = newBoat;
      newBoatSequence =
          currentIndex != -1
              ? Toolbelt.set(currentIndex, currentBoat, newBoatSequence)
              : newBoatSequence;
    }
    return new ThripleReturn(currentBoat, newBoatSequence, currentIndex);
  }

  public Game rotateChoosenBoat(Game game) {
    ThripleReturn t =
        returnUpdatedBoatAndSequence(choosenBoat != null ? choosenBoat.rotatedBoatGraphic() : null);
    return game.changePhase(this.updateCurrentBoatAndSequence(t.boat(), t.boats()));
  }

  public PlayerSetting updateCurrentBoatAndSequence(
      BoatGraphics newBoat, Sequence<BoatGraphics> boats) {
    return this.updateCurrentBoat(newBoat).updateBoatsSequence(boats);
  }

  public boolean boatClicked() {
    return this.clickedOnBoat && this.choosenBoat != null;
  }

  public PlayerSetting updateCurrentBoat(BoatGraphics newBoat) {
    return new PlayerSetting(
        newBoat,
        this.boats,
        this.defaults,
        this.selectedBoatIndex,
        this.boatsRemainingToBePlaced,
        this.clickedOnBoat);
  }

  public PlayerSetting updateBoatsSequence(Sequence<BoatGraphics> newBoat) {
    return new PlayerSetting(
        this.choosenBoat,
        newBoat,
        this.defaults,
        this.selectedBoatIndex,
        this.boatsRemainingToBePlaced,
        this.clickedOnBoat);
  }

  public PlayerSetting updateSelectedBoatIndex(int index) {
    return new PlayerSetting(
        this.choosenBoat,
        this.boats,
        this.defaults,
        index,
        this.boatsRemainingToBePlaced,
        this.clickedOnBoat);
  }

  public PlayerSetting updateBoatsToBePlaced(int toBePlaced) {
    return new PlayerSetting(
        this.choosenBoat,
        this.boats,
        this.defaults,
        this.selectedBoatIndex,
        toBePlaced,
        this.clickedOnBoat);
  }

  public PlayerSetting updateClickedOn(boolean clickedOn) {
    return new PlayerSetting(
        this.choosenBoat,
        this.boats,
        this.defaults,
        this.selectedBoatIndex,
        this.boatsRemainingToBePlaced,
        clickedOn);
  }

  /**
   * it is used to change a player or to move to the next stage iff all boats have been placed.
   *
   * @param game is the current game
   * @return a new game in the same phase, if the player index was 0, or a new game in the next
   *     phase if the player index is 1
   */
  public Game changePlayer(Game game) {
    Sequence<Boat> definitiveBoats =
        BoatGraphics.boatGraphicsToLogicBoats(this.boats, game.board());
    Player newPl = game.currentPlayer().setBoats(definitiveBoats);
    return game.isLastPlayer()
        ? game.changePhase(new ChooseWeapon())
        : game.changePhaseWithUpdatedPlayer(
                this.updateBoatsSequence(this.defaults)
                    .updateSelectedBoatIndex(-1)
                    .updateBoatsToBePlaced(4),
                newPl)
            .nextPlayer();
  }

  public Game handleMouse(Coordinate coordinate, Game game) {
    Position mousePos = Position.coordinateToPosition(coordinate);
    BoatGraphics selected = BoatGraphics.boatClickedOn(directConversion(mousePos), this.boats);
    boolean clicked = !this.clickedOnBoat && selected != null;
    PlayerSetting ps =
        this.updateCurrentBoat(selected)
            .updateBoatsToBePlaced(
                alreadyPlacedBoat(selected, clicked, game.board())
                    ? this.boatsRemainingToBePlaced + 1
                    : this.boatsRemainingToBePlaced)
            .updateClickedOn(clicked);
    return game.changePhase(ps);
  }

  public boolean alreadyPlacedBoat(BoatGraphics selected, boolean clicked, Board board) {
    return clicked && selected != null && board.isPositionOverBoard(selected.position());
  }

  public boolean currentBoatNotOverlaps(
      BoatGraphics boat, Sequence<BoatGraphics> boats, Board board) {
    return isEmpty(boats)
        ? true
        : !boat.equals(first(boats))
                && board.isPositionOverBoard(boat.position())
                && board.isPositionOverBoard(first(boats).position())
            ? !boat.overlaps(first(boats)) && currentBoatNotOverlaps(boat, rest(boats), board)
            : currentBoatNotOverlaps(boat, rest(boats), board);
  }

  @Override
  public Game mouseMoveHandler(Coordinate coordinate, Game game) {
    Position mousePos = Position.coordinateToPosition(coordinate);
    ThripleReturn t =
        returnUpdatedBoatAndSequence(
            this.choosenBoat != null
                ? this.choosenBoat.movedPosition(
                    new CartesianWorldPosition(mousePos.x(), ConstantsUtils.HEIGTH - mousePos.y()))
                : null);
    PlayerSetting ps =
        this.updateCurrentBoatAndSequence(t.boat(), t.boats())
            .updateSelectedBoatIndex(t.boatIndex());
    return game.changePhase(ps);
  }

  public boolean canChoosenBoatBeReleased(Board board) {
    return this.choosenBoat.isBoatGraphicInsideBoard(
        this.choosenBoat.units(), this.choosenBoat.position(), board);
  }

  public boolean hasBoatIndexBeenSet() {
    return this.selectedBoatIndex != -1;
  }

  public boolean canBoatBeAddedToPlayersBoats(
      Board board, BoatGraphics boat, Sequence<BoatGraphics> boats) {
    return boatClicked()
        && hasBoatIndexBeenSet()
        && canChoosenBoatBeReleased(board)
        && currentBoatNotOverlaps(boat, boats, board);
  }

  public BoatGraphics moveBoat(Board board, CartesianWorldPosition position, BoatGraphics boat) {
    return boatClicked() && board.isPositionOverBoard(position)
        ? this.choosenBoat.movedPosition(board.boardPositionMouseOn(position))
        : this.choosenBoat;
  }

  @Override
  public Game mouseReleaseHandler(Coordinate coordinate, Game game) {
    Position mousePos = Position.coordinateToPosition(coordinate);
    Board board = game.board();
    BoatGraphics choosenBoat = moveBoat(board, directConversion(mousePos), this.choosenBoat);
    if (canBoatBeAddedToPlayersBoats(board, choosenBoat, this.boats)) {
      Sequence<BoatGraphics> boats = Toolbelt.set(this.selectedBoatIndex, choosenBoat, this.boats);
      Player newPl =
          game.currentPlayer().setBoats(BoatGraphics.boatGraphicsToLogicBoats(boats, board));
      PlayerSetting ps =
          this.updateBoatsSequence(boats)
              .updateBoatsToBePlaced(this.boatsRemainingToBePlaced - 1)
              .updateClickedOn(false);
      return game.changePhaseWithUpdatedPlayer(ps, newPl);
    }
    PlayerSetting ps =
        this.updateBoatsSequence(
                hasBoatIndexBeenSet()
                    ? Toolbelt.set(
                        this.selectedBoatIndex,
                        Toolbelt.get(this.selectedBoatIndex, this.defaults),
                        this.boats)
                    : this.boats)
            .updateClickedOn(false);
    return game.changePhase(ps);
  }

  public Graphic render(Game game) {
    return PhaseUI.renderPlayerSettings(game, this);
  }
}
