package com.greenet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;

class LoginControllerTest {
    
    @Test
    @DisplayName("Test de validación de email válido")
    void testIsValidEmail() throws Exception {
        LoginController loginController = new LoginController();
        Method isValidEmailMethod = LoginController.class.getDeclaredMethod("isValidEmail", String.class);
        isValidEmailMethod.setAccessible(true);
        
        // Emails válidos
        assertTrue((Boolean) isValidEmailMethod.invoke(loginController, "usuario@dominio.com"));
        assertTrue((Boolean) isValidEmailMethod.invoke(loginController, "test.email@domain.co"));
        assertTrue((Boolean) isValidEmailMethod.invoke(loginController, "user123@test.org"));
        assertTrue((Boolean) isValidEmailMethod.invoke(loginController, "a@b.co"));
        
        // Emails inválidos
        assertFalse((Boolean) isValidEmailMethod.invoke(loginController, "usuario"));
        assertFalse((Boolean) isValidEmailMethod.invoke(loginController, "usuario@"));
        assertFalse((Boolean) isValidEmailMethod.invoke(loginController, "@dominio.com"));
        assertFalse((Boolean) isValidEmailMethod.invoke(loginController, "usuario@dominio"));
        assertFalse((Boolean) isValidEmailMethod.invoke(loginController, ""));
        assertFalse((Boolean) isValidEmailMethod.invoke(loginController, " "));
    }
    
    @Test
    @DisplayName("Test de existencia de métodos privados")
    void testMetodosPrivadosExisten() {
        assertDoesNotThrow(() -> {
            Method isValidEmail = LoginController.class.getDeclaredMethod("isValidEmail", String.class);
            Method showAlert = LoginController.class.getDeclaredMethod("showAlert", String.class, String.class, Class.forName("javafx.scene.control.Alert$AlertType"));
            Method obtenerIdUsuario = LoginController.class.getDeclaredMethod("obtenerIdUsuario", String.class);
            Method handleGoToProfile = LoginController.class.getDeclaredMethod("handleGoToProfile", int.class, String.class, boolean.class);
            
            isValidEmail.setAccessible(true);
            showAlert.setAccessible(true);
            obtenerIdUsuario.setAccessible(true);
            handleGoToProfile.setAccessible(true);
        });
    }
    
    @Test
    @DisplayName("Test de estructura de la clase")
    void testEstructuraClase() {
        // Verificar que la clase tiene los campos esperados
        assertDoesNotThrow(() -> {
            LoginController.class.getDeclaredField("correoField");
            LoginController.class.getDeclaredField("passwordField");
        });
        
        // Verificar que existen los métodos públicos
        assertDoesNotThrow(() -> {
            LoginController.class.getDeclaredMethod("handleLogin");
            LoginController.class.getDeclaredMethod("handleGoToSignup");
            LoginController.class.getDeclaredMethod("initialize");
        });
    }
    
    @Test
    @DisplayName("Test de patrones de email complejos")
    void testPatronesEmailComplejos() throws Exception {
        LoginController loginController = new LoginController();
        Method isValidEmailMethod = LoginController.class.getDeclaredMethod("isValidEmail", String.class);
        isValidEmailMethod.setAccessible(true);
        
        // Casos con caracteres especiales
        assertTrue((Boolean) isValidEmailMethod.invoke(loginController, "user.name@domain.com"));
        assertTrue((Boolean) isValidEmailMethod.invoke(loginController, "user_name@domain.com"));
        assertTrue((Boolean) isValidEmailMethod.invoke(loginController, "user-name@domain.com"));
        assertTrue((Boolean) isValidEmailMethod.invoke(loginController, "user123@domain123.com"));
    }
    
    @Test
    @DisplayName("Test de regex del email")
    void testRegexEmail() throws Exception {
        LoginController loginController = new LoginController();
        Method isValidEmailMethod = LoginController.class.getDeclaredMethod("isValidEmail", String.class);
        isValidEmailMethod.setAccessible(true);
        
        // Verificar que el regex funciona correctamente
        String[] emailsValidos = {
            "test@example.com",
            "test.email@example.co.uk",
            "test123@example.org",
            "a@b.co"
        };
        
        String[] emailsInvalidos = {
            "test",
            "test@",
            "@example.com",
            "test@example.",
            "test@.com",
            "test example@domain.com"
        };
        
        for (String email : emailsValidos) {
            assertTrue((Boolean) isValidEmailMethod.invoke(loginController, email), 
                "El email debería ser válido: " + email);
        }
        
        for (String email : emailsInvalidos) {
            assertFalse((Boolean) isValidEmailMethod.invoke(loginController, email), 
                "El email debería ser inválido: " + email);
        }
    }
}
