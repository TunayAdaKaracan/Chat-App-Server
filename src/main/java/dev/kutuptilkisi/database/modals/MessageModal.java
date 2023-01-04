package dev.kutuptilkisi.database.modals;

import java.sql.Timestamp;

public class MessageModal {
    private int id;
    private String username;
    private String content;
    private Timestamp timestamp;

    public MessageModal(){
        this(-1);
    }

    public MessageModal(int id){
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
