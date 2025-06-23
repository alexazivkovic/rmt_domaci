package main;

import util.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket sc = new ServerSocket(port);
            System.out.println("Server Started Successfully on Port " + port);
            while (true) {
                Socket s = sc.accept();
                System.out.println("Client Connected: " + s.getInetAddress());
                Thread t = new Thread(new ClientHandler(s));
                t.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
