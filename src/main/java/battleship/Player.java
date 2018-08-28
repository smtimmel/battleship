package battleship;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Controls server side communication and actions for a player.
 * @author Scott Timmel
 */
public class Player extends Communicator {

  private static final int SLEEP_TIME = 10;
  private int id;
  private BoardSpace[][] board;
  private HashMap<Integer, Integer> shipMap;
  private HashSet<Ship> remainingShips = new HashSet<>();
  private DisplayBoard display = new DisplayEmptyBoard();
  private ReentrantLock lock = new ReentrantLock();
  private static String[] columnList = {"A","B","C","D","E","F","G","H","I"};

  /**
   * Constructor of Player objects.
   * @param id of Player
   * @param size of board
   * @param shipMap map of Player's Ships
   */
  public Player(int id, int size, HashMap<Integer, Integer> shipMap) {
    this.id = id;
    board = new BoardSpace[size][size];
    this.shipMap = shipMap;
  }

  /**
   * Gets the list of acceptable column labels, current max is 9.
   * @return columnList
   */
  public static String[] getColumnList() {
    return columnList;
  }

  /**
   * Controls server side game setup.
   */
  protected void setup() throws Exception {
    //Ensure game does not begin until each Player setup finished
    lock.lock();
    //Finds player
    setSocket(Server.getServer().getServerSocket().accept());
    Server.getServer().startGame();
    //Sends board and ship counts to user for setup inputs
    send(getBoard());
    send(shipMap);
    //Gets user Ship inputs and attempts to create Ships
    Object obj = receive();
    while (obj instanceof String[]) {
      Ship current = GameFactory.getFactory().createShip(id, (String[])obj);
      if (current == null) {
        send(false);
      } else {
        current.setOwner(this);
        remainingShips.add(current);
        send(true);
      }
      obj = receive();
    }
    //Gets the spaces of the player's board
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board.length; j++) {
        registerBoardSpace(i, j, GameFactory.getFactory().createBoardSpace(id, i, j));
      }
    }
    lock.unlock();
    //Waits until all players done with setup
    while (!lock.isLocked()) {
      Thread.sleep(SLEEP_TIME);
    }
  }

  /**
   * Controls server side actions of the turn taken by the player.
   */
  protected void takeTurn() throws Exception {
    //Ensures setup or opponent turn finished
    lock.lock();
    //If the game is over, the Client is notified and Server told to shut down
    if (isOver()) {
      send(true);
      Server.getServer().endServer();
      lock.unlock();
    } else {
      send(false);
      //Sends boards to Client
      StringBuilder boards = new StringBuilder("This is the opponents board:\n");
      boards.append(Server.getServer().getOpponent(this).getBoard());
      boards.append("This is your board:\n");
      boards.append(getBoard());
      send(boards.toString());
      //Takes shot fired by user and validates it
      String position = ((String[])receive())[0];
      int[] coords = convertCoord(position, board.length);
      while (coords == null) {
        send(false);
        position = ((String[])receive())[0];
        coords = convertCoord(position, board.length);
      }
      send(true);
      //Updates opponents board and checks if game over
      send(Server.getServer().getOpponent(this).notifyBoardSpace(position, coords[0], coords[1]));
      send(isOver());
      lock.unlock();
      while (!lock.isLocked()) {
        Thread.sleep(SLEEP_TIME);
      }
    }
  }

  /**
   * Registers a BoardSpace to the board to allow for notifications.
   * @param i row of BoardSpace
   * @param j column of BoardSpace
   * @param addition BoardSpace to be added
   */
  private void registerBoardSpace(int i, int j, BoardSpace addition) {
    board[i][j] = addition;
  }

  /**
   * Notifies the BoardSpace of a shot fired at the given position.
   * @param pos position given as String
   * @param i row of position
   * @param j column of position
   * @return the result String of the shot
   * @throws IOException not applicable
   */
  public String notifyBoardSpace(String pos, int i, int j) throws IOException {
    String update = board[i][j].update(pos);
    Server.getServer().getOpponent(this).getDisplay().setUpdate(update);
    return update;
  }

  /**
   * Gets a printable player board for the user.
   * @return the board as a String
   */
  public String getBoard() {
    return display.getBoard(board);
  }

  /**
   * Gets the lock owned by this Player.
   * @return lock
   */
  public ReentrantLock getlock() {
    return lock;
  }

  /**
   * Sets the display behavior to be used.
   * @param display behavior
   */
  public void setDisplay(DisplayBoard display) {
    this.display = display;
  }

  /**
   * Gets the DisplayPlayerBoard behavior.
   * @return DisplayPlayerBoard object
   */
  public DisplayPlayerBoard getDisplay() {
    return (DisplayPlayerBoard)display;
  }

  /**
   * Attempts to convert the String position given into row and column coordinates.
   * @param coordStr position
   * @param size of game board
   * @return coordinate integers if valid, null otherwise
   */
  public static int[] convertCoord(String coordStr, int size) {
    String[] coord = coordStr.split("");
    int[] result = new int[2];
    try {
      result[0] = Integer.parseInt(coord[1]) - 1;
      //Checks if row coordinate within board
      if (result[0] < size && result[0] >= 0) {
        for (int i = 0; i < size; i++) {
          //Checks if column coordinate label exists on board
          if (coord[0].equals(columnList[i])) {
            result[1] = i;
            return result;
          }
        }
      }
      return null;
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * Removes sunken Ship from remaining ship map and sees if any ships remain.
   * @param sunk Ship which has sunk
   * @throws IOException not applicable
   */
  public void sinkShip(Ship sunk) throws IOException {
    if (remainingShips.remove(sunk)) {
      if (remainingShips.isEmpty()) {
        //If no more Ships remain ending process initiated
        endGame();
        Server.getServer().getOpponent(this).endGame();
      }
    }
  }

}
