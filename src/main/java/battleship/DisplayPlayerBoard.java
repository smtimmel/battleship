package battleship;

/**
 * Displays state of the active player's board.
 * @author Scott Timmel
 */
public class DisplayPlayerBoard extends DisplayBoard {

  private String update = "";

  /**
   * Provides the main board along with the update from the previous player's turn.
   */
  public String getBoard(BoardSpace[][] board) {
    return mainBoard(board) + update;
  }

  /**
   * Gets the symbol held by the board space.
   */
  public String boardSymbol(BoardSpace space) {
    return space.getSymbol();
  }

  /**
   * Sets the update to be presented to the user along with board.
   * @param update of the previous player's turn
   */
  public void setUpdate(String update) {
    this.update = update;
  }

}
