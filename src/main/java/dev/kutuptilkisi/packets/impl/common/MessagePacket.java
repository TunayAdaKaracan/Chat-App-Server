package dev.kutuptilkisi.packets.impl.common;

import dev.kutuptilkisi.packets.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// ID 0
public class MessagePacket implements Packet {
    private String username;
    private String messageContent;

    public MessagePacket setUsername(String name){
        this.username = name;
        return this;
    }

    public MessagePacket setMessageContent(String message){
        this.messageContent = message;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public String getMessageContent() {
        return messageContent;
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(0);
            stream.writeUTF(username);
            stream.writeUTF(messageContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.username = stream.readUTF();
            this.messageContent = stream.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
