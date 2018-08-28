package battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Factory for producing consistent ships, players and board spaces.
 * @author Scott Timmel
 */
public class GameFactory {

  private static GameFactory factory = null;
  private static final int NUM_PLAYERS = 2;
  private static final int MIN_BOARD_SIZE = 5;
  private static final int MAX_BOARD_SIZE = 9;
  private static final int MIN_SHIP_SIZE = 2;
  private static final int MAX_SHIP_SIZE = 4;
  private static final int SHIP_RATIO = 10;
  private int boardSize;
  private int playerId = 0;
  private boolean[] repeat = new boolean[NUM_PLAYERS];
  private ArrayList<HashMap<String, Ship>> shipPositions = new ArrayList<>(NUM_PLAYERS);
  private HashMap<Integer, Integer> shipMap = new HashMap<>();

  private GameFactory() {
    //Ship position map created for each player
    for (int i = 0; i < NUM_PLAYERS; i++) {
      shipPositions.add(new HashMap<>());
    }
    Random rand = new Random();
    //Random board size selected, number of ships based on size
    boardSize = rand.nextInt(MAX_BOARD_SIZE - MIN_BOARD_SIZE + 1) + MIN_BOARD_SIZE;
    int numShips = ((boardSize * boardSize) / SHIP_RATIO) + 1;
    int shipSize;
    //Each ships size randomly selected
    for (int i = 0; i < numShips; i++) {
      shipSize = rand.nextInt(MAX_SHIP_SIZE - MIN_SHIP_SIZE + 1) + MIN_SHIP_SIZE;
      if (shipMap.containsKey(shipSize)) {
        shipMap.put(shipSize, shipMap.get(shipSize) + 1);
      } else {
        shipMap.put(shipSize, 1);
      }
    }
  }

  /**
   * Gets this factory.
   * @return factory
   */
  public static GameFactory getFactory() {
    //Creates factory if method called for first time
    if (factory == null) {
      factory = new GameFactory();
    }
    return factory;
  }

  /**
   * Creates a Player object.
   * @return new Player
   */
  public Player createPlayer() {
    return new Player(playerId++, boardSize, shipMap);
  }

  /**
   * Creates a Ship object.
   * @param id of Player
   * @param positions requested for the ship
   * @return new Ship
   */
  public Ship createShip(int id, String[] positions) {
    int[][] location = {new int[positions.length], new int[positions.length]};
    for (int i = 0; i < positions.length; i++) {
      //Checks if a ship already in position
      if (shipPositions.get(id).containsKey(positions[i])) {
        return null;
      }
      //Attempts to get valid coordinates for the positions
      int[] coords = Player.convertCoord(positions[i], boardSize);
      if (coords == null) {
        return null;
      }
      for (int j = 0; j < location.length; j++) {
        location[j][i] = coords[j];
      }
    }
    repeat[id] = false;
    //Checks if converted coordinates are adjacent boat positions, if so Ship created
    for (int[] line : location) {
      if (!checkPositions(id, line)) {
        return null;
      }
    }
    if (repeat[id]) {
      Ship placed = new Ship(positions);
      //Puts ship positions in player specific position map
      for (String pos : positions) {
        shipPositions.get(id).put(pos, placed);
      }
      return placed;
    }
    return null;
  }

  /**
   * Creates a BoardSpace object.
   * @param id of Player
   * @param row on game board
   * @param column on game board
   * @return new BoardSpace
   */
  public BoardSpace createBoardSpace(int id, int row, int column) {
    String pos = Player.getColumnList()[column] + (row + 1);
    //Checks if BoardSpace has a Ship present, sets ShotStatus accordingly
    if (shipPositions.get(id).containsKey(pos)) {
      BoardSpace space = new BoardSpace("B");
      space.setFired(new FiredHit(space, shipPositions.get(id).get(pos)));
      return space;
      //If no ship present, miss ShotStatus set
    } else {
      BoardSpace space = new BoardSpace("-");
      space.setFired(new FiredMiss(space));
      return space;
    }
  }

  /**
   * Checks if given Ship position components are viable.
   * @param id of Player
   * @param line of position components to be checked for adjacency.
   * @return true if position components valid, false otherwise
   */
  private boolean checkPositions(int id, int[] line) {
    int[] lineFix = Arrays.stream(line).distinct().sorted().toArray();
    //Checks if line is consecutive
    if (lineFix.length == line.length
        && (lineFix[lineFix.length - 1] - lineFix[0]) == (lineFix.length - 1)) {
      return true;
    }
    //Checks if line is repeated(one position component needs to be)
    if (!repeat[id] && lineFix.length == 1) {
      repeat[id] = true;
      return true;
    }
    return false;
  }

}
