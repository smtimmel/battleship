package battleship;

/**
 * BoardSpace shot status indicating space not shot to.
 * @author Scott Timmel
 */
public class NotFired implements ShotStatus {

  private BoardSpace space;

  public NotFired(BoardSpace space) {
    this.space = space;
  }

  /**
   * Returns false as shot is not been fired.
   */
  public boolean shotFired() {
    return false;
  }

  /**
   * Indicates a shot has been fired to the board position, transitions to the fired state.
   */
  public String update() {
    return space.transitionStatus(space.getFired());
  }

  /**
   * WILL NOT BE CALLED.
   */
  public String transition() {
    return null;
  }

}
