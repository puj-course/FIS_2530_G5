package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class UploadMaterialController {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private ImageView previewImage;

    @FXML
    public void initialize() {
        categoryCombo.getItems().addAll("Plastico", "Vidrio", "Ropa", "Electronicos", "Otros");
    }

    @FXML
    private void onUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            previewImage.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void onSubmit() {
        String titulo = titleField.getText();
        String descripcion = descriptionArea.getText();
        String categoria = categoryCombo.getValue();

        if (titulo.isEmpty() || descripcion.isEmpty() || categoria == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor completa todos los campos.");
            alert.show();
            return;
        }

        Alert success = new Alert(Alert.AlertType.INFORMATION,
                "Material publicado:\n" + titulo + " (" + categoria + ")");
        success.show();
    }

    @FXML
    private void onNewMaterial() {
        titleField.clear();
        descriptionArea.clear();
        categoryCombo.getSelectionModel().clearSelection();
        previewImage.setImage(null);
    }
}
