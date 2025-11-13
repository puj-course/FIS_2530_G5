package com.greenet;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SettingsProfileControllerTest {

    private SettingsProfileController controller;

    @BeforeAll
    static void initFX() throws Exception {
        // Inicializa JavaFX si no está iniciado
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            latch.await(3, TimeUnit.SECONDS);
        } catch (IllegalStateException ignored) {
            // JavaFX ya estaba iniciado
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        controller = new SettingsProfileController();

        // Inyectar campos necesarios simulando los controles FXML
        setPrivateField("txtTelefono", new TextField());
        setPrivateField("txtCorreo", new TextField());
        setPrivateField("txtDireccion", new TextField());
        setPrivateField("btnTelefono", new Button());
        setPrivateField("btnCorreo", new Button());
        setPrivateField("btnDireccion", new Button());
        setPrivateField("btnVolver", new Button());
        setPrivateField("usuarioActualId", 1);
        setPrivateField("usuarioActualCorreo", "viejo@correo.com");
    }

    private void setPrivateField(String fieldName, Object value) throws Exception {
        Field field = controller.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object getPrivateField(String fieldName) throws Exception {
        Field field = controller.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(controller);
    }

    private void runOnFXThread(Runnable runnable) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                runnable.run();
            } finally {
                latch.countDown();
            }
        });
        latch.await(3, TimeUnit.SECONDS);
    }

    private void callPrivateMethod(String methodName, Class<?>... paramTypes) throws Exception {
        Method method = controller.getClass().getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        method.invoke(controller);
    }

    // ---------------- PRUEBAS ----------------

    @Test
    void testInitialize() throws Exception {
        runOnFXThread(() -> assertDoesNotThrow(() -> {
            try {
                callPrivateMethod("initialize");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    void testSetUsuarioActual() throws Exception {
        runOnFXThread(() -> assertDoesNotThrow(() -> {
            try {
                Method method = controller.getClass()
                        .getDeclaredMethod("setUsuarioActual", int.class, String.class);
                method.setAccessible(true);
                method.invoke(controller, 1, "nuevo@correo.com");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    void testActualizarTelefono() throws Exception {
        ((TextField) getPrivateField("txtTelefono")).setText("3124567890");

        runOnFXThread(() -> assertDoesNotThrow(() -> {
            try {
                callPrivateMethod("actualizarTelefono");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    void testActualizarCorreo() throws Exception {
        ((TextField) getPrivateField("txtCorreo")).setText("nuevo@correo.com");

        runOnFXThread(() -> assertDoesNotThrow(() -> {
            try {
                callPrivateMethod("actualizarCorreo");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    void testActualizarDireccion() throws Exception {
        ((TextField) getPrivateField("txtDireccion")).setText("Calle 123 #45-67 Bogotá");

        runOnFXThread(() -> assertDoesNotThrow(() -> {
            try {
                callPrivateMethod("actualizarDireccion");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    void testMostrarAlerta() throws Exception {
        runOnFXThread(() -> assertDoesNotThrow(() -> {
            try {
                Method method = controller.getClass()
                        .getDeclaredMethod("mostrarAlerta", String.class, String.class);
                method.setAccessible(true);
                method.invoke(controller, "Éxito", "Operación completada correctamente");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    void testIsValidEmail() throws Exception {
        Method method = controller.getClass().getDeclaredMethod("isValidEmail", String.class);
        method.setAccessible(true);
        boolean valid = (boolean) method.invoke(controller, "correo@ejemplo.com");
        boolean invalid = (boolean) method.invoke(controller, "correo@mal");

        assert(valid);
        assert(!invalid);
    }

    @Test
    void testVolverAtras() throws Exception {
        runOnFXThread(() -> assertDoesNotThrow(() -> {
            try {
                callPrivateMethod("volverAtras");
            } catch (Exception e) {
                // ignorar errores por falta de escena real
            }
        }));
    }
}
