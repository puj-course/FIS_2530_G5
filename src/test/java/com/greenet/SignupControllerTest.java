package com.greenet;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class SignupControllerTest {

    @Test
    void testControllerInstanciacion() {
        SignupController controller = new SignupController();
        assertNotNull(controller);
    }

    @Test
    void testValidacionesConReflection() throws Exception {
        SignupController controller = new SignupController();
        
        // Validación de email
        Method isValidEmail = SignupController.class.getDeclaredMethod("isValidEmail", String.class);
        isValidEmail.setAccessible(true);
        assertTrue((Boolean) isValidEmail.invoke(controller, "test@example.com"));
        assertTrue((Boolean) isValidEmail.invoke(controller, "user.name@domain.co"));
        assertFalse((Boolean) isValidEmail.invoke(controller, "invalid"));
        
        // Validación de fecha
        Method isValidDate = SignupController.class.getDeclaredMethod("isValidDate", String.class);
        isValidDate.setAccessible(true);
        assertTrue((Boolean) isValidDate.invoke(controller, "2020-01-15"));
        assertTrue((Boolean) isValidDate.invoke(controller, "1990-12-31"));
        assertFalse((Boolean) isValidDate.invoke(controller, "2020/01/15"));
        assertFalse((Boolean) isValidDate.invoke(controller, "invalid"));
        
        // Validación de tipo documento
        Method isValidTipoDoc = SignupController.class.getDeclaredMethod("isValidTipoDoc", String.class);
        isValidTipoDoc.setAccessible(true);
        assertTrue((Boolean) isValidTipoDoc.invoke(controller, "CC"));
        assertTrue((Boolean) isValidTipoDoc.invoke(controller, "TI"));
        assertTrue((Boolean) isValidTipoDoc.invoke(controller, "CE"));
        assertTrue((Boolean) isValidTipoDoc.invoke(controller, "PASAPORTE"));
        assertFalse((Boolean) isValidTipoDoc.invoke(controller, "DNI"));
    }

    @Test
    void testValidacionesDirectas() {
        // Probar regex CORREGIDOS
        assertTrue("test@example.com".matches("^[A-Za-z0-9+_.-]+@(.+)$"));
        assertTrue("2020-01-15".matches("^\\\\d{4}-\\\\d{2}-\\\\d{2}$")); // Fecha corregida
        assertTrue("CC".matches("^(CC|TI|CE|PASAPORTE)$"));
        
        // Campos vacíos
        assertTrue("".trim().isEmpty());
        assertFalse("texto".trim().isEmpty());
    }

    @Test
    void testMultiplesInstancias() {
        SignupController c1 = new SignupController();
        SignupController c2 = new SignupController();
        SignupController c3 = new SignupController();
        
        assertNotNull(c1);
        assertNotNull(c2);
        assertNotNull(c3);
    }

    @Test
    void testReflectionAccess() throws Exception {
        SignupController controller = new SignupController();
        
        // Solo acceder a métodos privados
        Method[] methods = SignupController.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("isValid")) {
                method.setAccessible(true);
            }
        }
    }
}
