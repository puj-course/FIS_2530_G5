package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ChatListCellController {
    @FXML private ImageView avatarImage;
    @FXML private Label nameLabel;
    @FXML private Label lastMessageLabel;
    @FXML private Label timeLabel;

    public void setData(ChatSummary chat) {
        nameLabel.setText(chat.getName());
        lastMessageLabel.setText(chat.getLastMessage());
        timeLabel.setText(chat.getTime());
    }
}
