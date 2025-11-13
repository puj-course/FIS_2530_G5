package com.greenet;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class NotificacionBaseTest {

    @Test
    void testSendMessageNoLanzaErrores() {
        NotificacionBase notificacion = new NotificacionBase();

        assertDoesNotThrow(() ->
                notificacion.sendMessage("Mensaje de prueba", "correo@ejemplo.com", 3105556677L)
        );
    }

    @Test
    void testOperationImprimeMensajeCorrecto() {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(salida));

        try {
            NotificacionBase notificacion = new NotificacionBase();
            notificacion.operation();

            String salidaConsola = salida.toString().trim();
            assertEquals("Operación base ejecutada", salidaConsola);
        } finally {
            System.setOut(originalOut);
        }
    }
}