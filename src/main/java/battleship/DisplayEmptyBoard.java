package battleship;

/**
 * Displays the board that needs to be setup to the user.
 * @author Scott Timmel
 */
public class DisplayEmptyBoard extends DisplayBoard {

  /**
   * Only the main board is displayed.
   */
  public String getBoard(BoardSpace[][] board) {
    return mainBoard(board);
  }

  /**
   * Empty not yet shot to symbol printed in each position.
   */
  public String boardSymbol(BoardSpace space) {
    return "-";
  }

}
