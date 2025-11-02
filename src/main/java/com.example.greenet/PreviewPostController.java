package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import services.FachadaImagen;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PreviewPostController {

    @FXML
    private Button backButton;

    @FXML
    private Label dateLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateLabel.setText("Fecha: " + hoy.format(formato));
    }

    @FXML
    private void onBack(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    public void cargarPublicacion(String titulo, String autor, String descripcion, String imagenBase64) {
        titleLabel.setText(titulo);
        authorLabel.setText("Publicado por: " + autor);
        descriptionLabel.setText(descripcion);
        Image img = FachadaImagen.convertirBytesAImage(FachadaImagen.decodificarStringAImagen(imagenBase64));
        imageView.setImage(img);
    }
}
