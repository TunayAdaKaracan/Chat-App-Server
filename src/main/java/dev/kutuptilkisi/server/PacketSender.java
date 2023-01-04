package dev.kutuptilkisi.server;

import dev.kutuptilkisi.packets.Packet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class PacketSender implements Runnable{
    private volatile Map<Socket, List<Packet>> packetsToSend;
    private boolean shutdown;

    public PacketSender(){
        this.packetsToSend = new LinkedHashMap<>();
        this.shutdown = false;
    }

    public void addPacket(Socket socket, Packet packet){
        if(!packetsToSend.containsKey(socket)){
            packetsToSend.put(socket, new ArrayList<>());
        }
        packetsToSend.get(socket).add(packet);
    }

    public void stop(){
        shutdown = true;
    }


    @Override
    public void run() {
        while(!shutdown){
            if(packetsToSend.size() != 0){
                try {
                    Map.Entry<Socket, List<Packet>> entry = new HashMap<>(packetsToSend).entrySet().iterator().next();
                    if(packetsToSend.get(entry.getKey()).size() == 0){
                        packetsToSend.remove(entry.getKey());
                        continue;
                    }
                    DataOutputStream dos = new DataOutputStream(entry.getKey().getOutputStream());
                    Packet packet = entry.getValue().get(0);
                    packet.write(dos);
                    dos.flush();
                    packetsToSend.get(entry.getKey()).remove(0);
                    if(packetsToSend.get(entry.getKey()).size() == 0){
                        packetsToSend.remove(entry.getKey());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
