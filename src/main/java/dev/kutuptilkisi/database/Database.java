package dev.kutuptilkisi.database;


import dev.kutuptilkisi.database.modals.MessageModal;
import dev.kutuptilkisi.database.modals.UserModal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private Connection sqlConnection;

    public Database(DatabaseCredentials credentials){
        try {
            sqlConnection = DriverManager.getConnection(credentials.getConnectionString(), credentials.getUsername(), credentials.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close(){
        try {
            sqlConnection.close();
        } catch (SQLException e) {
            System.out.println("Couldn't close SQL Connection.");
        }
    }

    public UserModal getUser(String username){
        UserModal modal = null;
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("select * from users where username=?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                modal = new UserModal(resultSet.getInt("id"));
                modal.setUsername(resultSet.getString("username"));
                modal.setEmail(resultSet.getString("email"));
                modal.setPassword(resultSet.getString("password"));
                modal.setTimestamp(resultSet.getTimestamp("lastactive"));
            }
        } catch (SQLException e) {
            return null;
        }
        return modal;
    }

    public UserModal getUser(String username, String password){
        UserModal modal = null;
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("select * from users where username=? and password=?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                modal = new UserModal(resultSet.getInt("id"));
                modal.setUsername(resultSet.getString("username"));
                modal.setEmail(resultSet.getString("email"));
                modal.setPassword(resultSet.getString("password"));
                modal.setTimestamp(resultSet.getTimestamp("lastactive"));
            }
        } catch (SQLException e) {
            return null;
        }
        return modal;
    }

    public void saveUser(UserModal modal){
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("insert into users(username, email, password, lastactive) values(?, ?, ?, ?)");
            statement.setString(1, modal.getUsername());
            statement.setString(2, modal.getEmail());
            statement.setString(3, modal.getPassword());
            statement.setTimestamp(4, modal.getTimestamp());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(UserModal modal){
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("update users set lastactive=? where username=?");
            statement.setTimestamp(1, modal.getTimestamp());
            statement.setString(2, modal.getUsername());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveMessage(MessageModal modal){
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("insert into messages(username, content, timestamp) values(?, ?, ?)");
            statement.setString(1, modal.getUsername());
            statement.setString(2, modal.getContent());
            statement.setTimestamp(3, modal.getTimestamp());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MessageModal> getMessagesBefore(Timestamp timestamp){
        List<MessageModal> messages = new ArrayList<>();
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("select * from messages where timestamp<?");
            statement.setTimestamp(1, timestamp);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                MessageModal messageModal = new MessageModal();
                messageModal.setUsername(resultSet.getString("username"));
                messageModal.setContent(resultSet.getString("content"));
                messages.add(messageModal);
            }
        } catch (SQLException e) {
            return new ArrayList<>();
        }
        return messages;
    }

    public List<MessageModal> getMessagesAfter(Timestamp timestamp){
        List<MessageModal> messages = new ArrayList<>();
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("select * from messages where timestamp>?");
            statement.setTimestamp(1, timestamp);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                MessageModal messageModal = new MessageModal();
                messageModal.setUsername(resultSet.getString("username"));
                messageModal.setContent(resultSet.getString("content"));
                messages.add(messageModal);
            }
        } catch (SQLException e) {
            return new ArrayList<>();
        }
        return messages;
    }
}
