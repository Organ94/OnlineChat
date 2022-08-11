import clientServerPackage.Client;

import java.io.*;
import java.util.Scanner;

public class MainClient {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("settings.txt")))) {
            String host = valueOfParam(reader.readLine().split(" "));
            int port = Integer.parseInt(valueOfParam(reader.readLine().split(" ")));
            System.out.print("Введите ваше имя\n>> ");

            String name = scanner.nextLine();
            Client client = new Client(name, host, port);
            new Thread(client).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String valueOfParam(String[] s) {
        return s[1];
    }
}
