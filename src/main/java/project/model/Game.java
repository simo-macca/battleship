package project.model;

import jtamaro.en.Graphic;
import jtamaro.en.Sequence;
import jtamaro.en.io.Coordinate;
import jtamaro.en.io.KeyboardChar;
import project.Toolbelt;
import project.configuration.ConstantsUtils;
import project.model.phase.*;
import project.model.players.Player;
import project.ui.board.Board;
import project.ui.cartesianWorldGraphics.CartesianWorldPosition;

/**
 * our game is based on 4 principles parameter:
 *
 * @param roundPhase that represent in which phase of the game the player is
 * @param board a matrix 2d to render the board of the game in which there will the players' boats
 * @param playerIndex that knows which of the two players is actually playing
 * @param players a sequence of player that can be two Player or one Player and one bot
 */
public record Game(
    Phase roundPhase, Board board, int playerIndex, Sequence<Player> players, boolean quit) {

  /**
   * creat a new game, the main page
   *
   * @return the frame of the main page
   */
  public static Game create() {
    return new Game(
        new Mode(),
        new Board(new CartesianWorldPosition(125, 425)),
        0,
        ConstantsUtils.PLAYERS,
        false);
  }

  /**
   * it uses to know which player is playing
   *
   * @return the current player that is playing
   */
  public Player currentPlayer() {
    return Toolbelt.get(this.playerIndex(), this.players);
  }

  /**
   * it is used to update the player sequence with one of the updated players
   *
   * @param newCurrentPlayer the new player to update into the sequence
   * @return the updated list
   */
  public Sequence<Player> updateCurrentPlayer(Player newCurrentPlayer) {
    return Toolbelt.set(playerIndex, newCurrentPlayer, players);
  }

  /**
   * Check if the current player is the last player.
   *
   * @return true if the current player is the last player, false otherwise
   */
  public boolean isLastPlayer() {
    return this.playerIndex == Toolbelt.length(this.players) - 1;
  }

  /**
   * Handle a key press. In this game, we can handle the key independently of the phase. If we press
   * the Q key in any phase, the application will close automatically
   *
   * @param keyChar the key that was pressed
   * @return the game after handling the key press
   */
  public Game handleKey(KeyboardChar keyChar) {
    return Character.toUpperCase(keyChar.getChar()) == 'Q'
        ? new Game(roundPhase, board, playerIndex, players, true)
        : Character.toUpperCase(keyChar.getChar()) == 'R'
            ? Game.create()
            : roundPhase.handleKey(keyChar, this);
  }

  /**
   * Handle a mouse click. The handling depends on the current phase.
   *
   * @param coordinate which use to know where the mouse is
   * @return the game after handling the mouse click
   */
  public Game handleMouse(Coordinate coordinate) {
    return roundPhase.handleMouse(coordinate, this);
  }

  /**
   * Handle a mouse when it is pressed and moved. The handling depends on the current phase.
   *
   * @param coordinate which use to know where the mouse is
   * @return the game after handling the mouse move
   */
  public Game mouseMoveHandler(Coordinate coordinate) {
    return roundPhase.mouseMoveHandler(coordinate, this);
  }

  /**
   * Handle a mouse when it is released. The handling depends on the current phase.
   *
   * @param coordinate which use to know where the mouse is
   * @return the game after handling the mouse released
   */
  public Game mouseReleaseHandler(Coordinate coordinate) {
    return roundPhase.mouseReleaseHandler(coordinate, this);
  }

  /**
   * Change the phase of the game. This is used by the various phases to transition to another
   * (possibly different) phase.
   *
   * @param newPhase the phase the game should transition to
   * @return the game with the new phase
   */
  public Game changePhase(Phase newPhase) {
    return new Game(newPhase, this.board, this.playerIndex, this.players, this.quit);
  }

  /**
   * Change the phase of the game. This is used by the various phases to transition to another
   * (possibly different) phase.
   *
   * @param newPhase the phase the game should transition to
   * @return the game with the new phase
   */
  public Game changePhaseWithUpdatedPlayer(Phase newPhase, Player newPl) {
    return new Game(
        newPhase, this.board, this.playerIndex, this.updateCurrentPlayer(newPl), this.quit);
  }

  /**
   * it uses to change the player
   *
   * @return a new game with a new player index
   */
  public Game nextPlayer() {
    return new Game(this.roundPhase, this.board, advancePlayer(), this.players, this.quit);
  }

  /**
   * Advance to the next player by increasing the current player index by one. This can only be
   * called (assert) if the current player is not the last player.
   *
   * @return the game with the incremented current player index
   */
  public int advancePlayer() {
    return isLastPlayer() ? 0 : 1;
  }

  /**
   * it uses to know who are the enemy
   *
   * @return the opponent of the current player
   */
  public Player getOpponent() {
    return playerIndex == 0 ? Toolbelt.get(1, players) : Toolbelt.get(0, players);
  }

  /**
   * Determine if a player is a winner, i.e., if all its boats are sunken.
   *
   * @param player the number of the sunken boats
   * @return true if the player is a winner, false otherwise
   */
  public boolean isWinner(Player player) {
    return Player.hasWon(getOpponent());
  }

  /**
   * it uses by all the phase to render itself
   *
   * @return the game graphic of the phase the player is in.
   */
  public Graphic render() {
    return roundPhase.render(this);
  }
}
