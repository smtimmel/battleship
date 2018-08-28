package battleship;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Performs checks for ship status as sunk or afloat.
 * @author Scott Timmel
 */
public class Ship {

  private HashSet<String> segmentPos = new HashSet<>();
  private Player owner;

  public Ship(String[] positions) {
    Arrays.stream(positions).forEach(p -> segmentPos.add(p));
  }

  /**
   * Set the Player controlling this Ship.
   * @param owner controlling Player
   */
  public void setOwner(Player owner) {
    this.owner = owner;
  }

  /**
   * Checks if ship has sunk, informs Player if so.
   * @param pos position which has been hit
   * @return true if Ship is sunk, false otherwise
   * @throws IOException not applicable
   */
  public boolean sink(String pos) throws IOException {
    if (segmentPos.remove(pos)) {
      if (segmentPos.isEmpty()) {
        //Player informed of Ship sinking
        owner.sinkShip(this);
        return true;
      }
    }
    return false;
  }

}
