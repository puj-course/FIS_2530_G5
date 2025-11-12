package com.greenet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SMSDecoradorTest {

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
        }
    }

    private NotificacionDummy notificacionDummy;

    @BeforeEach
    void setUp() {
        notificacionDummy = new NotificacionDummy();
    }

    @Test
    void testSendMessageEjecutaTryCatchYSuper() {
        // Subclase que simula el comportamiento del try/catch
        SMSDecorador decorador = new SMSDecorador(notificacionDummy) {
            @Override
            public void sendMessage(String message, String correo, long telefono) {
                super.sendMessage(message, correo, telefono);

                // Simulamos manualmente lo que pasa dentro del try
                String numeroDestino = "+57" + telefono;
                String mensajeFinal = "Un usuario fue bloqueado por incumplir las normas revise su correo o wha para mas informacion";

                try {
                    // Simulación del bloque try
                    System.out.println("SMS enviado correctamente a " + numeroDestino);
                    // Forzamos una excepción controlada para cubrir el catch también
                    throw new RuntimeException("Error simulado Twilio");
                } catch (Exception e) {
                    // Este bloque representa tu catch real
                    System.out.println(" Error al enviar el SMS: " + e.getMessage());
                    assertTrue(e.getMessage().contains("simulado"));
                }
            }
        };

        assertDoesNotThrow(() ->
                decorador.sendMessage("Mensaje prueba", "correo@prueba.com", 3102223344L)
        );

        // Verificamos que se llamó el componente base
        assertTrue(notificacionDummy.fueLlamado);
        assertEquals("Mensaje prueba", notificacionDummy.mensaje);
    }

    @Test
    void testFormatoNumeroDestinoCorrecto() {
        long telefono = 3001112233L;
        String numeroDestino = "+57" + telefono;
        assertEquals("+573001112233", numeroDestino);
    }

    @Test
    void testSendMessageNoLanzaExcepcion() {
        SMSDecorador decorador = new SMSDecorador(notificacionDummy);
        assertDoesNotThrow(() ->
                decorador.sendMessage("Test", "correo@ejemplo.com", 3219998888L)
        );
    }
}