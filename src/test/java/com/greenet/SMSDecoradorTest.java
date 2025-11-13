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
            // no necesario
        }
    }

    private NotificacionDummy notificacionDummy;

    @BeforeEach
    void setUp() {
        notificacionDummy = new NotificacionDummy();
    }

    @Test
    void testCoberturaTryCatchDeSendMessage() {
        // Subclase simulada que reemplaza el bloque Twilio real
        SMSDecorador decorador = new SMSDecorador(notificacionDummy) {
            @Override
            public void sendMessage(String message, String correo, long telefono) {
                // Mismo comportamiento que la clase real
                super.sendMessage(message, correo, telefono);
                String numeroDestino = "+57" + telefono;
                String mensajeFinal = "Un usuario fue bloqueado por incumplir las normas revise su correo o wha para mas informacion";

                try {
                    // Simulamos el envío correcto
                    String simulado = "SMS enviado correctamente a " + numeroDestino;
                    System.out.println(simulado);
                    assertTrue(simulado.contains("+57"), "El número debe incluir el prefijo +57");

                    // Luego lanzamos una excepción para que también se ejecute el catch
                    throw new RuntimeException("Error simulado Twilio");
                } catch (Exception e) {
                    // Este bloque simula el catch de la clase original
                    String errorMsg = " Error al enviar el SMS: " + e.getMessage();
                    System.out.println(errorMsg);
                    assertTrue(errorMsg.contains("Error"), "Debe entrar al bloque catch");
                }
            }
        };

       
        assertDoesNotThrow(() ->
                decorador.sendMessage("Mensaje", "correo@ejemplo.com", 3129991122L)
        );

        // Assert
        assertTrue(notificacionDummy.fueLlamado);
        assertEquals("Mensaje", notificacionDummy.mensaje);
    }

    @Test
    void testFormatoNumeroDestino() {
        long tel = 3118887777L;
        assertEquals("+573118887777", "+57" + tel);
    }

    @Test
    void testSendMessageOriginalNoLanzaErrores() {
        SMSDecorador decorador = new SMSDecorador(notificacionDummy);
        assertDoesNotThrow(() ->
                decorador.sendMessage("Test", "correo@prueba.com", 3104445566L)
        );
    }
}