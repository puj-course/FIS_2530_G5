package com.greenet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class GmailDecoradorTest {

   
    static class NotificacionDummy implements Notificacion {
        boolean fueLlamado = false;
        String mensaje;
        String correo;
        long telefono;

        @Override
        public void sendMessage(String message, String correo, long telefono) {
            fueLlamado = true;
            this.mensaje = message;
            this.correo = correo;
            this.telefono = telefono;
        }

        @Override
        public void operation() {
            // no necesario para este test
        }
    }

    private GmailDecorador gmailDecorador;
    private NotificacionDummy notificacionDummy;

    @BeforeEach
    void setUp() {
        notificacionDummy = new NotificacionDummy();
        gmailDecorador = new GmailDecorador(notificacionDummy);
    }

    @Test
    void testSendMessageLlamaASuper() {
        gmailDecorador.sendMessage("Hola", "correo@prueba.com", 123L);

        assertTrue(notificacionDummy.fueLlamado);
        assertEquals("Hola", notificacionDummy.mensaje);
        assertEquals("correo@prueba.com", notificacionDummy.correo);
        assertEquals(123L, notificacionDummy.telefono);
    }

    @Test
    void testEnviarCorreoPorReflection() throws Exception {

        String destino = "correo@prueba.com";
        String asunto = "Top secret";
        String cuerpo = "Mensaje de prueba";

        Method metodoPrivado = GmailDecorador.class.getDeclaredMethod("enviarCorreo", String.class, String.class, String.class);

       
        metodoPrivado.setAccessible(true);


        assertDoesNotThrow(() -> metodoPrivado.invoke(gmailDecorador, destino, asunto, cuerpo));
    }

    @Test
    void testSendMessageNoLanzaErrores() {
        assertDoesNotThrow(() ->
                gmailDecorador.sendMessage("Mensaje", "correo@prueba.com", 321L)
        );
    }
}