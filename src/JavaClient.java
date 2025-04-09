import java.io.*;
import java.net.*;
import java.util.Scanner;

public class JavaClient {

    private Socket socket;
    private Scanner scanner;
    private PrintWriter out;
    private BufferedReader in;

    public JavaClient() {

    }

    public void startClient(String serverIP, int port) {
        scanner = new Scanner(System.in);
        try {
            socket = new Socket(serverIP, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                System.out.print("client >> ");
                String usr_input = scanner.nextLine();
                out.println(usr_input);
                String server_message = in.readLine();
                if (server_message.equals("SERVER CLOSE")) {
                    stopClient();
                }
                System.out.println("Server Message: " + server_message);
            }
        } catch (SocketException e) {
            scanner.close();
            System.exit(0);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void stopClient() throws IOException {
        scanner.close();
        in.close();
        out.close();
        socket.close();
        System.exit(0);
    }

    public static void main(String[] args) {
        JavaClient client = new JavaClient();
        client.startClient(args[0], Integer.parseInt(args[1]));
    }
}