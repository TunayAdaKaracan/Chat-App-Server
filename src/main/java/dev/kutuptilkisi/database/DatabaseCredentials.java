package dev.kutuptilkisi.database;

public class DatabaseCredentials {
    private final String HOST;
    private final int PORT;
    private final String DATABASE;
    private final String USERNAME;
    private final String PASSWORD;

    public DatabaseCredentials(String host, int port, String database, String username, String password){
        this.HOST = host;
        this.PORT = port;
        this.DATABASE = database;
        this.USERNAME = username;
        this.PASSWORD = password;
    }

    public String getHost() {
        return HOST;
    }

    public int getPort() {
        return PORT;
    }

    public String getDatabase() {
        return DATABASE;
    }

    public String getUsername() {
        return USERNAME;
    }

    public String getPassword() {
        return PASSWORD;
    }

    public String getConnectionString() {
        return "jdbc:mysql://"+getHost()+":"+getPort()+"/"+getDatabase()+"?useSSL=false";
    }
}
