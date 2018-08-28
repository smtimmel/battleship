package battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Presents interface to user and communicates with server to get data.
 * @author Scott Timmel
 */
public class Client extends Communicator {

  private static Client user = new Client();
  private static final int POSITION_LENGTH = 2;
  private BufferedReader br;

  private Client() { }

  /**
   * Retrieves this Client object.
   * @return user
   */
  public static Client getClient() {
    return user;
  }

  /**
   * Communicates with user to provide setup parameters to server.
   */
  protected void setup() throws Exception {
    System.out.println("Player found, starting game setup.");
    System.out.println("Here is the gameboard:");
    System.out.println((String)receive());
    //Map provides number and ships and ship sizes to user to allow for setup
    @SuppressWarnings("unchecked")
    HashMap<Integer, Integer> gameBoats = (HashMap<Integer, Integer>)receive();
    for (int size : gameBoats.keySet()) {
      System.out.println("There are " + gameBoats.get(size) + " boats of size " + size + ".");
    }
    System.out.println("Enter all positions for the boats in the format: 'A1,A2,A3'.");
    for (int size : gameBoats.keySet()) {
      for (int i = 0; i < gameBoats.get(size); i++) {
        //Positions of each ship chosen by user
        input(("Enter a valid untaken position for a boat "
            + (i + 1) + " of size " + size + ": "), size);
      }
    }
    System.out.println("Setup completed, awaiting start of match...");
    //Completion true, sent to server
    send(true);
  }

  /**
   * Shows board to user and send shot selection to server.
   */
  protected void takeTurn() throws Exception {
    //If end confirmed the other player must have won last turn
    if (confirmEnd()) {
      System.out.println("You lost!");
    } else {
      System.out.println("It is your turn!");
      //Prints current board to user
      System.out.println((String)receive());
      input("Enter a valid space on the opponents board to shoot: ", 1);
      //Prints effect of shot to user
      System.out.println((String)receive());
      if (confirmEnd()) {
        System.out.println("You won!");
      } else {
        System.out.println("It is your opponent's turn.");
      }
    }
  }

  /**
   * Input taken from user and validated before being sent out to server.
   * @param mes prompt given to user before input taken
   * @param size number of board spaces being input by user
   * @throws IOException due to connection errors
   * @throws ClassNotFoundException due to connection errors
   */
  private void input(String mes, int size) throws IOException, ClassNotFoundException {
    String input;
    do {
      System.out.print(mes);
      input = br.readLine();
      //Continues taking inputs until valid input found
    } while (!validate(input, size));
  }

  /**
   * Validates input given by user and sends it to the server.
   * @param input given by user
   * @param size number of board spaces provided
   * @return true is input valid
   * @throws IOException due to connection errors
   * @throws ClassNotFoundException due to connection errors
   */
  private boolean validate(String input, int size) throws IOException, ClassNotFoundException {
    String[] coords = input.split(",");
    //Checks first if appropriate positions given
    if (coords.length == size && !Arrays.stream(coords)
        .anyMatch(p -> p.length() != POSITION_LENGTH)) {
      //Lets server finish validation step
      send(coords);
      return (boolean)receive();
    } else {
      return false;
    }
  }

  /**
   * Checks if game has ended.
   * @return true if game over, false otherwise
   * @throws Exception due to connection errors
   */
  private boolean confirmEnd() throws Exception {
    if ((boolean)receive()) {
      //performs cleanup before termination
      endGame();
      br.close();
      return true;
    } else {
      return false;
    }
  }

  /**
   * Sets the reader for the program.
   * @param br input reader
   */
  public void setBufferedReader(BufferedReader br) {
    this.br = br;
  }
}
