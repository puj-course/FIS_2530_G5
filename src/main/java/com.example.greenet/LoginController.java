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
import java.sql.Types;
import java.util.Objects;

public class LoginController {

    @FXML private TextField correoField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String correo = correoField.getText().trim();
        String password = passwordField.getText().trim();

        if (correo.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Por favor complete todos los campos");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall("{? = call iniciar_sesion(?, ?)}")) {


            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, correo);
            stmt.setString(3, password);
            stmt.execute();

            int resultado = stmt.getInt(1);
            switch (resultado) {
                case 0:
                    showAlert("Éxito", "Login exitoso");
                    // cargar la ventana principal
                    // loadMainWindow();
                    break;
                case 1:
                    showAlert("Error", "El usuario no existe");
                    break;
                case 2:
                    showAlert("Error", "Contraseña incorrecta");
                    break;
                default:
                    showAlert("Error", "Error desconocido: " + resultado);
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
            Stage stage = (Stage) correoField.getScene().getWindow();
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