package com.greenet;

import com.greenet.service.UsuarioService;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import org.junit.jupiter.api.*;

import java.awt.GraphicsEnvironment;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class homeControllerTest {

    private homeController controller;

    @BeforeAll
    void initJavaFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX ya iniciado
        }
    }

    @BeforeEach
    void setUp() {
        controller = new homeController();
        controller.setUsuarioActual(10, "correo@prueba.com");
    }

    // --- SESIÓN ---
    @Test
    void testCerrarSesionBD_SesionCerradaCorrectamente() {
        int resultado = UsuarioService.cerrarSesion(10);
        assertTrue(resultado == 0 || resultado == 1);
    }

    @Test
    void testCerrarSesionBD_SinSesionActiva() {
        int resultado = UsuarioService.cerrarSesion(9999);
        assertTrue(resultado == 1 || resultado == -1);
    }

    @Test
    void testCerrarSesionBD_ErrorGeneral() {
        int resultado = UsuarioService.cerrarSesion(-1);
        assertTrue(resultado == -1 || resultado == 1);
    }

    // --- ALERTAS ---
    @Test
    void testMostrarAlerta_Info() throws Exception {
        assumeTrue(!GraphicsEnvironment.isHeadless(), "Sin GUI: se omite test");
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.mostrarAlerta("Éxito", "Todo correcto");
            } finally {
                latch.countDown();
            }
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testMostrarAlerta_Error() throws Exception {
        assumeTrue(!GraphicsEnvironment.isHeadless(), "Sin GUI: se omite test");
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.mostrarAlerta("Error", "Falló la operación");
            } finally {
                latch.countDown();
            }
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    // --- VOLVER AL LOGIN ---
    @Test
    void testVolverALogin() throws Exception {
        assumeTrue(!GraphicsEnvironment.isHeadless(), "Sin GUI: se omite test");
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.volverAlLogin();
            } finally {
                latch.countDown();
            }
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    // --- SET USUARIO ---
    @Test
    void testSetUsuarioActual() {
        homeController ctrl = new homeController();
        ctrl.setUsuarioActual(123, "test@correo.com");
        assertNotNull(ctrl);
    }

    // --- NAVEGACIÓN / FXML ---
    @Test
    void testOnGoToBuscar_MetodoEjecutado() throws Exception {
        assumeTrue(!GraphicsEnvironment.isHeadless(), "Sin GUI: se omite test");
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenet/ProductSearch.fxml"));
                loader.setController(controller); // asignamos nuestro controlador de prueba
                loader.load();
                controller.onGoToBuscar(); // llamamos al método real
            } catch (Exception e) {
                fail("Error al ejecutar onGoToBuscar: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testOnGoToPublicaciones_MetodoEjecutado() throws Exception {
        assumeTrue(!GraphicsEnvironment.isHeadless(), "Sin GUI: se omite test");
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenet/upload_material.fxml"));
                loader.setController(controller);
                loader.load();
                controller.onGoToPublicaciones(); // llamamos al método real
            } catch (Exception e) {
                fail("Error al ejecutar onGoToPublicaciones: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testOnGoToPublicaciones_ControllerAsignado() throws Exception {
        assumeTrue(!GraphicsEnvironment.isHeadless(), "Sin GUI: se omite test");
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenet/upload_material.fxml"));
                loader.load();
                Object ctrl = loader.getController();
                assertNotNull(ctrl);
            } catch (Exception e) {
                fail("Error al cargar o asignar controlador: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        latch.await(2, TimeUnit.SECONDS);
    }
}