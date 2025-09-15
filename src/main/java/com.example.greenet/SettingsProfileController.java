package com.example.greenet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;

public class SettingsProfileController {

    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtDireccion;
    
    @FXML private Button btnTelefono;
    @FXML private Button btnCorreo;
    @FXML private Button btnDireccion;
    @FXML private Button btnCerrarSesion;

    // Variable para almacenar el ID del usuario actual
    private int usuarioActualId;
    private String usuarioActualNombre;

    @FXML
    public void initialize() {
        configurarEventos();
        cargarDatosUsuario();
    }

    private void configurarEventos() {
        // Configurar eventos para los botones "Aplicar"
        btnTelefono.setOnAction(e -> actualizarTelefono());
        btnCorreo.setOnAction(e -> actualizarCorreo());
        btnDireccion.setOnAction(e -> actualizarDireccion());
        btnCerrarSesion.setOnAction(e -> cerrarSesion());
        
        // Agregar placeholder text si no está en el FXML
        txtTelefono.setPromptText("Escribe tu telefono...");
        txtCorreo.setPromptText("Escribe tu correo...");
        txtDireccion.setPromptText("Escribe tu dirección...");
    }

    // Método para establecer el usuario actual (llamado desde otra ventana)
    public void setUsuarioActual(int usuarioId, String nombreUsuario) {
        this.usuarioActualId = usuarioId;
        this.usuarioActualNombre = nombreUsuario;
        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        // Si no se ha establecido el usuario, usar el de la sesión actual
        if (usuarioActualId == 0) {
            usuarioActualId = LoginController.getUsuarioLogueadoId();
            usuarioActualNombre = LoginController.getUsuarioLogueadoNombre();
        }
        
        if (usuarioActualId == 0) {
            mostrarAlerta("Error", "No se pudo identificar el usuario actual");
            return;
        }

        String sql = "SELECT correo, telefono, direccion FROM usuarios WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioActualId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Cargar datos existentes en los campos
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                String direccion = rs.getString("direccion");
                
                if (telefono != null) txtTelefono.setText(telefono);
                if (correo != null) txtCorreo.setText(correo);
                if (direccion != null) txtDireccion.setText(direccion);
            }

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar datos del usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void actualizarTelefono() {
        String telefono = txtTelefono.getText().trim();
        
        if (telefono.isEmpty()) {
            mostrarAlerta("Error", "El campo teléfono no puede estar vacío");
            return;
        }
        
        if (!telefono.matches("\\d{7,15}")) {
            mostrarAlerta("Error", "El teléfono debe contener entre 7 y 15 dígitos");
            return;
        }

        if (actualizarCampo("telefono", telefono)) {
            mostrarAlerta("Éxito", "Teléfono actualizado correctamente");
        }
    }

    @FXML
    private void actualizarCorreo() {
        String correo = txtCorreo.getText().trim();
        
        if (correo.isEmpty()) {
            mostrarAlerta("Error", "El campo correo no puede estar vacío");
            return;
        }
        
        if (!isValidEmail(correo)) {
            mostrarAlerta("Error", "Por favor ingrese un correo electrónico válido");
            return;
        }

        // Verificar que el correo no esté en uso por otro usuario
        if (correoEnUso(correo)) {
            mostrarAlerta("Error", "Este correo ya está siendo utilizado por otro usuario");
            return;
        }

        if (actualizarCampo("correo", correo)) {
            mostrarAlerta("Éxito", "Correo actualizado correctamente");
        }
    }

    @FXML
    private void actualizarDireccion() {
        String direccion = txtDireccion.getText().trim();
        
        if (direccion.isEmpty()) {
            mostrarAlerta("Error", "El campo dirección no puede estar vacío");
            return;
        }
        
        if (direccion.length() < 10) {
            mostrarAlerta("Error", "La dirección debe tener al menos 10 caracteres");
            return;
        }

        if (actualizarCampo("direccion", direccion)) {
            mostrarAlerta("Éxito", "Dirección actualizada correctamente");
        }
    }

    private boolean actualizarCampo(String campo, String valor) {
        String sql = "UPDATE usuarios SET " + campo + " = ?, fecha_actualizacion = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valor);
            stmt.setInt(2, usuarioActualId);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar " + campo + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean correoEnUso(String correo) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE correo = ? AND id != ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, correo);
            stmt.setInt(2, usuarioActualId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }

    @FXML
    private void cerrarSesion() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cerrar Sesión");
        confirmacion.setHeaderText("¿Desea cerrar sesión?");
        confirmacion.setContentText("Será redirigido a la pantalla de inicio de sesión");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Limpiar la sesión
            LoginController.limpiarSesion();
            
            mostrarAlerta("Sesión cerrada", "Gracias por usar GREENET");
            volverAlLogin();
        }
    }

    private void volverAlLogin() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LOGIN.fxml")));
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            stage.setScene(new Scene(root, 354, 600));
            stage.setTitle("GREENET - Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para volver a la ventana anterior (si se necesita)
    public void volverAtras() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MAIN.fxml")));
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GREENET - Principal");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al regresar a la ventana principal");
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)\\.[A-Za-z]{2,}$");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert;
        
        if ("Éxito".equals(titulo)) {
            alert = new Alert(Alert.AlertType.INFORMATION);
        } else if ("Error".equals(titulo)) {
            alert = new Alert(Alert.AlertType.ERROR);
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método adicional para refrescar los datos (útil si se llama desde otra ventana)
    public void refrescarDatos() {
        cargarDatosUsuario();
    }
}
