package com.example.greenet;

import com.example.greenet.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Objects;

public class LoginController {

    @FXML private TextField correoField;
    @FXML private PasswordField passwordField;

    @FXML
    public void initialize() {
        System.out.println("✅ LoginController inicializado");

        // Configurar Enter para iniciar sesión
        passwordField.setOnAction(e -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String correo = correoField.getText().trim();
        String contrasena = passwordField.getText().trim();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            showAlert("❌ Error", "Por favor complete todos los campos", Alert.AlertType.ERROR);
            return;
        }

        // Validar formato de email
        if (!isValidEmail(correo)) {
            showAlert("❌ Error", "Formato de correo electrónico inválido", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Usar el servicio para iniciar sesión
            int resultado = UsuarioService.iniciarSesion(correo, contrasena);

            switch (resultado) {
                case 0:
                    // Login exitoso - obtener ID del usuario
                    int usuarioId = obtenerIdUsuario(correo);
                    if (usuarioId > 0) {
                        showAlert("✅ Éxito", "Inicio de sesión exitoso", Alert.AlertType.INFORMATION);
                        handleGoToProfile(usuarioId, correo);
                    } else {
                        showAlert("❌ Error", "No se pudo obtener información del usuario", Alert.AlertType.ERROR);
                    }
                    break;
                case 1:
                    showAlert("❌ Error", "El usuario no existe o está bloqueado", Alert.AlertType.ERROR);
                    break;
                case 2:
                    showAlert("❌ Error", "Contraseña incorrecta", Alert.AlertType.ERROR);
                    break;
                case -1:
                    showAlert("❌ Error", "Error de conexión con la base de datos", Alert.AlertType.ERROR);
                    break;
                default:
                    showAlert("❌ Error", "Error desconocido: " + resultado, Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            showAlert("❌ Error", "Error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el ID del usuario por correo
     */
    private int obtenerIdUsuario(String correo) {
        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement("SELECT id FROM usuarios WHERE correo = ?")) {

            stmt.setString(1, correo);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            System.err.println("Error al obtener ID de usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Navega a la pantalla de perfil después del login exitoso
     */
    private void handleGoToProfile(int usuarioId, String correo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y establecer el usuario
            SettingsProfileController controller = loader.getController();
            controller.setUsuarioActual(usuarioId, correo);

            Stage stage = (Stage) correoField.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 600));
            stage.setTitle("GREENET - Perfil");
            stage.centerOnScreen();

        } catch (Exception e) {
            System.err.println("Error al cargar perfil: " + e.getMessage());
            showAlert("❌ Error", "Error al cargar la ventana de perfil", Alert.AlertType.ERROR);

            // Fallback: mostrar información del login exitoso
            System.out.println("✅ Login exitoso - Usuario ID: " + usuarioId + ", Correo: " + correo);
        }
    }

    @FXML
    private void handleGoToSignup() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SIGNUP.fxml")));
            Stage stage = (Stage) correoField.getScene().getWindow();
            stage.setScene(new Scene(root, 500, 700));
            stage.setTitle("GREENET - Registro");
            stage.centerOnScreen();
        } catch (Exception e) {
            System.err.println("Error al cargar registro: " + e.getMessage());
            showAlert("❌ Error", "Error al cargar la ventana de registro", Alert.AlertType.ERROR);
        }
    }

    /**
     * Valida formato de email
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$");
    }

    /**
     * Muestra alertas con emojis y mejor formato
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Personalizar según el tipo
        switch (type) {
            case ERROR:
                alert.getDialogPane().setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
                break;
            case INFORMATION:
                alert.getDialogPane().setStyle("-fx-border-color: #27ae60; -fx-border-width: 2px;");
                break;
            case WARNING:
                alert.getDialogPane().setStyle("-fx-border-color: #f39c12; -fx-border-width: 2px;");
                break;
        }

        alert.showAndWait();
    }
}