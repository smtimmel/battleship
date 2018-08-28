package battleship;

/**
 * Board space shot status indicating shot fired and miss occurred.
 * @author Scott Timmel
 */
public class FiredMiss extends Fired {

  public FiredMiss(BoardSpace space) {
    super(space);
  }

  /**
   * Miss symbol set and position updated with miss.
   */
  public String transition() {
    space.setSymbol("*");
    return "Position " + space.getPosition() + " was a MISS.";
  }

}
