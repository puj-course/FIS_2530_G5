package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ChatsListController {

    @FXML private ListView<ChatSummary> chatListView;

    @FXML
    public void initialize() {
        chatListView.setCellFactory(list -> new ChatListCell());

        chatListView.getItems().addAll(
                new ChatSummary("Mateo", "Nos vemos a las 3", "07:30"),
                new ChatSummary("Rey", "Perfecto!", "09:12"),
                new ChatSummary("Brandon", "Hola :)", "10:30"),
                new ChatSummary("Andrés", "Adios", "08:15")

        );

        chatListView.setOnMouseClicked(evt -> {
            if (evt.getClickCount() == 1) {
                ChatSummary selected = chatListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    try {
                        MainApp.setRoot("ChatView.fxml");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
