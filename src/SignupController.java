package com.example.greenet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Objects;

public class SignupController {

    @FXML private TextField nombresField;
    @FXML private TextField apellidosField;
    @FXML private TextField correoField;
    @FXML private TextField usuarioField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleSignup() {
        String nombres = nombresField.getText().trim();
        String apellidos = apellidosField.getText().trim();
        String correo = correoField.getText().trim();
        String usuario = usuarioField.getText().trim();
        String password = passwordField.getText().trim();

        if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() ||
                usuario.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Por favor complete todos los campos");
            return;
        }

        if (!isValidEmail(correo)) {
            showAlert("Error", "Por favor ingrese un correo electrónico válido");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall("{call registrar_usuario(?, ?, ?, ?, ?)}")) {

            stmt.setString(1, nombres);
            stmt.setString(2, apellidos);
            stmt.setString(3, correo);
            stmt.setString(4, usuario);
            stmt.setString(5, password);
            stmt.execute();

            showAlert("Éxito", "Usuario registrado correctamente");
            handleGoToLogin();

        } catch (Exception e) {
            showAlert("Error", "Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoToLogin() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LOGIN.fxml")));
            Stage stage = (Stage) nombresField.getScene().getWindow();
            stage.setScene(new Scene(root, 354, 600));
            stage.setTitle("GREENET - Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}