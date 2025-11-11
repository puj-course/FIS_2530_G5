package com.greenet;

import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vista_Admin_BloqueosControllerTest {

    private Vista_Admin_BloqueosController controller;

    @BeforeEach
    void setUp() {
        // Inicializar el controlador
        controller = new Vista_Admin_BloqueosController();
    }

    @Test
    void testSetUsuarioActualAsignaIdCorrectamente() {
        // Datos de prueba
        int usuarioIdEsperado = 12345;

        // Ejecutar el método
        controller.setUsuarioActual(usuarioIdEsperado);

        // Verificar que el ID se asignó correctamente usando reflexión
        try {
            var usuarioIdField = Vista_Admin_BloqueosController.class.getDeclaredField("usuarioId");
            usuarioIdField.setAccessible(true);
            int usuarioIdActual = (int) usuarioIdField.get(controller);

            assertEquals(usuarioIdEsperado, usuarioIdActual,
                    "El ID del usuario debe ser asignado correctamente");
        } catch (Exception e) {
            fail("Error al verificar el usuarioId: " + e.getMessage());
        }
    }

    @Test
    void testControllerImplementaInterfazSuscribe() {
        // Verificar que el controlador implementa la interfaz Suscribe
        assertTrue(controller instanceof Suscribe,
                "El controlador debe implementar la interfaz Suscribe");
    }

    @Test
    void testControllerExtiendePublisher() {
        // Verificar que el controlador extiende de Publisher
        assertTrue(controller instanceof Publisher,
                "El controlador debe extender la clase Publisher");
    }
}
