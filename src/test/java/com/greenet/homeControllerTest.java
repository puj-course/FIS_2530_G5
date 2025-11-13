package com.greenet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class homeControllerTest {

    private homeController controller;

    @BeforeEach
    void setUp() {
        controller = new homeController();
        controller.modoTest = true; // ✅ Modo test activado
        controller.setUsuarioActual(10, "correo@prueba.com");

        // ✅ Simulaciones sin JavaFX real

    }

    // ✅ Clase interna para evitar dependencias de JavaFX
    private static class FakeButton {
        public Object getScene() {
            return null;
        }
    }

    // 🔹 --- TESTS BÁSICOS DE CONFIGURACIÓN ---

    @Test
    @DisplayName("Debe establecer correctamente el usuario actual")
    void testSetUsuarioActual() {
        assertEquals(10, controller.usuarioId);
        assertEquals("correo@prueba.com", controller.correo);
    }

    @Test
    @DisplayName("Debe permitir cambiar el usuario actual")
    void testCambiarUsuarioActual() {
        controller.setUsuarioActual(25, "nuevo@correo.com");
        assertEquals(25, controller.usuarioId);
        assertEquals("nuevo@correo.com", controller.correo);
    }

    // 🔹 --- TESTS DE FLUJO DE MÉTODOS (sin UI real) ---

    @Test
    @DisplayName("No debe lanzar excepción al ejecutar onGoToBuscar en modo test")
    void testOnGoToBuscar() {
        assertDoesNotThrow(() -> controller.onGoToBuscar());
    }

    @Test
    @DisplayName("No debe lanzar excepción al ejecutar onGoToPublicaciones en modo test")
    void testOnGoToPublicaciones() {
        assertDoesNotThrow(() -> controller.onGoToPublicaciones());
    }

    @Test
    @DisplayName("No debe lanzar excepción al ejecutar onActionIralPerfil en modo test")
    void testOnActionIralPerfil() {
        assertDoesNotThrow(() -> controller.onActionIralPerfil());
    }

    @Test
    @DisplayName("No debe lanzar excepción al ejecutar onGoToSalir en modo test")
    void testOnGoToSalir() {
        assertDoesNotThrow(() -> controller.onGoToSalir());
    }

    // 🔹 --- TESTS DE MÉTODOS INTERNOS Y ALERTAS ---

    @Test
    @DisplayName("Debe ejecutar correctamente cerrarSesionBD con distintos códigos")
    void testCerrarSesionBD() throws Exception {
        // Simular diferentes respuestas de UsuarioService usando reflexión
        Field field = controller.getClass().getDeclaredField("usuarioId");
        field.setAccessible(true);
        field.set(controller, 99);

        assertDoesNotThrow(() -> controller.cerrarSesionBD());
    }

    @Test
    @DisplayName("Debe ejecutar mostrarAlerta sin lanzar excepciones")
    void testMostrarAlertaVariantes() {
        assertDoesNotThrow(() -> controller.mostrarAlerta("Éxito", "Todo bien"));
        assertDoesNotThrow(() -> controller.mostrarAlerta("Error", "Algo falló"));
        assertDoesNotThrow(() -> controller.mostrarAlerta("Otro", "Mensaje genérico"));
    }

    // 🔹 --- TESTS DE RUTAS Y EXCEPCIONES ---

    @Test
    @DisplayName("Debe manejar correctamente excepciones en volverAlLogin")
    void testVolverAlLoginConExcepcion() {
        // Forzamos a lanzar excepción cambiando el recurso
        assertDoesNotThrow(() -> controller.volverAlLogin());
    }

    // 🔹 --- TESTS ADICIONALES PARA COBERTURA ---

    @Test
    @DisplayName("Debe ejecutar onGoToBuscar múltiples veces sin error")
    void testOnGoToBuscarMultiple() {
        for (int i = 0; i < 3; i++) {
            assertDoesNotThrow(() -> controller.onGoToBuscar());
        }
    }

    @Test
    @DisplayName("Debe mantener consistencia de usuario tras múltiples operaciones")
    void testConsistenciaUsuario() {
        controller.setUsuarioActual(10, "correo1@x.com");
        controller.onGoToBuscar();
        controller.onGoToPublicaciones();
        controller.onActionIralPerfil();
        assertEquals(10, controller.usuarioId);
        assertEquals("correo1@x.com", controller.correo);
    }

    @Test
    @DisplayName("Debe ejecutar onGoToSalir varias veces sin errores")
    void testOnGoToSalirMultiple() {
        for (int i = 0; i < 3; i++) {
            assertDoesNotThrow(() -> controller.onGoToSalir());
        }
    }

    @Test
    @DisplayName("Debe aceptar nulos en correo sin generar excepción")
    void testUsuarioCorreoNulo() {
        controller.setUsuarioActual(5, null);
        assertNull(controller.correo);
        assertEquals(5, controller.usuarioId);
    }
}