package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsProfileController {

    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtDireccion;

    @FXML private Button btnTelefono;
    @FXML private Button btnCorreo;
    @FXML private Button btnDireccion;
    @FXML private Button btnCerrarSesion;

    @FXML
    public void initialize() {
        btnTelefono.setOnAction(e -> {
            String telefono = txtTelefono.getText();
            if (telefono.matches("\\d{10}")) {
                mostrarAlerta("Exito", "Cambios guardados");
            } else {
                mostrarAlerta("Error", "Numero invalido (deben ser 10 digitos)");
            }
        });

        btnCorreo.setOnAction(e -> {
            String correo = txtCorreo.getText();
            if (correo.contains("@")) {
                mostrarAlerta("Exito", "Cambios guardados");
            } else {
                mostrarAlerta("Error", "Correo invalido (falta @)");
            }
        });

        btnDireccion.setOnAction(e -> {
            if (!txtDireccion.getText().isEmpty()) {
                mostrarAlerta("Exito", "Cambios guardados");
            } else {
                mostrarAlerta("Error", "La direccion no puede estar vacia");
            }
        });

        btnCerrarSesion.setOnAction(e -> {
            mostrarAlerta("Sesion cerrada", "Gracias por usar nuestros servicios");
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            stage.close();
        });
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
