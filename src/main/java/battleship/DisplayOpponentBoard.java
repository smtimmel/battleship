package battleship;

/**
 * Displays the state of the opponents board.
 * @author Scott Timmel
 */
public class DisplayOpponentBoard extends DisplayBoard {

  /**
   * Provides only the main board to user.
   */
  public String getBoard(BoardSpace[][] board) {
    return mainBoard(board);
  }

  /**
   * Presents the board space symbol if a shot has been fired, else presents the empty water symbol.
   */
  public String boardSymbol(BoardSpace space) {
    if (space.shotFired()) {
      return space.getSymbol();
    } else {
      return "-";
    }
  }

}
