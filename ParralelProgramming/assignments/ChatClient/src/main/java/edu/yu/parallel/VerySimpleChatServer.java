package edu.yu.parallel;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.*;

public class VerySimpleChatServer {

    static List<MultiServerThread> list = new ArrayList<MultiServerThread>();

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java VerySimpleChatServer <port number>");
            System.exit(1);
        }

        System.out.println("Starting server ...");

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (listening) {
                Socket socket = serverSocket.accept();
                int port  = socket.getPort();
                System.out.println("client socket=Socket[addr=" + socket.getInetAddress() + ",port=" + socket.getPort() + ",localport=" + socket.getLocalPort() + "]");
                System.out.println("Connected to client {socket port=" + socket.getPort() + "}");
                MultiServerThread thread = new MultiServerThread(socket, port);
                list.add(thread);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}

class MultiServerThread extends Thread {
    public Socket socket = null;
    public int port;

    public MultiServerThread(Socket socket, int port) {
        super("MultiServerThread");
        this.socket = socket;
        this.port = port;
    }

    @Override
    public void run() {

        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Client {socket port="+ this.port + "} sent> " + inputLine);
                for (MultiServerThread th : VerySimpleChatServer.list) {
                    if (th.port != this.port) {
                        PrintWriter out1 = new PrintWriter(th.socket.getOutputStream(), true);
                        String outputLine = "{socket port="+ this.port + "} sent> " + inputLine;
                            out1.println(outputLine);
                    }
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
