package com.example.padron_decorador_modificado;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class NotificacionBaseTest {

    private NotificacionBase notificacionBase;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        notificacionBase = new NotificacionBase();
    }

    @Test
    void testSendMessageNoLanzaExcepcion() {
        // Arrange (Preparar)
        String mensaje = "Mensaje de prueba";
        String correo = "test@example.com";
        long telefono = 3001234567L;

        // Act & Assert (Actuar y Verificar)
        assertDoesNotThrow(() -> {
            notificacionBase.sendMessage(mensaje, correo, telefono);
        }, "El método sendMessage no debería lanzar ninguna excepción");
    }

    @Test
    void testOperationImprimeMensajeCorrecto() {
        // Arrange (Preparar)
        System.setOut(new PrintStream(outputStream));
        String mensajeEsperado = "Operación base ejecutada";

        // Act (Actuar)
        notificacionBase.operation();

        // Assert (Verificar)
        String salidaConsola = outputStream.toString().trim();
        assertEquals(mensajeEsperado, salidaConsola,
                "El método operation debería imprimir el mensaje correcto");

        // Restaurar System.out original
        System.setOut(originalOut);
    }


    @Test
    void testSendMessageConParametrosNulosNoLanzaExcepcion() {
        // Arrange (Preparar)
        String mensajeNulo = null;
        String correoNulo = null;
        long telefono = 0L;

        // Act & Assert (Actuar y Verificar) - Prueba Negativa
        assertDoesNotThrow(() -> {
            notificacionBase.sendMessage(mensajeNulo, correoNulo, telefono);
        }, "El método sendMessage debería manejar parámetros nulos sin lanzar excepción");
    }


}
