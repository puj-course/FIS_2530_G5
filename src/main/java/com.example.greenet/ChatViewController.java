package com.example;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class ChatViewController {

    @FXML private VBox messagesVBox;
    @FXML private TextField messageField;
    @FXML private ScrollPane chatScroll;
    @FXML private Button backButton;
    @FXML private Label chatName;

    @FXML
    public void initialize() {
        addMessage("Hola, ¿cómo vas?", false);
    }

    @FXML
    private void onSend() {
        String text = messageField.getText().trim();
        if (!text.isEmpty()) {
            addMessage(text, true);
            messageField.clear();
        }
    }

    private void addMessage(String text, boolean sent) {
        Label lbl = new Label(text);
        lbl.setWrapText(true);
        lbl.getStyleClass().add("message-bubble");
        lbl.getStyleClass().add(sent ? "message-sent" : "message-received");
        lbl.maxWidthProperty().bind(messagesVBox.widthProperty().multiply(0.7));

        HBox container = new HBox(lbl);
        container.setAlignment(sent ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        messagesVBox.getChildren().add(container);

        Platform.runLater(() -> chatScroll.setVvalue(1.0));
    }

    @FXML
    private void initial() {
        backButton.setOnAction(e -> goBackToChats());
    }

    private void goBackToChats() {
        try {
            MainApp.setRoot("ChatsList.fxml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
