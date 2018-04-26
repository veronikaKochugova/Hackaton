import java.io.IOException;
import java.net.ServerSocket;

public class Server implements Runnable {

  private static final int SERVER_PORT = 27015;

  private static volatile Server instance = null;

  private ServerSocket serverSocket = null;

  private Server() { }

  public static Server getInstance() {
    if (instance == null) {
      synchronized (Server.class) {
        instance = new Server();
      }
    }
    return instance;
  }

  @Override
  public void run() {
    try {
      serverSocket = new ServerSocket(SERVER_PORT);
      System.out.println("Start server");

      while(true) {
        ConnectionWorker worker = null;
        try {
          worker = new ConnectionWorker(serverSocket.accept());
          System.out.println("Get client connection");

          Thread thread = new Thread(worker);
          thread.start();
        } catch (Exception e) {
          System.out.println("Connection error");
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (serverSocket != null)
        try {
          serverSocket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
  }
}
