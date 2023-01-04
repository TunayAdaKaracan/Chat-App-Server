package dev.kutuptilkisi.events;

import dev.kutuptilkisi.database.Database;
import dev.kutuptilkisi.database.modals.MessageModal;
import dev.kutuptilkisi.database.modals.UserModal;
import dev.kutuptilkisi.packets.impl.common.MessagePacket;
import dev.kutuptilkisi.packets.impl.incoming.RequestHistoryPacket;
import dev.kutuptilkisi.packets.impl.outgoing.UnreadMessagePacket;
import dev.kutuptilkisi.server.NetworkServer;

import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class MessageEvents {

    private final Database database;

    public MessageEvents(Database database){
        this.database = database;
    }

    public void messageEvent(Socket socket, MessagePacket packet){
        MessageModal modal = new MessageModal();
        modal.setUsername(packet.getUsername());
        modal.setContent(packet.getMessageContent());
        modal.setTimestamp(new Timestamp(new Date().getTime()));
        database.saveMessage(modal);

        packet.send();
    }

    public void requestMessageHistory(Socket socket, RequestHistoryPacket packet){
        UserModal userModal = database.getUser(NetworkServer.getName(socket));
        List<MessageModal> messages = database.getMessagesBefore(userModal.getTimestamp());
        for(MessageModal messageModal : messages){
            new MessagePacket()
                    .setUsername(messageModal.getUsername())
                    .setMessageContent(messageModal.getContent())
                    .send(socket);
        }
        messages = database.getMessagesAfter(userModal.getTimestamp());
        if(messages.size() != 0){
            new UnreadMessagePacket().send(socket);
            for(MessageModal messageModal : messages){
                new MessagePacket()
                        .setUsername(messageModal.getUsername())
                        .setMessageContent(messageModal.getContent())
                        .send(socket);
            }
        }
    }
}
