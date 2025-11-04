package com.greenet;

import com.greenet.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
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
    @FXML private Button btnVolver;

    // Variables del usuario actual
    private int usuarioActualId;
    private String usuarioActualCorreo;

    @FXML
    public void initialize() {
        configurarEventos();
    }

    private void configurarEventos() {
        // Configurar eventos para los botones
        btnTelefono.setOnAction(e -> actualizarTelefono());
        btnCorreo.setOnAction(e -> actualizarCorreo());
        btnDireccion.setOnAction(e -> actualizarDireccion());


        // Placeholders
        txtTelefono.setPromptText("Escribe tu teléfono...");
        txtCorreo.setPromptText("Escribe tu correo...");
        txtDireccion.setPromptText("Escribe tu dirección...");
    }

    /**
     * Establece el usuario actual (llamado desde LoginController)
     */

    public void setUsuarioActual(int usuarioId, String correoUsuario) {
        this.usuarioActualId = usuarioId;
        this.usuarioActualCorreo = correoUsuario;
        cargarDatosUsuario();

        System.out.println("✅ Usuario cargado en Settings: ID=" + usuarioId + ", Correo=" + correoUsuario);
    }

    /**
     * Carga los datos del usuario desde la base de datos
     */
    private void cargarDatosUsuario() {
        if (usuarioActualId == 0) {
            mostrarAlerta("Error", "No se pudo identificar el usuario actual");
            return;
        }

        String sql = """
            SELECT u.correo, u.telefono, u.direccion, u.nombre, u.apellidos 
            FROM usuarios u 
            WHERE u.id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioActualId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                String direccion = rs.getString("direccion");

                txtTelefono.setText(telefono != null ? telefono : "");
                txtCorreo.setText(correo != null ? correo : "");
                txtDireccion.setText(direccion != null ? direccion : "");

                System.out.println("✅ Datos cargados: " + rs.getString("nombre") + " " + rs.getString("apellidos"));
            } else {
                mostrarAlerta("Error", "No se encontraron datos del usuario");
            }

        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al cargar datos del usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualiza el teléfono del usuario
     */
    @FXML
    private void actualizarTelefono() {
        String telefono = txtTelefono.getText().trim();

        // Validaciones
        if (telefono.isEmpty()) {
            mostrarAlerta("Error", "El campo teléfono no puede estar vacío");
            return;
        }

        if (!telefono.matches("\\d{7,15}")) {
            mostrarAlerta("Error", "El teléfono debe contener entre 7 y 15 dígitos");
            return;
        }

        // Actualizar en BD
        if (actualizarCampoUsuario("telefono", telefono)) {
            mostrarAlerta("Éxito", "Teléfono actualizado correctamente");
            System.out.println("✅ Teléfono actualizado: " + telefono);
        }
    }

    /**
     * Actualiza el correo del usuario usando UsuarioService
     */
    @FXML
    private void actualizarCorreo() {
        String nuevoCorreo = txtCorreo.getText().trim();

        // Validaciones
        if (nuevoCorreo.isEmpty()) {
            mostrarAlerta("Error", "El campo correo no puede estar vacío");
            return;
        }

        if (nuevoCorreo.equals(usuarioActualCorreo)) {
            mostrarAlerta("Información", "El correo es el mismo que el actual");
            return;
        }

        if (!isValidEmail(nuevoCorreo)) {
            mostrarAlerta("Error", "Por favor ingrese un correo electrónico válido");
            return;
        }

        // ✅ NUEVO: Usar UsuarioService en lugar de función de BD
        int resultado = UsuarioService.actualizarCorreo(usuarioActualId, nuevoCorreo);

        switch (resultado) {
            case 0 -> {
                mostrarAlerta("Éxito", "Correo actualizado correctamente");
                this.usuarioActualCorreo = nuevoCorreo;
                System.out.println("✅ Correo actualizado: " + nuevoCorreo);
            }
            case 1 -> mostrarAlerta("Error", "Formato de correo inválido");
            case 2 -> mostrarAlerta("Error", "Este correo ya está siendo utilizado por otro usuario");
            case 3 -> mostrarAlerta("Error", "Usuario no encontrado");
            default -> mostrarAlerta("Error", "Error desconocido al actualizar correo");
        }
    }

    /**
     * Actualiza la dirección del usuario
     */
    @FXML
    private void actualizarDireccion() {
        String direccion = txtDireccion.getText().trim();

        // Validaciones
        if (direccion.isEmpty()) {
            mostrarAlerta("Error", "El campo dirección no puede estar vacío");
            return;
        }

        if (direccion.length() < 10) {
            mostrarAlerta("Error", "La dirección debe tener al menos 10 caracteres");
            return;
        }

        // Actualizar en BD
        if (actualizarCampoUsuario("direccion", direccion)) {
            mostrarAlerta("Éxito", "Dirección actualizada correctamente");
            System.out.println("✅ Dirección actualizada: " + direccion);
        }
    }

    /**
     * Actualiza un campo específico del usuario en la base de datos
     */
    private boolean actualizarCampoUsuario(String campo, String valor) {
        String sql = "UPDATE usuarios SET " + campo + " = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valor);
            stmt.setInt(2, usuarioActualId);

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("✅ Campo '" + campo + "' actualizado para usuario ID: " + usuarioActualId);
                return true;
            } else {
                System.out.println("⚠️ No se actualizó ninguna fila para campo: " + campo);
                return false;
            }

        } catch (SQLException e) {
            mostrarAlerta("Error", "Error al actualizar " + campo + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cierra la sesión del usuario
     */
    @FXML


    /**
     * Cierra la sesión en la base de datos
     */


    /**
     * Vuelve a la pantalla de login
     */


    /**
     * Vuelve a la ventana principal
     */
    public void volverAtras() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("Home.fxml")
            ));

            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root, 354, 600));
            stage.setTitle("GREENET - home");

            System.out.println("✅ Redirigido a home");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al regresar a la ventana principal");
            e.printStackTrace();
        }
    }

    /**
     * Valida el formato de un email
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)\\.[A-Za-z]{2,}$");
    }

    /**
     * Muestra una alerta al usuario
     */
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
}
