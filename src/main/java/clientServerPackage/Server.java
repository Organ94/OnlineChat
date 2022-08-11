package clientServerPackage;

import logger.ListStorageAdapter;
import logger.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    static final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private static List<String> aListWithMessageFromUsers = new ArrayList<>();
    private static Logger logger = new Logger(new ListStorageAdapter(aListWithMessageFromUsers));
    static List<SocketChannel> clients = new ArrayList<>();
    private static final String HOST = "localhost";
    private static final int PORT = 8080;


    @Override
    public void run() {
        try {
            final ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(HOST, PORT));
            System.out.println("Сервер запущен!");

            while (true) {
                SocketChannel clientSocket = server.accept();
                executorService.execute(() -> {
                    try (SocketChannel client = clientSocket) {
                        addingClient(client);
                        serve(client);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serve(SocketChannel client) throws IOException, InterruptedException {
        final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

        while (client.isConnected()) {
            int byteCount = client.read(inputBuffer);

            if (byteCount == -1) {
                break;
            }

            final String msg = new String(inputBuffer.array(), 0, byteCount, StandardCharsets.UTF_8);

            if (msg.equalsIgnoreCase("/exit")) {
                removeClient(client);
                client.close();
                break;
            }

            for (SocketChannel channelClient : clients) {
                channelClient.write(ByteBuffer.wrap((logger.log(msg) + "\n").getBytes(StandardCharsets.UTF_8)));
            }
            inputBuffer.clear();
            Thread.sleep(100);
            writeMessageFromClientToFile(msg, logger);
        }
    }


    private void addingClient(SocketChannel client) {
        clients.add(client);
    }

    private void removeClient(SocketChannel client) {
        clients.remove(client);
    }

    private void writeMessageFromClientToFile(String msg, Logger logger) {
        try (FileWriter fw = new FileWriter("messageFromUsers.txt", true)) {
            fw.write(logger.log(msg));
            fw.append('\n');
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
