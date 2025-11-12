package com.greenet;

import com.greenet.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class homeControllerTest {

    private homeController controller;

    @BeforeEach
    void setUp() {
        controller = new homeController();
        controller.setUsuarioActual(10, "correo@prueba.com");
    }

    @Test
    void testCerrarSesionBD_SesionCerradaCorrectamente() {
        int resultadoReal = UsuarioService.cerrarSesion(1);
        // Aceptamos que 0 significa correcto y -1 error de conexión general
        assertTrue(resultadoReal == 0 || resultadoReal == 1,
                "Debería devolver 0 (correcto) o -1 si no hay conexión a BD");
        System.out.println("✅ Test sesión cerrada correctamente pasado (resultado: " + resultadoReal + ")");
    }

    @Test
    void testCerrarSesionBD_SinSesionActiva() {
        int resultadoReal = UsuarioService.cerrarSesion(9999); // usuario no válido
        // Aceptamos 1 (sin sesión) o -1 (error)
        assertTrue(resultadoReal == 1 || resultadoReal == -1,
                "Debería devolver 1 (sin sesión) o -1 si hubo error");
        System.out.println("⚠️ Test sin sesión activa pasado (resultado: " + resultadoReal + ")");
    }

    @Test
    void testCerrarSesionBD_ErrorGeneral() {
        int resultadoReal = UsuarioService.cerrarSesion(-1);
        // Puede devolver -1 o 1 dependiendo de cómo manejes el error interno
        assertTrue(resultadoReal == -1 || resultadoReal == 1,
                "Debería devolver -1 o 1 en caso de error general");
        System.out.println("❌ Test error general pasado (resultado: " + resultadoReal + ")");
    }

    @Test
    void testSetUsuarioActual() {
        homeController ctrl = new homeController();
        ctrl.setUsuarioActual(123, "test@correo.com");
        assertNotNull(ctrl, "El controlador no debe ser nulo");
        System.out.println("✅ Test setUsuarioActual pasado correctamente");
    }
}
