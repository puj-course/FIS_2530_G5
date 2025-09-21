package com.example.greenet;

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

    // Variable para almacenar el ID del usuario actual
    private int usuarioActualId;
    private String usuarioActualCorreo;

    @FXML
    public void initialize() {
        configurarEventos();
        // cargarDatosUsuario() se llamará después de setUsuarioActual()
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

    // Método para establecer el usuario actual (llamado desde LoginController)
    public void setUsuarioActual(int usuarioId, String correoUsuario) {
        this.usuarioActualId = usuarioId;
        this.usuarioActualCorreo = correoUsuario;
        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        if (usuarioActualId == 0) {
            mostrarAlerta("Error", "No se pudo identificar el usuario actual");
            return;
        }

        // Consulta actualizada con los nuevos campos
        String sql = "SELECT u.correo, u.telefono, u.direccion, u.nombre, u.apellidos " +
                "FROM usuarios u " +
                "WHERE u.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioActualId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Cargar datos existentes en los campos
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                String direccion = rs.getString("direccion");

                // Usar valores por defecto si son null
                txtTelefono.setText(telefono != null ? telefono : "");
                txtCorreo.setText(correo != null ? correo : "");
                txtDireccion.setText(direccion != null ? direccion : "");
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

        if (actualizarCampoUsuario("telefono", telefono)) {
            mostrarAlerta("Éxito", "Teléfono actualizado correctamente");
        }
    }

    @FXML
    private void actualizarCorreo() {
        String nuevoCorreo = txtCorreo.getText().trim();

        if (nuevoCorreo.isEmpty()) {
            mostrarAlerta("Error", "El campo correo no puede estar vacío");
            return;
        }

        // Verificación inmediata en el UI
        if (nuevoCorreo.equals(usuarioActualCorreo)) {
            mostrarAlerta("Información", "El correo es el mismo que el actual");
            return;
        }

        if (!isValidEmail(nuevoCorreo)) {
            mostrarAlerta("Error", "Por favor ingrese un correo electrónico válido");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall("{? = call actualizar_correo_usuario(?, ?)}")) {

            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, usuarioActualId);
            stmt.setString(3, nuevoCorreo);
            stmt.execute();

            int resultado = stmt.getInt(1);

            switch (resultado) {
                case 0:
                    // Verificar si realmente cambió o es el mismo
                    if (nuevoCorreo.equals(usuarioActualCorreo)) {
                        mostrarAlerta("Información", "El correo se mantiene igual");
                    } else {
                        mostrarAlerta("Éxito", "Correo actualizado correctamente");
                        this.usuarioActualCorreo = nuevoCorreo;
                    }
                    break;
                case 1:
                    mostrarAlerta("Error", "Formato de correo inválido");
                    break;
                case 2:
                    mostrarAlerta("Error", "Este correo ya está siendo utilizado por otro usuario");
                    break;
                case 3:
                    mostrarAlerta("Error", "Usuario no encontrado");
                    break;
                default:
                    mostrarAlerta("Error", "Error desconocido: " + resultado);
            }

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar correo: " + e.getMessage());
            e.printStackTrace();
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

        if (actualizarCampoUsuario("direccion", direccion)) {
            mostrarAlerta("Éxito", "Dirección actualizada correctamente");
        }
    }

    private boolean actualizarCampoUsuario(String campo, String valor) {
        String sql = "UPDATE usuarios SET " + campo + " = ? WHERE id = ?";

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

    @FXML
    private void cerrarSesion() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cerrar Sesión");
        confirmacion.setHeaderText("¿Desea cerrar sesión?");
        confirmacion.setContentText("Será redirigido a la pantalla de inicio de sesión");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

            // Llamar a la función cerrar_sesion de tu base de datos
            cerrarSesionBD();

            mostrarAlerta("Sesión cerrada", "Gracias por usar GREENET");
            volverAlLogin();
        }
    }

    private void cerrarSesionBD() {
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall("{ ? = call cerrar_sesion(?) }")) {

            stmt.registerOutParameter(1, java.sql.Types.INTEGER);
            stmt.setInt(2, usuarioActualId);
            stmt.execute();

            int resultado = stmt.getInt(1);

            if (resultado != 0) {
                System.out.println("No se pudo cerrar la sesión en la base de datos. Código: " + resultado);
            }

        } catch (Exception e) {
            System.err.println("Error al cerrar sesión en BD: " + e.getMessage());
            e.printStackTrace();
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
