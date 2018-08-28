package battleship;

/**
 * Abstract class providing template for behaviors for displaying game boards to users.
 * @author Scott Timmel
 */
public abstract class DisplayBoard {

  /**
   * Main step for printing the board to the user.
   * @param board board spaces to be printed
   * @return String representing the board
   */
  protected String mainBoard(BoardSpace[][] board) {
    StringBuilder boardStr = new StringBuilder("  ");
    //Prints column labels
    for (int i = 0; i < board.length; i++) {
      boardStr.append((Player.getColumnList()[i]) + " ");
    }
    boardStr.append("\n");
    //Prints each row of board
    for (int i = 0; i < board.length; i++) {
      boardStr.append((i + 1) + " ");
      for (int j = 0; j < board.length; j++) {
        boardStr.append(boardSymbol(board[i][j]) + " ");
      }
      boardStr.append("\n");
    }
    return boardStr.toString();
  }

  /**
   * Returns entire printout to user.
   * @param board spaces to be printed
   * @return String to be displayed to user
   */
  public abstract String getBoard(BoardSpace[][] board);

  /**
   * Gets the symbol to be displayed for each board space.
   * @param space having symbol
   * @return symbol to be printed
   */
  public abstract String boardSymbol(BoardSpace space);

}
