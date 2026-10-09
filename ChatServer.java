import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer {
    static Set<PrintWriter> clients = Collections.synchronizedSet(new HashSet<>());

    static void broadcast(String msg) {
        synchronized (clients) {
            for (PrintWriter pw : clients) pw.println(msg);
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 5555;
        System.out.println("Chat Server started on port " + port);
        ServerSocket server = new ServerSocket(port);
        while (true) {
            Socket sock = server.accept();
            new Thread(() -> {
                String username = "Unknown";
                PrintWriter out = null;
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    out = new PrintWriter(sock.getOutputStream(), true);
                    clients.add(out);
                    username = in.readLine();
                    broadcast("🟢 " + username + " joined the chat!");
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        if (msg.equals("/quit")) break;
                        broadcast("[" + username + "]: " + msg);
                    }
                } catch (IOException e) {}
                finally {
                    if (out != null) clients.remove(out);
                    broadcast("🔴 " + username + " left the chat.");
                    try { sock.close(); } catch (IOException e) {}
                }
            }).start();
        }
    }
}
