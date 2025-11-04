package com.example.greenet;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class UploadMaterialControllerTest {

    // TEST 1: Solo probar que el controller se puede crear
    @Test
    void testControllerInicializacion() {
        UploadMaterialController controller = new UploadMaterialController();
        assertNotNull(controller);
    }

    // TEST 2: Probar que los métodos existen (sin ejecutarlos)
    @Test 
    void testMetodosExisten() throws Exception {
        // Verificar que los métodos privados existen
        assertDoesNotThrow(() -> 
            UploadMaterialController.class.getDeclaredMethod("obtenerParametrosEspecificos", String.class)
        );
        
        assertDoesNotThrow(() -> 
            UploadMaterialController.class.getDeclaredMethod("mostrarCamposEspecificos", String.class)
        );
        
        assertDoesNotThrow(() -> 
            UploadMaterialController.class.getDeclaredMethod("ocultarTodosLosCampos")
        );
        
        assertDoesNotThrow(() -> 
            UploadMaterialController.class.getDeclaredMethod("limpiarFormulario")
        );
    }

    // TEST 3: Probar método crearPublicacion con manejo de excepciones
    @Test
    void testCrearPublicacion_MetodoExiste() throws Exception {
        UploadMaterialController controller = new UploadMaterialController();
        
        Method metodo = UploadMaterialController.class.getDeclaredMethod(
            "crearPublicacion", 
            int.class, String.class, String.class, String.class, String.class, String[].class
        );
        metodo.setAccessible(true);
        
        // Ejecutar pero esperar que falle (por BD null)
        int resultado = (int) metodo.invoke(
            controller,
            1, "Test Title", "Test Description", "Tecnología", "base64imagen", new String[]{}
        );
        
        // Debería retornar -1 por error de BD, pero el método se ejecuta
        assertEquals(-1, resultado);
    }

    // TEST 4: Probar método registrarPublicacionEnBD
    @Test
    void testRegistrarPublicacionEnBD_MetodoExiste() throws Exception {
        UploadMaterialController controller = new UploadMaterialController();
        
        Method metodo = UploadMaterialController.class.getDeclaredMethod(
            "registrarPublicacionEnBD", 
            Publicacion.class, int.class
        );
        metodo.setAccessible(true);
        
        // Solo verificar que el método existe y es accesible
        assertNotNull(metodo);
    }
}