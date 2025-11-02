package com.example.greenet;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.greenet.service.UsuarioService;
import java.sql.*;

public class SignupController {
    
    //private UsuarioService usuarioService = new UsuarioService();
    @FXML private TextField nombreField;
    @FXML private TextField apellidosField;
    @FXML private TextField fechaNacimientoField;
    @FXML private ComboBox<String> tipoDocCombo;
    @FXML private TextField numeroDocField;
    @FXML private TextField correoField;
    @FXML private PasswordField contrasenaField;
    @FXML private TextField telefonoField;
    @FXML private TextField direccionField;
    @FXML private ComboBox<String> rolCombo;



    @FXML
    public void initialize() {
        System.out.println("✅ Controlador de registro inicializado");

        // Inicializar ComboBox de Tipo de Documento
        ObservableList<String> tiposDocumento = FXCollections.observableArrayList(
                "CC",  
                "TI",
                "CE",
                "PASAPORTE"
        );
        tipoDocCombo.setItems(tiposDocumento);

        // Inicializar ComboBox de Roles
        ObservableList<String> roles = FXCollections.observableArrayList(
                "usuario",        
                "administrador"   
        );
        rolCombo.setItems(roles);

        setupFieldListeners();
    }

    private void setupFieldListeners() {
        // Validación en tiempo real del email
        correoField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !isValidEmail(newVal)) {
                setFieldError(correoField, "Formato de email inválido");
            } else {
                clearFieldError(correoField);
            }
        });

        // Validación en tiempo real de la fecha
        fechaNacimientoField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !isValidDate(newVal)) {
                setFieldError(fechaNacimientoField, "Formato: YYYY-MM-DD");
            } else {
                clearFieldError(fechaNacimientoField);
            }
        });

        // Validación en tiempo real del teléfono
        telefonoField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !isValidPhone(newVal)) {
                setFieldError(telefonoField, "Solo números, mínimo 10 dígitos");
            } else {
                clearFieldError(telefonoField);
            }
        });
    }

    @FXML
    private void handleSignup() {
        if (validarFormulario()) {
            int resultado = registrarUsuario();

            switch (resultado) {
                case 0 -> {
                    mostrarAlerta("✅ Registro Exitoso",
                            "Usuario " + nombreField.getText() + " registrado correctamente",
                            Alert.AlertType.INFORMATION);
                    limpiarFormulario();
                }
                case 1 -> mostrarAlerta("❌ Error", "Formato de correo electrónico inválido", Alert.AlertType.ERROR);
                case 2 -> mostrarAlerta("❌ Error", "El correo electrónico ya está registrado", Alert.AlertType.ERROR);
                case 3 -> mostrarAlerta("❌ Error", "Rol inválido", Alert.AlertType.ERROR);
                case 4 -> mostrarAlerta("❌ Error", "Tipo de documento inválido", Alert.AlertType.ERROR);
                case -1 -> mostrarAlerta("❌ Error", "Error de base de datos", Alert.AlertType.ERROR);
                default -> mostrarAlerta("❌ Error", "Error desconocido: " + resultado, Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void cancelarRegistro() {
        // Cerrar la ventana actual
        nombreField.getScene().getWindow().hide();
    }

    private boolean validarFormulario() {
        boolean valido = true;
        StringBuilder errores = new StringBuilder();

        // Validar cada campo
        if (nombreField.getText().trim().isEmpty()) {
            setFieldError(nombreField, "Campo obligatorio");
            errores.append("• Nombre es obligatorio\n");
            valido = false;
        }

        if (apellidosField.getText().trim().isEmpty()) {
            setFieldError(apellidosField, "Campo obligatorio");
            errores.append("• Apellidos son obligatorios\n");
            valido = false;
        }

        if (fechaNacimientoField.getText().trim().isEmpty()) {
            setFieldError(fechaNacimientoField, "Campo obligatorio");
            errores.append("• Fecha de nacimiento es obligatoria\n");
            valido = false;
        } else if (!isValidDate(fechaNacimientoField.getText())) {
            setFieldError(fechaNacimientoField, "Formato: YYYY-MM-DD");
            errores.append("• Formato de fecha inválido (YYYY-MM-DD)\n");
            valido = false;
        }

        if (tipoDocCombo.getValue() == null) {
            setFieldError(tipoDocCombo, "Selecciona un tipo");
            errores.append("• Tipo de documento es obligatorio\n");
            valido = false;
        }

        if (numeroDocField.getText().trim().isEmpty()) {
            setFieldError(numeroDocField, "Campo obligatorio");
            errores.append("• Número de documento es obligatorio\n");
            valido = false;
        }

        if (correoField.getText().trim().isEmpty()) {
            setFieldError(correoField, "Campo obligatorio");
            errores.append("• Correo electrónico es obligatorio\n");
            valido = false;
        } else if (!isValidEmail(correoField.getText())) {
            setFieldError(correoField, "Email inválido");
            errores.append("• Formato de email inválido\n");
            valido = false;
        }

        if (contrasenaField.getText().isEmpty()) {
            setFieldError(contrasenaField, "Campo obligatorio");
            errores.append("• Contraseña es obligatoria\n");
            valido = false;
        } else if (contrasenaField.getText().length() < 6) {
            setFieldError(contrasenaField, "Mínimo 6 caracteres");
            errores.append("• La contraseña debe tener al menos 6 caracteres\n");
            valido = false;
        }

        if (rolCombo.getValue() == null) {
            setFieldError(rolCombo, "Selecciona un rol");
            errores.append("• Rol es obligatorio\n");
            valido = false;
        }

        if (!valido) {
            mostrarAlerta("⚠️ Errores de Validación",
                    "Por favor corrige los siguientes errores:\n\n" + errores.toString(),
                    Alert.AlertType.WARNING);
        }

        return valido;
    }

    private int registrarUsuario() {
        // Obtener valores de los campos
        String nombre = nombreField.getText().trim();
        String apellidos = apellidosField.getText().trim();
        String fechaNacimiento = fechaNacimientoField.getText().trim();
        String tipoDoc = tipoDocCombo.getValue();
        String numeroDoc = numeroDocField.getText().trim();
        String correo = correoField.getText().trim();
        String contrasena = contrasenaField.getText();
        String telefono = telefonoField.getText().trim();
        String direccion = direccionField.getText().trim();
        String rol = rolCombo.getValue();

        // Debug: Mostrar los valores que se enviarán
        System.out.println("📝 Intentando registrar usuario:");
        System.out.println("   Nombre: " + nombre);
        System.out.println("   Apellidos: " + apellidos);
        System.out.println("   Fecha: " + fechaNacimiento);
        System.out.println("   Tipo Doc: " + tipoDoc);
        System.out.println("   Número Doc: " + numeroDoc);
        System.out.println("   Correo: " + correo);
        System.out.println("   Teléfono: " + telefono);
        System.out.println("   Dirección: " + direccion);
        System.out.println("   Rol: " + rol);

        try {
            // Llamar al servicio con TODOS los parámetros
            int resultado = UsuarioService.registrarUsuario(
                    nombre,
                    apellidos,
                    fechaNacimiento,  // Usar la fecha del campo, no una fija
                    tipoDoc,
                    numeroDoc,        // Usar el número del campo, no uno fijo
                    correo,
                    contrasena,
                    rol,              // Usar el rol seleccionado
                    telefono,
                    direccion
            );

            return resultado;

        } catch (Exception e) {
            System.err.println("❌ Error en el controlador: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    private void limpiarFormulario() {
        nombreField.clear();
        apellidosField.clear();
        fechaNacimientoField.clear();
        tipoDocCombo.setValue(null);
        numeroDocField.clear();
        correoField.clear();
        contrasenaField.clear();
        telefonoField.clear();
        direccionField.clear();
        rolCombo.setValue(null);

        // Limpiar errores visuales
        clearAllFieldErrors();
    }

    private void clearAllFieldErrors() {
        clearFieldError(nombreField);
        clearFieldError(apellidosField);
        clearFieldError(fechaNacimientoField);
        clearFieldError(tipoDocCombo);
        clearFieldError(numeroDocField);
        clearFieldError(correoField);
        clearFieldError(contrasenaField);
        clearFieldError(telefonoField);
        clearFieldError(direccionField);
        clearFieldError(rolCombo);
    }

    // Métodos de utilidad
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private boolean isValidDate(String date) {
        return date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("^\\d{10,15}$");
    }

    private void setFieldError(Control field, String message) {
        field.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2px;");
        if (field instanceof TextInputControl) {
            Tooltip tooltip = new Tooltip(message);
            tooltip.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            ((TextInputControl) field).setTooltip(tooltip);
        } else if (field instanceof ComboBox) {
            Tooltip tooltip = new Tooltip(message);
            tooltip.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            ((ComboBox<?>) field).setTooltip(tooltip);
        }
    }

    private void clearFieldError(Control field) {
        field.setStyle("-fx-border-color: null;");
        if (field instanceof TextInputControl) {
            ((TextInputControl) field).setTooltip(null);
        } else if (field instanceof ComboBox) {
            ((ComboBox<?>) field).setTooltip(null);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

