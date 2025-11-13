package com.greenet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ✅ Pruebas unitarias para la clase Admin
 * Se usan clases falsas (stubs) para simular el envío de notificaciones
 * y evitar llamadas reales a Twilio, Gmail, etc.
 */
public class AdminTest {

    private Admin admin;
    private StringBuilder log;

    // ======== Clases internas falsas (stubs) =========

    class FakeNotificacion implements Notificacion {
        @Override
        public void sendMessage(String mensaje, String correo, long telefono) {
            log.append("[Base:" + mensaje + "]");
        }

        @Override
        public void operation() {
            log.append("[Operation]");
        }
    }

    class FakeWhatsApp extends FakeNotificacion {
        private final Notificacion next;

        public FakeWhatsApp(Notificacion next) {
            this.next = next;
        }

        @Override
        public void sendMessage(String mensaje, String correo, long telefono) {
            log.append("[WhatsApp]");
            next.sendMessage(mensaje, correo, telefono);
        }
    }

    class FakeGmail extends FakeNotificacion {
        private final Notificacion next;

        public FakeGmail(Notificacion next) {
            this.next = next;
        }

        @Override
        public void sendMessage(String mensaje, String correo, long telefono) {
            log.append("[Gmail]");
            next.sendMessage(mensaje, correo, telefono);
        }
    }

    class FakeSMS extends FakeNotificacion {
        private final Notificacion next;

        public FakeSMS(Notificacion next) {
            this.next = next;
        }

        @Override
        public void sendMessage(String mensaje, String correo, long telefono) {
            log.append("[SMS]");
            next.sendMessage(mensaje, correo, telefono);
        }
    }

    // ======== Clase especial para probar Admin sin dependencias externas =========
    class TestableAdmin extends Admin {
        public TestableAdmin(String nombre, String correo, long telefono) {
            super(nombre, correo, telefono);
        }

        @Override
        public void actualizar(String mensaje) {
            Notificacion fake = new FakeSMS(
                    new FakeGmail(
                            new FakeWhatsApp(
                                    new FakeNotificacion()
                            )
                    )
            );
            fake.sendMessage(mensaje, "fake@correo.com", 123456789L);
            fake.operation(); // Para cubrir también el método operation()
        }
    }

    // ======== Configuración antes de cada test =========
    @BeforeEach
    void setUp() {
        log = new StringBuilder();
        admin = new TestableAdmin("Mateo", "correo@prueba.com", 3100000000L);
    }

    // ======== Pruebas =========

    @Test
    void testActualizarEjecutaDecoradores() {
        admin.actualizar("Hola mundo");
        String salida = log.toString();
        assertTrue(salida.contains("SMS"));
        assertTrue(salida.contains("Gmail"));
        assertTrue(salida.contains("WhatsApp"));
        assertTrue(salida.contains("Hola mundo"));
        assertTrue(salida.contains("Operation"));
    }

    @Test
    void testActualizarMensajeVacio() {
        admin.actualizar("");
        String salida = log.toString();
        assertTrue(salida.contains("Base"));
        assertTrue(salida.contains("Operation"));
    }

    @Test
    void testActualizarTelefonoCero() {
        admin = new TestableAdmin("Mateo", "correo@prueba.com", 0);
        admin.actualizar("Teléfono 0");
        String salida = log.toString();
        assertTrue(salida.contains("SMS"));
        assertTrue(salida.contains("Base"));
    }

    @Test
    void testActualizarCorreoNulo() {
        admin = new TestableAdmin("Mateo", null, 3200000000L);
        admin.actualizar("Correo nulo");
        String salida = log.toString();
        assertTrue(salida.contains("Gmail"));
        assertTrue(salida.contains("Base"));
    }

    @Test
    void testActualizarMultiplesMensajes() {
        for (int i = 0; i < 3; i++) {
            admin.actualizar("Mensaje #" + i);
        }
        String salida = log.toString();
        assertTrue(salida.contains("Mensaje #2"));
        assertTrue(salida.contains("Operation"));
    }

    @Test
    void testActualizarNombreVacio() {
        admin = new TestableAdmin("", "test@mail.com", 12345L);
        admin.actualizar("Prueba nombre vacío");
        String salida = log.toString();
        assertTrue(salida.contains("SMS"));
    }

    @Test
    void testActualizarConCaracteresEspeciales() {
        admin.actualizar("¡Hola! ¿Qué tal? 😀");
        String salida = log.toString();
        assertTrue(salida.contains("Hola"));
        assertTrue(salida.contains("Operation"));
    }
}