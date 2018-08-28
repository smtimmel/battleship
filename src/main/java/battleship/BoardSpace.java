package battleship;

/**
 * Maintains states of a space on the board.
 * @author Scott Timmel
 */
public class BoardSpace {

  private ShotStatus notFiredStatus = new NotFired(this);
  private ShotStatus firedStatus;
  private ShotStatus status = notFiredStatus;
  private String symbol;
  private String pos;

  public BoardSpace(String symbol) {
    this.symbol = symbol;
  }

  /**
   * Updates the status of this board space.
   * @param pos position of this board space
   * @return String message regarding the update
   */
  public String update(String pos) {
    this.pos = pos;
    return status.update();
  }

  /**
   * Facilitates a state transition.
   * @param status State which is transitioned to
   * @return message regarding the transition
   */
  public String transitionStatus(ShotStatus status) {
    this.status = status;
    return status.transition();
  }

  /**
   * Checks if board space has been fired to.
   * @return true is space has been fired to, false otherwise
   */
  public boolean shotFired() {
    return status.shotFired();
  }

  /**
   * Sets the state which will occur when a shot has been fired.
   * @param firedStatus state when shot is fired
   */
  public void setFired(ShotStatus firedStatus) {
    this.firedStatus = firedStatus;
  }

  /**
   * Gets state of board when shot is fired.
   * @return firedStatus
   */
  public ShotStatus getFired() {
    return firedStatus;
  }

  /**
   * Sets symbol to be printed when board displayed.
   * @param symbol printable symbol
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  /**
   * Retrieves symbol to be printed.
   * @return printable symbol
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * Gets position of board space.
   * @return position
   */
  public String getPosition() {
    return pos;
  }

}
