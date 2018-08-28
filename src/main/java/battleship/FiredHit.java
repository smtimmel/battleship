package battleship;

import java.io.IOException;

/**
 * Board space shot status indicating board space fired to and hit occurred.
 * @author Scott Timmel
 */
public class FiredHit extends Fired {

  private Ship segment;

  public FiredHit(BoardSpace space, Ship segment) {
    super(space);
    this.segment = segment;
  }

  /**
   * Occurs as shot is fired, updates symbol and informs system of hit.
   */
  public String transition() {
    space.setSymbol("X");
    StringBuilder updateStr = new StringBuilder("Position " + space.getPosition() + " was a HIT.");
    try {
      //checks if ship sunk and updates ship with hit
      if (segment.sink(space.getPosition())) {
        updateStr.append("\nShip sunk!");
      }
      return updateStr.toString();
    } catch (IOException e) {
      return updateStr.toString();
    }
  }

}
