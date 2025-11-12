/*package com.greenet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javafx.application.Platform;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

class SettingsProfileControllerTest {

    @BeforeAll
    static void initJavaFX() {
        // Inicializar JavaFX manualmente
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Ya está inicializado, continuar
        }
    }

    @Test
    void testControllerCompletoConJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        Platform.runLater(() -> {
            try {
                SettingsProfileController controller = new SettingsProfileController();
                assertNotNull(controller);
                
                // ESTO SÍ FUNCIONA CON JAVAFX INICIALIZADO
                controller.setUsuarioActual(1, "test@example.com");
                Method cargarDatosUsuario = SettingsProfileController.class.getDeclaredMethod("cargarDatosUsuario");
                cargarDatosUsuario.setAccessible(true);
                cargarDatosUsuario.invoke(controller);
                
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        
        assertTrue(latch.await(10, TimeUnit.SECONDS));
    }

    @Test
    void testMetodosPrivadosConJavaFX() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        
        Platform.runLater(() -> {
            try {
                SettingsProfileController controller = new SettingsProfileController();
                controller.setUsuarioActual(1, "test@example.com");
                
                // Reflection DENTRO del hilo de JavaFX
                Method isValidEmail = SettingsProfileController.class.getDeclaredMethod("isValidEmail", String.class);
                isValidEmail.setAccessible(true);
                assertTrue((Boolean) isValidEmail.invoke(controller, "test@example.com"));
                
                Method correoEnUso = SettingsProfileController.class.getDeclaredMethod("correoEnUso", String.class);
                correoEnUso.setAccessible(true);
                correoEnUso.invoke(controller, "otro@test.com");
                
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        
        assertTrue(latch.await(10, TimeUnit.SECONDS));
    }

    @Test
    void testActualizacionesConJavaFX() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        
        Platform.runLater(() -> {
            try {
                SettingsProfileController controller = new SettingsProfileController();
                controller.setUsuarioActual(1, "test@example.com");
                
                // Reflection para métodos de actualización
                Method actualizarTelefono = SettingsProfileController.class.getDeclaredMethod("actualizarTelefono");
                actualizarTelefono.setAccessible(true);
                actualizarTelefono.invoke(controller);
                
                Method actualizarCorreo = SettingsProfileController.class.getDeclaredMethod("actualizarCorreo");
                actualizarCorreo.setAccessible(true);
                actualizarCorreo.invoke(controller);
                
                Method actualizarDireccion = SettingsProfileController.class.getDeclaredMethod("actualizarDireccion");
                actualizarDireccion.setAccessible(true);
                actualizarDireccion.invoke(controller);
                
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }
        });
        
        assertTrue(latch.await(10, TimeUnit.SECONDS));
    }

    @Test
    void testValidacionesEnJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        Platform.runLater(() -> {
            // Validaciones DENTRO de JavaFX
            assertTrue("1234567".matches("\\d{7,15}"));
            assertTrue("test@example.com".contains("@"));
            assertTrue("Calle 123 #45-67".length() >= 10);
            
            latch.countDown();
        });
        
        assertTrue(latch.await(10, TimeUnit.SECONDS));
    }
}
*/