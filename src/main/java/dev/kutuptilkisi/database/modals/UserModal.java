package dev.kutuptilkisi.database.modals;

import java.sql.Timestamp;

public class UserModal {
    private final int ID;
    private String username;
    private String email;
    private String password;

    private Timestamp timestamp;

    public UserModal(){
        this(-1);
    }

    public UserModal(int id){
        this.ID = id;
    }

    public int getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
