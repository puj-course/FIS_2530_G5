package com.example.padron_decorador_modificado;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.example.greenet.Publisher;
import com.example.greenet.Suscribe;

class PublisherTest {

    private Publisher publisher;
    private SuscribeMock suscriptor1;
    private SuscribeMock suscriptor2;

    @BeforeEach
    void setUp() {
        // Creamos una implementación concreta de Publisher para probar
        publisher = new PublisherConcreto();
        suscriptor1 = new SuscribeMock();
        suscriptor2 = new SuscribeMock();
    }

    @Test
    void testNotificarSubsLlamaActualizarEnTodosSuscriptores() {
        // Arrange (Preparar)
        publisher.suscribir(suscriptor1);
        publisher.suscribir(suscriptor2);
        String mensaje = "Mensaje de prueba";

        // Act (Actuar)
        ((PublisherConcreto) publisher).notificarSubsPublico(mensaje);

        // Assert (Verificar)
        assertTrue(suscriptor1.fueActualizado,
                "El suscriptor 1 debería haber sido actualizado");
        assertTrue(suscriptor2.fueActualizado,
                "El suscriptor 2 debería haber sido actualizado");
        assertEquals(mensaje, suscriptor1.mensajeRecibido,
                "El suscriptor 1 debería recibir el mensaje correcto");
        assertEquals(mensaje, suscriptor2.mensajeRecibido,
                "El suscriptor 2 debería recibir el mensaje correcto");
    }

    // Clase Mock para simular un Suscribe
    private static class SuscribeMock implements Suscribe {
        boolean fueActualizado = false;
        String mensajeRecibido = null;

        @Override
        public void actualizar(String mensaje) {
            this.fueActualizado = true;
            this.mensajeRecibido = mensaje;
        }
    }
   //clase para instanciar un publisher en concreto
   class PublisherConcreto extends Publisher {
    public void notificarSubsPublico(String mensaje) {
        super.notificarSubs(mensaje);
    }
   }
}
