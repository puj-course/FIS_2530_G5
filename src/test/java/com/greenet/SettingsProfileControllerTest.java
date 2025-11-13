package com.greenet;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsProfileControllerTest {

    private SettingsProfileController controller;

    @BeforeAll
    static void initFX() throws Exception {
        // Evita re-inicializar JavaFX si ya está corriendo
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            latch.await(3, TimeUnit.SECONDS);
        } catch (IllegalStateException e) {
            // JavaFX ya está iniciado, ignorar
        }
    }

    @BeforeEach
    void setUp() {
        controller = new SettingsProfileController();
    }

    private void runOnFXThread(Runnable action) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });
        latch.await(3, TimeUnit.SECONDS);
    }

    private void callMethod(String methodName, Class<?>[] paramTypes, Object... args) throws Exception {
        Method method = controller.getClass().getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        method.invoke(controller, args);
    }

    @Test
    void testSetUsuarioActualNoLanzaExcepcion() throws Exception {
        runOnFXThread(() -> assertDoesNotThrow(() -> {
            try {
                callMethod("setUsuarioActual", new Class[]{int.class, String.class}, 1, "correo@ejemplo.com");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    void testMostrarAlertaNoLanzaExcepcion() throws Exception {
        runOnFXThread(() -> assertDoesNotThrow(() -> {
            try {
                callMethod("mostrarAlerta", new Class[]{String.class, String.class}, "Título", "Mensaje de prueba");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    void testVolverAtrasNoLanzaExcepcion() throws Exception {
        runOnFXThread(() -> assertDoesNotThrow(() -> {
            try {
                callMethod("volverAtras", new Class[]{});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    void testGuardarCambiosNoLanzaExcepcion() throws Exception {
        runOnFXThread(() -> assertDoesNotThrow(() -> {
            try {
                callMethod("guardarCambios", new Class[]{});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
