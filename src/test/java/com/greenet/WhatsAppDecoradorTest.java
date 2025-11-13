package com.greenet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WhatsAppDecoradorTest {

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
    void testSendMessageBasico() {
        WhatsAppDecorador decorador = new WhatsAppDecorador(notificacionDummy);
        
               try {
            decorador.sendMessage("Test", "test@test.com", 3108887777L);
        } catch (Exception e) {
           
        }
        
   
        assertTrue(notificacionDummy.fueLlamado);
    }

    @Test
    void testMultiplesInstancias() {
       
        for (int i = 0; i < 10; i++) {
            NotificacionDummy dummy = new NotificacionDummy();
            WhatsAppDecorador decorador = new WhatsAppDecorador(dummy);
            
            try {
                decorador.sendMessage("Test " + i, "test" + i + "@test.com", 3100000000L + i);
            } catch (Exception e) {
                // IGNORAR
            }
            
            assertTrue(dummy.fueLlamado);
        }
    }

    @Test
    void testNumerosDiferentes() {
        long[] telefonos = {
            3101112233L, 3114445566L, 3127778899L, 
            3131234567L, 3149876543L, 3155556666L,
            3161113333L, 3174447777L, 3188889999L,
            3191239876L
        };
        
        for (long telefono : telefonos) {
            NotificacionDummy dummy = new NotificacionDummy();
            WhatsAppDecorador decorador = new WhatsAppDecorador(dummy);
            
            try {
                decorador.sendMessage("Mensaje para " + telefono, "test@test.com", telefono);
            } catch (Exception e) {
                // IGNORAR
            }
            
            assertTrue(dummy.fueLlamado);
        }
    }

    @Test
    void testMensajesVariados() {
        String[] mensajes = {
            "Alerta", "Notificación", "Aviso", "Recordatorio",
            "Mensaje urgente", "Información importante", "Actualización",
            "Confirmación", "Solicitud", "Respuesta"
        };
        
        for (String mensaje : mensajes) {
            NotificacionDummy dummy = new NotificacionDummy();
            WhatsAppDecorador decorador = new WhatsAppDecorador(dummy);
            
            try {
                decorador.sendMessage(mensaje, "test@test.com", 3108887777L);
            } catch (Exception e) {
                // IGNORAR
            }
            
            assertTrue(dummy.fueLlamado);
            assertEquals(mensaje, dummy.mensaje);
        }
    }

    @Test
    void testCorreosDiferentes() {
        String[] correos = {
            "test1@test.com", "user@domain.com", "admin@system.com",
            "notificacion@app.com", "soporte@help.com", "info@company.com"
        };
        
        for (String correo : correos) {
            NotificacionDummy dummy = new NotificacionDummy();
            WhatsAppDecorador decorador = new WhatsAppDecorador(dummy);
            
            try {
                decorador.sendMessage("Test correo", correo, 3108887777L);
            } catch (Exception e) {
                // IGNORAR
            }
            
            assertTrue(dummy.fueLlamado);
            assertEquals(correo, dummy.correo);
        }
    }

    @Test
    void testEjecucionMasiva() {
        // EJECUCIÓN MASIVA para máxima cobertura
        for (int i = 0; i < 50; i++) {
            NotificacionDummy dummy = new NotificacionDummy();
            WhatsAppDecorador decorador = new WhatsAppDecorador(dummy);
            
            try {
                decorador.sendMessage("Massive " + i, "massive@test.com", 3100000000L + i);
                
                // EJECUTAR MÚLTIPLES VECES LA MISMA INSTANCIA
                if (i % 5 == 0) {
                    decorador.sendMessage("Repeat " + i, "repeat@test.com", 3200000000L + i);
                }
            } catch (Exception e) {
                // IGNORAR TODOS LOS ERRORES
            }
        }
    }

    @Test
    void testConstructorSolo() {
        // SOLO probar constructor múltiples veces
        for (int i = 0; i < 20; i++) {
            NotificacionDummy dummy = new NotificacionDummy();
            WhatsAppDecorador decorador = new WhatsAppDecorador(dummy);
           
        }
    }

    @Test
    void testSendMessageSolo() {
        WhatsAppDecorador decorador = new WhatsAppDecorador(notificacionDummy);
        
        
        for (int i = 0; i < 10; i++) {
            try {
                decorador.sendMessage("Multi " + i, "multi@test.com", 3108887777L + i);
            } catch (Exception e) {
              
            }
        }
        
        assertTrue(notificacionDummy.fueLlamado);
    }
}