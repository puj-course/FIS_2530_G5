package com.greenet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    private Admin admin;
    private String nombreEsperado;
    private String correoEsperado;
    private long telefonoEsperado;

    @BeforeEach
    void setUp() {
        // Datos de prueba
        nombreEsperado = "Juan Pérez";
        correoEsperado = "juan.perez@greenet.com";
        telefonoEsperado = 3001234567L;

        // Crear instancia de Admin
        admin = new Admin(nombreEsperado, correoEsperado, telefonoEsperado);
    }

    @Test
    void testConstructorAsignaValoresCorrectamente() {
        // Verificar que el constructor asigna los valores correctamente usando reflexión
        try {
            // Acceder al campo privado 'nombre'
            var nombreField = Admin.class.getDeclaredField("nombre");
            nombreField.setAccessible(true);
            String nombreActual = (String) nombreField.get(admin);
            assertEquals(nombreEsperado, nombreActual,
                    "El nombre debe ser asignado correctamente por el constructor");

            // Acceder al campo privado 'correo'
            var correoField = Admin.class.getDeclaredField("correo");
            correoField.setAccessible(true);
            String correoActual = (String) correoField.get(admin);
            assertEquals(correoEsperado, correoActual,
                    "El correo debe ser asignado correctamente por el constructor");

            // Acceder al campo privado 'telefono'
            var telefonoField = Admin.class.getDeclaredField("telefono");
            telefonoField.setAccessible(true);
            long telefonoActual = (long) telefonoField.get(admin);
            assertEquals(telefonoEsperado, telefonoActual,
                    "El teléfono debe ser asignado correctamente por el constructor");

        } catch (Exception e) {
            fail("Error al verificar los campos del Admin: " + e.getMessage());
        }
    }
    
    @Test
    void testAdminImplementaInterfazSuscribe() {
        // Verificar que Admin implementa la interfaz Suscribe
        assertTrue(admin instanceof Suscribe,
                "Admin debe implementar la interfaz Suscribe");

        // Verificar que puede ser usado como Suscribe
        Suscribe suscriptor = admin;
        assertNotNull(suscriptor,
                "Admin debe poder ser tratado como un objeto Suscribe");
    }

}
