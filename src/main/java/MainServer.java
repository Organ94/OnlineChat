import clientServerPackage.Server;

public class MainServer {

    public static void main(String[] args) {
        Server server = new Server();
        new Thread(server).start();
    }
}
