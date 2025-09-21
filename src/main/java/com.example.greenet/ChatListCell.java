package com.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import java.io.IOException;

public class ChatListCell extends ListCell<ChatSummary> {
    private Node root;
    private ChatListCellController controller;

    public ChatListCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatListCell.fxml"));
            root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(ChatSummary item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            controller.setData(item);
            setGraphic(root);
        }
    }
}
