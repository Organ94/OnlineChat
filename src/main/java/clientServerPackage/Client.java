package clientServerPackage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client implements Runnable {

    private static InetSocketAddress socketAddress;
    private static SocketChannel clientSocket;
    private static String nameClient;

    public Client(String name, String host, int port) {
        Client.nameClient = name;
        try {
            socketAddress = new InetSocketAddress(host, port);
            clientSocket = SocketChannel.open();
            clientSocket.connect(socketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

            String msg;
            while (true) {
                System.out.print("Введите сообщение или \"/exit\" для выхода\n>> ");
                msg = scanner.nextLine();

                if (msg.equalsIgnoreCase("/exit")) {
                    clientSocket.close();
                    break;
                }

                clientSocket.write(ByteBuffer.wrap(
                        (Client.nameClient + ": " + msg).getBytes(StandardCharsets.UTF_8)));
                Thread.sleep(100);

                int byteCount = clientSocket.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, byteCount, StandardCharsets.UTF_8));
                inputBuffer.clear();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
