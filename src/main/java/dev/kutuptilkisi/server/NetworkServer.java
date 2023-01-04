package dev.kutuptilkisi.server;

import dev.kutuptilkisi.database.Database;
import dev.kutuptilkisi.database.modals.UserModal;
import dev.kutuptilkisi.events.EventRegistry;
import dev.kutuptilkisi.packets.Packet;
import dev.kutuptilkisi.packets.PacketFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NetworkServer implements Runnable{

    private static NetworkServer INSTANCE;

    private ServerSocket serverSocket;

    private boolean shutdown;

    private final List<Socket> connections;
    private final PacketFactory packetFactory;
    private final PacketSender packetSender;

    private final Database database;

    private final HashMap<Socket, String> names;

    public NetworkServer(int port, Database database){
        if(INSTANCE != null){
            throw new RuntimeException("You can't create 2 NetworkServer instance");
        }


        INSTANCE = this;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is now listening...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.database = database;
        shutdown = false;
        connections = new ArrayList<>();
        packetFactory = new PacketFactory();
        packetSender = new PacketSender();
        names = new HashMap<>();
        Thread thread = new Thread(packetSender);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        while(!shutdown){
            try {
                Socket client = serverSocket.accept();
                System.out.println("New Client");
                connections.add(client);
                Thread thread = new Thread(new ClientInputReader(client));
                thread.setDaemon(true);
                thread.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    class ClientInputReader implements Runnable {
        private final Socket socket;
        private DataInputStream stream;

        public ClientInputReader(Socket client){
            socket = client;
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
                    Packet packet = packetFactory.fromId(stream.readInt());
                    packet.read(stream);
                    EventRegistry.fireEvent(socket, packet);
                } catch (IOException e) {
                    System.out.println("Client disconnected");
                    String socketName = names.get(socket);
                    if(socketName != null){
                        UserModal modal = database.getUser(socketName);
                        modal.setTimestamp(new Timestamp(new Date().getTime()));
                        database.updateUser(modal);
                        names.remove(socket);
                    }
                    connections.remove(socket);
                    return;
                }
            }
        }
    }

    public static void stop(){
        INSTANCE.shutdown = true;
        for(Socket socket : INSTANCE.connections){
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void send(Packet packet){
        for(Socket socket : INSTANCE.connections){
            INSTANCE.packetSender.addPacket(socket, packet);
        }
    }

    public static void send(Socket socket, Packet packet){
        INSTANCE.packetSender.addPacket(socket, packet);
    }

    public static void saveName(Socket socket, String name){
        INSTANCE.names.put(socket, name);
    }

    public static String getName(Socket socket){
        return INSTANCE.names.get(socket);
    }
}
