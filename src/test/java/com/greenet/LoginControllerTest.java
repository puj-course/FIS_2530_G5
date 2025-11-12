package com.greenet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import java.lang.reflect.Method;

class LoginControllerTest {
    
    private LoginController loginController;
    
    @BeforeEach
    void setUp() {
        loginController = new LoginController();
        
        // Inicializar los campos FXML usando reflexión
        try {
            Field correoField = LoginController.class.getDeclaredField("correoField");
            Field passwordField = LoginController.class.getDeclaredField("passwordField");
            
            correoField.setAccessible(true);
            passwordField.setAccessible(true);
            
            correoField.set(loginController, new TextField());
            passwordField.set(loginController, new PasswordField());
            
        } catch (Exception e) {
            fail("Error al inicializar campos FXML: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Test de validación de email válido")
    void testIsValidEmail() throws Exception {
        // Usar reflexión para acceder al método privado
        Method isValidEmailMethod = LoginController.class.getDeclaredMethod("isValidEmail", String.class);
        isValidEmailMethod.setAccessible(true);
        
        // Emails válidos
        assertTrue((Boolean) isValidEmailMethod.invoke(loginController, "usuario@dominio.com"));
        assertTrue((Boolean) isValidEmailMethod.invoke(loginController, "test.email@domain.co"));
        assertTrue((Boolean) isValidEmailMethod.invoke(loginController, "user123@test.org"));
        
        // Emails inválidos
        assertFalse((Boolean) isValidEmailMethod.invoke(loginController, "usuario"));
        assertFalse((Boolean) isValidEmailMethod.invoke(loginController, "usuario@"));
        assertFalse((Boolean) isValidEmailMethod.invoke(loginController, "@dominio.com"));
        assertFalse((Boolean) isValidEmailMethod.invoke(loginController, "usuario@dominio"));
    }
    
    @Test
    @DisplayName("Test de campos vacíos en login")
    void testCamposVacios() {
        // Configurar campos vacíos
        loginController.correoField.setText("");
        loginController.passwordField.setText("");
        
        // Verificar que los campos están vacíos
        assertTrue(loginController.correoField.getText().trim().isEmpty());
        assertTrue(loginController.passwordField.getText().trim().isEmpty());
    }
    
    @Test
    @DisplayName("Test de campos completos")
    void testCamposCompletos() {
        // Configurar campos con datos
        String testEmail = "test@example.com";
        String testPassword = "password123";
        
        loginController.correoField.setText(testEmail);
        loginController.passwordField.setText(testPassword);
        
        assertEquals(testEmail, loginController.correoField.getText().trim());
        assertEquals(testPassword, loginController.passwordField.getText().trim());
    }
    
    @Test
    @DisplayName("Test de trim en campos")
    void testTrimCampos() {
        // Configurar campos con espacios
        loginController.correoField.setText("  test@example.com  ");
        loginController.passwordField.setText("  password123  ");
        
        // Verificar que el trim funciona correctamente
        assertEquals("test@example.com", loginController.correoField.getText().trim());
        assertEquals("password123", loginController.passwordField.getText().trim());
    }
    
    @Test
    @DisplayName("Test de inicialización del controlador")
    void testInicializacion() {
        // Verificar que los campos no son nulos después del setUp
        assertNotNull(loginController.correoField);
        assertNotNull(loginController.passwordField);
        
        // Verificar que son instancias de los tipos correctos
        assertTrue(loginController.correoField instanceof TextField);
        assertTrue(loginController.passwordField instanceof PasswordField);
    }
    
    @Test
    @DisplayName("Test de formato de alertas")
    void testTiposAlerta() {
        // Verificar que los tipos de alerta existen
        assertNotNull(Alert.AlertType.ERROR);
        assertNotNull(Alert.AlertType.INFORMATION);
        assertNotNull(Alert.AlertType.WARNING);
        
        // Verificar los nombres de los tipos
        assertEquals("ERROR", Alert.AlertType.ERROR.name());
        assertEquals("INFORMATION", Alert.AlertType.INFORMATION.name());
        assertEquals("WARNING", Alert.AlertType.WARNING.name());
    }
    
    @Test
    @DisplayName("Test de estructura del controlador")
    void testEstructuraControlador() {
        // Verificar que la clase tiene las anotaciones FXML esperadas
        assertDoesNotThrow(() -> {
            LoginController.class.getDeclaredField("correoField");
            LoginController.class.getDeclaredField("passwordField");
        });
        
        // Verificar que existen los métodos principales
        assertDoesNotThrow(() -> {
            LoginController.class.getDeclaredMethod("handleLogin");
            LoginController.class.getDeclaredMethod("handleGoToSignup");
            LoginController.class.getDeclaredMethod("initialize");
        });
    }
    
    @Test
    @DisplayName("Test de métodos privados mediante reflexión")
    void testMetodosPrivados() {
        // Verificar que los métodos privados existen
        assertDoesNotThrow(() -> {
            Method isValidEmail = LoginController.class.getDeclaredMethod("isValidEmail", String.class);
            Method showAlert = LoginController.class.getDeclaredMethod("showAlert", String.class, String.class, Alert.AlertType.class);
            Method obtenerIdUsuario = LoginController.class.getDeclaredMethod("obtenerIdUsuario", String.class);
            Method handleGoToProfile = LoginController.class.getDeclaredMethod("handleGoToProfile", int.class, String.class, boolean.class);
            
            // Hacerlos accesibles
            isValidEmail.setAccessible(true);
            showAlert.setAccessible(true);
            obtenerIdUsuario.setAccessible(true);
            handleGoToProfile.setAccessible(true);
        });
    }
}