package com.unipi.kottarido.mywallchat.mywallchat;

public class Message {

    private String Username;
    private String Email;
    private String Message;

    //prepei na exw dilomeno enan no-argument constructor gia na xristimopoiithei
    //otan tou anathetoume to value enos snapshot obj tis firebase
    public Message(){

    }

    public Message(String username, String email, String message) {
        Username = username;
        Email = email;
        Message = message;

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
