
public class Main {
  public static void main(String[] args) {
    Server server = Server.getInstance();
    Thread thread = new Thread(server);
    thread.start();
  }
}
