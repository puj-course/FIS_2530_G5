package com.example;

public class ChatSummary {
    private String name;
    private String lastMessage;
    private String time;

    public ChatSummary(String name, String lastMessage, String time) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
    }

    public String getName() { return name; }
    public String getLastMessage() { return lastMessage; }
    public String getTime() { return time; }
}
