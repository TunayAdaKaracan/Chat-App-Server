package dev.kutuptilkisi.packets.impl.incoming;

import dev.kutuptilkisi.packets.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegistryPacket implements Packet {

    private String username;
    private String email;
    private String password;

    @Override
    public void write(DataOutputStream stream) {}

    @Override
    public void read(DataInputStream stream) {
        try {
            username = stream.readUTF();
            email = stream.readUTF();
            password = stream.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
