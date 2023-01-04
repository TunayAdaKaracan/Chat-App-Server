package dev.kutuptilkisi.packets.impl.outgoing;

import dev.kutuptilkisi.packets.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UnreadMessagePacket implements Packet {
    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(5);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void read(DataInputStream stream) {

    }
}
