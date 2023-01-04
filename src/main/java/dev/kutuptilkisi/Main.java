package dev.kutuptilkisi;

import dev.kutuptilkisi.database.Database;
import dev.kutuptilkisi.database.DatabaseCredentials;
import dev.kutuptilkisi.events.EventRegistry;
import dev.kutuptilkisi.events.LoginEvents;
import dev.kutuptilkisi.events.MessageEvents;
import dev.kutuptilkisi.server.NetworkServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        // Args
        // 0 - PORT
        // 1 - MYSQL Host
        // 2 - MYSQL PORT
        // 3 - MYSQL Database
        // 4 - MYSQL User
        // 5 - MYSQL Password
        Database database = new Database(new DatabaseCredentials(args[1], Integer.parseInt(args[2]), args[3], args[4], args[5]));

        EventRegistry.register(new LoginEvents(database));
        EventRegistry.register(new MessageEvents(database));
        new NetworkServer(Integer.parseInt(args[0]), database).run();
    }
}