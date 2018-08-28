package battleship;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Provides means of communication over a server-socket system.
 * @author Scott Timmel
 */
public abstract class Communicator implements Runnable {

  private Socket soc;
  private ObjectInputStream in;
  private ObjectOutputStream out;
  private boolean over = false;

  /**
   * Starts when new thread is called.
   */
  public void run() {
    try {
      //Game setup step
      setup();
      //Turns taken until game is over
      while (!isOver()) {
        takeTurn();
      }
      //cleanup performed
      close();
    } catch (Exception e) {
      System.out.println("Connection error occurred.");
      close();
    }
  }

  /**
   * Sets socket performing the communication.
   * @param soc socket being set
   * @throws IOException if stream creation fails
   */
  public void setSocket(Socket soc) throws IOException {
    this.soc = soc;
    //streams created
    out = new ObjectOutputStream(soc.getOutputStream());
    in = new ObjectInputStream(soc.getInputStream());
  }

  /**
   * Receives data from the other socket.
   * @return sent data
   * @throws ClassNotFoundException due to connection errors
   * @throws IOException due to connection errors
   */
  protected Object receive() throws ClassNotFoundException, IOException {
    //Runs until data received
    while (true) {
      try {
        return in.readObject();
      } catch (EOFException e) {
        continue;
      }
    }
  }

  /**
   * Sends data to other socket.
   * @param data to be sent
   * @throws IOException due to connection errors
   */
  protected <T> void send(T data) throws IOException {
    out.writeObject(data);
  }

  /**
   * Sets status indicating game will end soon.
   */
  public void endGame() {
    over = true;
  }

  /**
   * Checks if game is ending.
   * @return true if game ending, false otherwise
   */
  protected boolean isOver() {
    return over;
  }

  /**
   * Closes socket before terminating program.
   */
  private void close() {
    try {
      soc.close();
    } catch (IOException e) {
      System.out.println("Error terminating program.");
    }
  }

  /**
   * Performs setup step of game.
   * @throws Exception if setup error occurs
   */
  protected abstract void setup() throws Exception;

  /**
   * Performs step during a turn.
   * @throws Exception if turn error occurs
   */
  protected abstract void takeTurn() throws Exception;
}
