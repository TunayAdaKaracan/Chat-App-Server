package dev.kutuptilkisi.events;

import dev.kutuptilkisi.database.Database;
import dev.kutuptilkisi.database.modals.MessageModal;
import dev.kutuptilkisi.database.modals.UserModal;
import dev.kutuptilkisi.packets.impl.common.MessagePacket;
import dev.kutuptilkisi.packets.impl.incoming.AuthPacket;
import dev.kutuptilkisi.packets.impl.incoming.RegistryPacket;
import dev.kutuptilkisi.packets.impl.outgoing.ServerResponse;
import dev.kutuptilkisi.server.NetworkServer;

import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class LoginEvents {

    private Database database;

    public LoginEvents(Database database){
        this.database = database;
    }

    public void loginEvent(Socket socket, AuthPacket packet){
        UserModal userModal = database.getUser(packet.getUsername(), packet.getPassword());
        if(userModal == null){
            new ServerResponse()
                    .setCode(1)
                    .setText("Account is not valid")
                    .send(socket);
            return;
        }
        NetworkServer.saveName(socket, packet.getUsername());
        new ServerResponse()
                .setCode(0)
                .setText("OK")
                .send(socket);


    }

    public void registerEvent(Socket socket, RegistryPacket packet){
        if(packet.getUsername().equals("System")){
            new ServerResponse()
                    .setCode(1)
                    .setText("You can't use this name as username!")
                    .send(socket);
            return;
        }
        if(database.getUser(packet.getUsername()) != null){
            new ServerResponse()
                    .setCode(1)
                    .setText("There is already an account with same username!")
                    .send(socket);
            return;
        }

        UserModal modal = new UserModal();
        modal.setUsername(packet.getUsername());
        modal.setEmail(packet.getEmail());
        modal.setPassword(packet.getPassword());
        modal.setTimestamp(new Timestamp(new Date().getTime()));
        database.saveUser(modal);

        new ServerResponse()
                .setCode(0)
                .setText("OK")
                .send(socket);
    }
}
