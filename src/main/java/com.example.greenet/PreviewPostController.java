package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PreviewPostController {

    @FXML
    private Button backButton;

    @FXML
    private void onBack(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private Label dateLabel;

    @FXML
    public void initialize() {
        LocalDate hoy = LocalDate.now();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        dateLabel.setText("Fecha: " + hoy.format(formato));
    }
}
