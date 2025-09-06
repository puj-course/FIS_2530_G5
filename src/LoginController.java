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
import java.sql.ResultSet;
import java.util.Objects;

public class LoginController {

    @FXML private TextField usuarioField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String usuario = usuarioField.getText().trim();
        String password = passwordField.getText().trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Por favor complete todos los campos");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall("{? = call iniciar_sesion(?, ?)}")) {

            stmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
            stmt.setString(2, usuario);
            stmt.setString(3, password);
            stmt.execute();

            boolean loginExitoso = stmt.getBoolean(1);

            if (loginExitoso) {
                showAlert("Éxito", "Login exitoso");
                // cargar la ventana principal
                // loadMainWindow();
            } else {
                showAlert("Error", "Usuario o contraseña incorrectos");
            }

        } catch (Exception e) {
            showAlert("Error", "Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoToSignup() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SIGNUP.fxml")));
            Stage stage = (Stage) usuarioField.getScene().getWindow();
            stage.setScene(new Scene(root, 354, 600));
            stage.setTitle("GREENET - Registro");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}