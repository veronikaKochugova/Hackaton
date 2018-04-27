import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ConnectionWorker implements Runnable {

  private Socket clientSocket = null;
  private InputStream inputStream = null;

  public ConnectionWorker(Socket socket) {
    clientSocket = socket;
  }

  @Override
  public void run() {
    try {
      inputStream = clientSocket.getInputStream();
    } catch (IOException e) {
      e.printStackTrace();
    }

    byte[] buffer = new byte[1024 * 4];

    while(true) {
      try {
        int count = inputStream.read(buffer, 0, buffer.length);
        if (count > 0) {
          System.out.println(new String(buffer, 0, count));
        } else {
          System.out.println("something wrong, close socket");
          clientSocket.close();
          break;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
