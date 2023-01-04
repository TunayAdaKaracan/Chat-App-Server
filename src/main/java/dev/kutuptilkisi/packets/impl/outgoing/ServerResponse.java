package dev.kutuptilkisi.packets.impl.outgoing;

import dev.kutuptilkisi.packets.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerResponse implements Packet {

    private int code;
    private String text;

    public int getCode() {
        return code;
    }

    public ServerResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public String getText() {
        return text;
    }

    public ServerResponse setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(2);
            stream.writeInt(code);
            stream.writeUTF(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void read(DataInputStream stream) {}
}
