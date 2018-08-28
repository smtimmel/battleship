package battleship;

/**
 * Interface for BoardSpace states regarding if shot had been fired there and if ship hit.
 * @author Scott Timmel
 */
public interface ShotStatus {

  /**
   * Checks if shot fired to BoardSpace.
   * @return true if shot fired, false otherwise
   */
  public boolean shotFired();

  /**
   * Performs action when shot fired to.
   * @return result of action
   */
  public String update();

  /**
   * Performs action when state transitioned to.
   * @return result of action
   */
  public String transition();

}
