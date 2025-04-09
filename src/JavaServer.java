import java.net.*;
import java.util.Scanner;
import java.io.*;

public class JavaServer {

    private static Scanner scanner = new Scanner(System.in);

    public static void startServer(int serverPort) {
        try {
            ServerThread serverThread = new ServerThread(serverPort);
            serverThread.start();
            System.out.println("Server up and Running on port: " + serverPort + "\nReady to receive commands:");
            while (true) {
                System.out.print("server >> ");
                String input = scanner.nextLine();
                if (input.equals("goodbye")) {
                    System.out.println("Server Stopping, Goodbye!");
                    serverThread.stopServer();
                    break;
                }
            }
        } catch (Exception e) {

        }

        System.exit(0);
    }

    private static class ServerThread extends Thread {

        private ServerSocket serverSocket;
        private int port;

        public ServerThread(int serverPort) {
            this.port = serverPort;
        }

        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (true) {
                    new ClientHandlerThread(serverSocket.accept()).start();
                }
            } catch (Exception e) {
                
            }
        }

        public void stopServer() {
            try {
                serverSocket.close();
                interrupt();
            } catch (Exception e) {

            }
        }
    }

    private static class ClientHandlerThread extends Thread{

        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandlerThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (Exception e) {

            }
        }

        public void run() {
            try {
                while (true) {
                    String usr_input = in.readLine();
                    System.out.print("\nUser message: " + usr_input + "\nserver >> ");
                    if (usr_input.equals("goodbye")) {
                        out.println("SERVER CLOSE");
                        stopClient();
                        break;
                    }
                    out.println("ECHO -> " + usr_input);
                }
            } catch (Exception e) {

            }
        }

        public void stopClient() {
            try {
                in.close();
                out.close();
                clientSocket.close();
                interrupt();
            } catch (Exception e) {

            }
        }

    }

    public static void main(String[] args) {
        startServer(Integer.parseInt(args[0]));
    }
}

