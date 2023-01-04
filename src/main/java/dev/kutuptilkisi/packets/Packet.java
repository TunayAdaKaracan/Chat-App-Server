package dev.kutuptilkisi.packets;


import dev.kutuptilkisi.server.NetworkServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public interface Packet {

    void write(DataOutputStream stream);

    void read(DataInputStream stream);

    default void send(){
        NetworkServer.send(this);
    }

    default void send(Socket socket){
        NetworkServer.send(socket, this);
    }

}
