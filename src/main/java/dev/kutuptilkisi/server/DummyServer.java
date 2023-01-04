package dev.kutuptilkisi.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DummyServer implements Runnable{

    private ServerSocket socket;

    public DummyServer(int port) throws IOException {
        socket = new ServerSocket(port);

        System.out.println("Ready to connection");
    }


    @Override
    public void run() {
        while(true){
            try {
                Socket client = socket.accept();
                System.out.println("New Client");
                new Thread(new ClientInputReader(client)).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class ClientInputReader implements Runnable {
        private DataInputStream stream;

        public ClientInputReader(Socket client){
            try {
                stream = new DataInputStream(client.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            while(true){
                try {
                    int id = stream.readInt();
                    System.out.println(id);
                    System.out.println(stream.readUTF());
                    System.out.println(stream.readUTF());
                } catch (IOException e) {
                    System.out.println("Client disconnected");
                    return;
                }
            }
        }
    }
}
