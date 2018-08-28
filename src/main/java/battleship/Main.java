package battleship;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Class starts the program.
 * @author Scott Timmel
 */
public class Main {

  /**
   * A main method to start program. 
   * @param args not used 
   */
  public static void main(String[] args) {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Would you like to host a game? (y/n): ");
    try {
      //Checks if server is to used for hosting and calls it along with first client if so
      if (br.readLine().equals("y")) {
        Client.getClient().setBufferedReader(br);
        Server.getServer().findOpponent();
        //Otherwise user enters connection details and Client connects to server
      } else {
        System.out.print("Please enter the host IP address: ");
        String address = br.readLine();
        System.out.print("Please enter the host port: ");
        int port = Integer.parseInt(br.readLine());
        System.out.println("Attempting to connect...");
        Client.getClient().setSocket(new Socket(address, port));
        Client.getClient().setBufferedReader(br);
        new Thread(Client.getClient()).start();
      }
    } catch (Exception e) {
      System.out.println("Could not connect, make sure proper credentials provided.");
    }
  }

}
