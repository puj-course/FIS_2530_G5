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
import java.sql.Types;
import java.util.Objects;

public class SignupController {

    @FXML private TextField nombresField;
    @FXML private TextField apellidosField;
    @FXML private TextField correoField;
    @FXML private TextField usuarioField;
    @FXML private PasswordField passwordField;
    @FXML private TextField fechaNacimientoField;
    @FXML private TextField tipoDocField;
    @FXML private TextField numeroDocField;
    @FXML private TextField telefonoField;      // Nuevo campo
    @FXML private TextField direccionField;     // Nuevo campo

    @FXML
    private void handleSignup() {
        String nombres = nombresField.getText().trim();
        String apellidos = apellidosField.getText().trim();
        String fechaNacimiento = fechaNacimientoField.getText().trim();
        String tipoDoc = tipoDocField.getText().trim().toUpperCase();
        String numeroDoc = numeroDocField.getText().trim();
        String correo = correoField.getText().trim();
        String password = passwordField.getText().trim();
        String usuario = usuarioField.getText().trim();
        String telefono = telefonoField.getText().trim();    // Nuevo
        String direccion = direccionField.getText().trim();  // Nuevo

        // Validaciones
        if (nombres.isEmpty() || apellidos.isEmpty() || fechaNacimiento.isEmpty() ||
                tipoDoc.isEmpty() || numeroDoc.isEmpty() || correo.isEmpty() ||
                password.isEmpty() || usuario.isEmpty()) {
            showAlert("Error", "Por favor complete todos los campos obligatorios");
            return;
        }

        if (!isValidEmail(correo)) {
            showAlert("Error", "Por favor ingrese un correo electrónico válido");
            return;
        }

        if (!isValidDate(fechaNacimiento)) {
            showAlert("Error", "Formato de fecha inválido. Use YYYY-MM-DD");
            return;
        }

        if (!isValidTipoDoc(tipoDoc)) {
            showAlert("Error", "Tipo de documento inválido. Use CC, TI, CE o PASAPORTE");
            return;
        }

        if (!isValidNumeroDoc(numeroDoc)) {
            showAlert("Error", "Número de documento debe contener solo dígitos");
            return;
        }

        // Validar teléfono si se ingresó
        if (!telefono.isEmpty() && !isValidTelefono(telefono)) {
            showAlert("Error", "Teléfono debe contener entre 7 y 15 dígitos");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall("{? = call registrar_usuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {

            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, nombres);
            stmt.setString(3, apellidos);
            stmt.setString(4, fechaNacimiento);
            stmt.setString(5, tipoDoc);
            stmt.setString(6, numeroDoc);
            stmt.setString(7, correo);
            stmt.setString(8, password);
            stmt.setString(9, "usuario");
            stmt.setString(10, telefono.isEmpty() ? null : telefono);      // Teléfono
            stmt.setString(11, direccion.isEmpty() ? null : direccion);    // Dirección

            stmt.execute();
            int resultado = stmt.getInt(1);

            switch (resultado) {
                case 0:
                    showAlert("Éxito", "Usuario registrado correctamente");
                    handleGoToLogin();
                    break;
                case 1:
                    showAlert("Error", "Correo electrónico inválido");
                    break;
                case 2:
                    showAlert("Error", "El correo electrónico ya está registrado");
                    break;
                case 3:
                    showAlert("Error", "Rol de usuario inválido");
                    break;
                case 4:
                    showAlert("Error", "Tipo de documento inválido");
                    break;
                case 5:
                    showAlert("Error", "Error inesperado en el servidor");
                    break;
                default:
                    showAlert("Error", "Error desconocido: " + resultado);
            }

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

    private boolean isValidDate(String date) {
        return date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    private boolean isValidTipoDoc(String tipoDoc) {
        return tipoDoc.matches("^(CC|TI|CE|PASAPORTE)$");
    }

    private boolean isValidNumeroDoc(String numeroDoc) {
        return numeroDoc.matches("\\d+");
    }

    private boolean isValidTelefono(String telefono) {
        return telefono.matches("\\d{7,15}");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
