package battleship;

/**
 * Abstract class providing methods for shot status of board spcaes which were fired to.
 * @author Scott Timmel
 */
public abstract class Fired implements ShotStatus {

  protected BoardSpace space;

  public Fired(BoardSpace space) {
    this.space = space;
  }

  /**
   * Returns true as a shot has been fired.
   */
  public boolean shotFired() {
    return true;
  }

  /**
   * Informs the user they have already fired to the current position.
   */
  public String update() {
    return space.getPosition() + " fired to again.";
  }

}
