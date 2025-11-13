package com.greenet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseDecoradorTest {

    static class NotificacionDummy implements Notificacion {
        boolean sendMessageLlamado = false;
        boolean operationLlamado = false;
        String mensaje;
        String correo;
        long telefono;

        @Override
        public void sendMessage(String message, String correo, long telefono) {
            sendMessageLlamado = true;
            this.mensaje = message;
            this.correo = correo;
            this.telefono = telefono;
        }

        @Override
        public void operation() {
            operationLlamado = true;
        }
    }

    // Subclase concreta de BaseDecorador para poder instanciarla
    static class DecoradorConcreto extends BaseDecorador {
        public DecoradorConcreto(Notificacion notifier) {
            super(notifier);
        }
    }

    private NotificacionDummy notificacionDummy;
    private DecoradorConcreto decorador;

    @BeforeEach
    void setUp() {
        notificacionDummy = new NotificacionDummy();
        decorador = new DecoradorConcreto(notificacionDummy);
    }

    @Test
    void testSendMessageLlamaAlWrappe() {
        decorador.sendMessage("Hola", "correo@ejemplo.com", 3105554433L);

        assertTrue(notificacionDummy.sendMessageLlamado);
        assertEquals("Hola", notificacionDummy.mensaje);
        assertEquals("correo@ejemplo.com", notificacionDummy.correo);
        assertEquals(3105554433L, notificacionDummy.telefono);
    }

    @Test
    void testOperationLlamaAlWrappe() {
        decorador.operation();

        assertTrue(notificacionDummy.operationLlamado, "Debe llamar al método operation() del wrappe");
    }

    @Test
    void testNoLanzaErroresSiWrappeEsNull() {
        DecoradorConcreto decoradorNulo = new DecoradorConcreto(null);

        assertDoesNotThrow(() -> decoradorNulo.sendMessage("Mensaje", "mail@test.com", 3112223344L));
        assertDoesNotThrow(decoradorNulo::operation);
    }
}