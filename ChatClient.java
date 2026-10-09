import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = sc.nextLine().trim();
        Socket sock = new Socket("localhost", 5555);
        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

        out.println(username);
        System.out.println("Connected! Type messages or /quit to exit.
");

        // Thread to receive messages
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) System.out.println(msg);
            } catch (IOException e) {}
        }).start();

        // Send messages
        while (sc.hasNextLine()) {
            String msg = sc.nextLine();
            out.println(msg);
            if (msg.equals("/quit")) break;
        }
        sock.close();
    }
}
