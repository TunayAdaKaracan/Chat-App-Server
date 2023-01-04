package dev.kutuptilkisi.packets.impl.incoming;

import dev.kutuptilkisi.packets.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AuthPacket implements Packet {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void write(DataOutputStream stream) {}

    @Override
    public void read(DataInputStream stream) {
        try {
            username = stream.readUTF();
            password = stream.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
