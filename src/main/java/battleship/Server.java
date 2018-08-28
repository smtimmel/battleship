package battleship;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * Controls the server holding the game data and providing the data to Clients.
 * @author Scott Timmel
 */
public class Server {

  private static final int NUM_PLAYERS = 2;
  private static final int SLEEP_TIME = 10;
  private static Server game = null;
  private ServerSocket serve;
  private Player[] players = new Player[NUM_PLAYERS];
  private boolean end = false;

  private Server() throws IOException {
    serve = new ServerSocket(0);
  }

  /**
   * Gets the Server object.
   * @return Server
   * @throws IOException if Server can not be created
   */
  public static Server getServer() throws IOException {
    //Creates Server object if called for first time
    if (game == null) {
      game = new Server();
    }
    return game;
  }

  /**
   * Attempts to find an opponent for the host.
   * @throws IOException if server can not provide IP address.
   * @throws InterruptedException if thread interrupted
   */
  public void findOpponent() throws IOException, InterruptedException {
    //Provides connection details and starts thread for Player one
    System.out.println("Began hosting, your IP: " + InetAddress.getLocalHost()
        .getHostAddress() + ", your port: " + serve.getLocalPort());
    System.out.println("Looking for opponent.");
    addPlayer(0);
    //Attempts to acquire locks of Players after setup
    while (players[1] == null || !players[1].getlock().isLocked()) {
      Thread.sleep(10);
    }
    for (Player player : players) {
      player.getlock().lock();
    }
    controlLocks();
  }

  /**
   * Player created and a new thread started.
   * @param i Player id of new Player
   */
  private void addPlayer(int i) {
    players[i] = GameFactory.getFactory().createPlayer();
    new Thread(players[i]).start();
  }

  /**
   * Gets the ServerSocket.
   * @return ServerSocket
   */
  public ServerSocket getServerSocket() {
    return serve;
  }

  /**
   * Creates the second player and starts the associated Client thread. 
   * @throws IOException if socket setting causes error
   */
  public void startGame() throws IOException {
    //Second Player created if it does not already exist
    if (players[1] == null) {
      addPlayer(1);
      //Client thread started
      Client.getClient().setSocket(new Socket(InetAddress.getLocalHost(), serve.getLocalPort()));
      new Thread(Client.getClient()).start();
    }
  }

  /**
   * Controls the Player locks and makes sure Player turns do not overlap.
   * @throws InterruptedException if thread interrupted
   */
  private void controlLocks() throws InterruptedException {
    int i = 0;
    DisplayBoard[] displays = {new DisplayPlayerBoard(), new DisplayOpponentBoard()};
    while (!end) {
      for (int j = 0; j < displays.length; j++) {
        //Sets the display behavior for the active and opposing player's boards
        players[(i + j) % NUM_PLAYERS].setDisplay(displays[j]);
      }
      //Releases lock of next Player to allow for turn to be taken
      players[i % NUM_PLAYERS].getlock().unlock();
      Thread.sleep(SLEEP_TIME);
      players[i++ % NUM_PLAYERS].getlock().lock();
    }
  }

  /**
   * Gets the opposing Player.
   * @param user active Player
   * @return opposing Player
   */
  public Player getOpponent(Player user) {
    return Arrays.stream(players).filter(p -> !p.equals(user)).findAny().get();
  }

  /**
   * Begins process of ending server.
   */
  public void endServer() {
    end = true;
  }

}
