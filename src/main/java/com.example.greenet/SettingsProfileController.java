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

    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtUsuario;
    @FXML private DatePicker dateFechaNacimiento;
    @FXML private ComboBox<String> cbTipoDocumento;
    @FXML private TextField txtNumeroDocumento;
    
    @FXML private Button btnActualizarPerfil;
    @FXML private Button btnCambiarContrasena;
    @FXML private Button btnEliminarCuenta;
    @FXML private Button btnCerrarSesion;
    @FXML private Button btnVolver;
    
    @FXML private PasswordField passwordActual;
    @FXML private PasswordField passwordNueva;
    @FXML private PasswordField passwordConfirmar;

    // Variable para almacenar el ID del usuario actual
    private int usuarioActualId;
    private String usuarioActualNombre;

    @FXML
    public void initialize() {
        // Inicializar ComboBox de tipo de documento
        cbTipoDocumento.getItems().addAll(
            "Cédula de Ciudadanía",
            "Cédula de Extranjería", 
            "Pasaporte",
            "Tarjeta de Identidad"
        );
        
        configurarEventos();
        // cargarDatosUsuario() se llamará desde fuera cuando se establezca el usuario
    }

    private void configurarEventos() {
        btnActualizarPerfil.setOnAction(e -> actualizarPerfil());
        btnCambiarContrasena.setOnAction(e -> cambiarContrasena());
        btnEliminarCuenta.setOnAction(e -> eliminarCuenta());
        btnCerrarSesion.setOnAction(e -> cerrarSesion());
        btnVolver.setOnAction(e -> volverAtras());
    }

    // Método para establecer el usuario actual (llamado desde LoginController)
    public void setUsuarioActual(int usuarioId, String nombreUsuario) {
        this.usuarioActualId = usuarioId;
        this.usuarioActualNombre = nombreUsuario;
        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        String sql = "SELECT nombres, apellidos, correo, telefono, direccion, " +
                    "fecha_nacimiento, tipo_documento, numero_documento, usuario " +
                    "FROM usuarios WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioActualId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                txtNombres.setText(rs.getString("nombres"));
                txtApellidos.setText(rs.getString("apellidos"));
                txtCorreo.setText(rs.getString("correo"));
                txtTelefono.setText(rs.getString("telefono"));
                txtDireccion.setText(rs.getString("direccion"));
                txtUsuario.setText(rs.getString("usuario"));
                txtNumeroDocumento.setText(rs.getString("numero_documento"));
                
                // Cargar fecha de nacimiento
                if (rs.getDate("fecha_nacimiento") != null) {
                    dateFechaNacimiento.setValue(rs.getDate("fecha_nacimiento").toLocalDate());
                }
                
                // Cargar tipo de documento
                String tipoDoc = rs.getString("tipo_documento");
                if (tipoDoc != null) {
                    cbTipoDocumento.setValue(tipoDoc);
                }
            }

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar datos del usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void actualizarPerfil() {
        // Validar campos obligatorios
        if (!validarCamposObligatorios()) {
            return;
        }

        // Validar formato de datos
        if (!validarFormatos()) {
            return;
        }

        String sql = "{call actualizar_perfil_usuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, usuarioActualId);
            stmt.setString(2, txtNombres.getText().trim());
            stmt.setString(3, txtApellidos.getText().trim());
            stmt.setString(4, txtCorreo.getText().trim());
            stmt.setString(5, txtTelefono.getText().trim());
            stmt.setString(6, txtDireccion.getText().trim());
            stmt.setString(7, txtUsuario.getText().trim());
            
            if (dateFechaNacimiento.getValue() != null) {
                stmt.setDate(8, java.sql.Date.valueOf(dateFechaNacimiento.getValue()));
            } else {
                stmt.setNull(8, java.sql.Types.DATE);
            }
            
            stmt.setString(9, cbTipoDocumento.getValue());
            stmt.setString(10, txtNumeroDocumento.getText().trim());

            stmt.execute();
            mostrarAlerta("Éxito", "Perfil actualizado correctamente");

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar perfil: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cambiarContrasena() {
        String actual = passwordActual.getText();
        String nueva = passwordNueva.getText();
        String confirmar = passwordConfirmar.getText();

        if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
            mostrarAlerta("Error", "Por favor complete todos los campos de contraseña");
            return;
        }

        if (!nueva.equals(confirmar)) {
            mostrarAlerta("Error", "La nueva contraseña y su confirmación no coinciden");
            return;
        }

        if (nueva.length() < 6) {
            mostrarAlerta("Error", "La nueva contraseña debe tener al menos 6 caracteres");
            return;
        }

        String sql = "{? = call cambiar_contrasena(?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
            stmt.setInt(2, usuarioActualId);
            stmt.setString(3, actual);
            stmt.setString(4, nueva);
            stmt.execute();

            boolean cambioExitoso = stmt.getBoolean(1);

            if (cambioExitoso) {
                mostrarAlerta("Éxito", "Contraseña cambiada correctamente");
                // Limpiar campos de contraseña
                passwordActual.clear();
                passwordNueva.clear();
                passwordConfirmar.clear();
            } else {
                mostrarAlerta("Error", "La contraseña actual es incorrecta");
            }

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cambiar contraseña: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarCuenta() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro que desea eliminar su cuenta?");
        confirmacion.setContentText("Esta acción es irreversible. Se eliminarán todos sus datos.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            
            // Pedir contraseña para confirmar
            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Confirmar eliminación");
            passwordDialog.setHeaderText("Ingrese su contraseña para confirmar");
            passwordDialog.setContentText("Contraseña:");

            Optional<String> password = passwordDialog.showAndWait();
            if (password.isPresent() && !password.get().isEmpty()) {
                eliminarCuentaConPassword(password.get());
            }
        }
    }

    private void eliminarCuentaConPassword(String password) {
        String sql = "{? = call eliminar_cuenta_usuario(?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, java.sql.Types.BOOLEAN);
            stmt.setInt(2, usuarioActualId);
            stmt.setString(3, password);
            stmt.execute();

            boolean eliminacionExitosa = stmt.getBoolean(1);

            if (eliminacionExitosa) {
                mostrarAlerta("Cuenta eliminada", "Su cuenta ha sido eliminada exitosamente");
                volverAlLogin();
            } else {
                mostrarAlerta("Error", "Contraseña incorrecta. No se pudo eliminar la cuenta");
            }

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al eliminar cuenta: " + e.getMessage());
            e.printStackTrace();
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
            mostrarAlerta("Sesión cerrada", "Gracias por usar GREENET");
            volverAlLogin();
        }
    }

    @FXML
    private void volverAtras() {
        try {
            // Aquí deberías cargar la ventana principal de la aplicación
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MAIN.fxml")));
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GREENET - Principal");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al regresar a la ventana principal");
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

    private boolean validarCamposObligatorios() {
        if (txtNombres.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "El campo nombres es obligatorio");
            txtNombres.requestFocus();
            return false;
        }
        if (txtApellidos.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "El campo apellidos es obligatorio");
            txtApellidos.requestFocus();
            return false;
        }
        if (txtCorreo.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "El campo correo es obligatorio");
            txtCorreo.requestFocus();
            return false;
        }
        if (txtUsuario.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "El campo usuario es obligatorio");
            txtUsuario.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validarFormatos() {
        // Validar correo electrónico
        if (!isValidEmail(txtCorreo.getText().trim())) {
            mostrarAlerta("Error", "Por favor ingrese un correo electrónico válido");
            txtCorreo.requestFocus();
            return false;
        }

        // Validar teléfono si no está vacío
        String telefono = txtTelefono.getText().trim();
        if (!telefono.isEmpty() && !telefono.matches("\\d{7,15}")) {
            mostrarAlerta("Error", "El teléfono debe contener entre 7 y 15 dígitos");
            txtTelefono.requestFocus();
            return false;
        }

        // Validar número de documento si no está vacío
        String numeroDoc = txtNumeroDocumento.getText().trim();
        if (!numeroDoc.isEmpty() && numeroDoc.length() < 5) {
            mostrarAlerta("Error", "El número de documento debe tener al menos 5 caracteres");
            txtNumeroDocumento.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para mostrar alertas de error específicamente
    private void mostrarAlertaError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para mostrar alertas de confirmación
    private boolean mostrarAlertaConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        
        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }
}
