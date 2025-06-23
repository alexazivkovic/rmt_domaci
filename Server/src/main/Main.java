package main;

public class Main {
    public static void main(String[] args) {
        Thread thread = new Thread(new Server(9000));
        thread.start();
    }
}