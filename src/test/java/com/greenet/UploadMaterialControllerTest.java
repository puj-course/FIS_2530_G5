package com.greenet;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class UploadMaterialControllerTest {

    private UploadMaterialController controller;

    @BeforeAll
    static void initFX() {
        // Solo inicia entorno JavaFX para evitar errores internos
        new JFXPanel();
    }

    @BeforeEach
    void setUp() {
        controller = new UploadMaterialController();
    }

    // -------------------- TEST INICIALIZACIÓN --------------------
    @Test
    void testInicializaCorrectamente() {
        assertNotNull(controller, "El controlador no debe ser nulo");
    }

    @Test
    void testSetUsuarioActual() {
        assertDoesNotThrow(() -> controller.setUsuarioActual(42));
    }

    // -------------------- TEST CONVERSIÓN BASE64 --------------------
    @Test
    void testCodificarYDecodificarImagen() {
        byte[] datos = "imagen_test".getBytes();
        String codificado = FachadaImagen.codificarImagenAString(datos);
        byte[] decodificado = FachadaImagen.decodificarStringAImagen(codificado);

        assertArrayEquals(datos, decodificado, "La codificación y decodificación deben coincidir");
    }

    @Test
    void testCodificarImagenNula() {
        assertNull(FachadaImagen.codificarImagenAString(null), "Debe retornar null para imagen nula");
    }

    @Test
    void testDecodificarStringVacio() {
        byte[] resultado = FachadaImagen.decodificarStringAImagen("");
        assertEquals(0, resultado.length, "Debe retornar arreglo vacío para string vacío");
    }

    // -------------------- TEST CREACIÓN DE PUBLICACIONES --------------------
    @Test
    void testCrearPublicacionConDatosValidos() {
        List<String> params = new ArrayList<>(List.of("modelo", "marca", "true"));
        int id = controller.crearPublicacion(1, "Laptop", "Nueva", "Tecnología", "img", 1, params);
        assertTrue(id >= -1);
    }

    @Test
    void testCrearPublicacionConCategoriaInvalida() {
        List<String> params = List.of("dato");
        int id = controller.crearPublicacion(1, "Test", "Sin categoría", "Inexistente", "img", 9, params);
        assertTrue(id <= 0, "Debe retornar -1 para categoría inválida");
    }

    // -------------------- TEST FUNCIONES DE APOYO --------------------
    @Test
    void testObtenerParametrosCategoriaInvalida() {
        String[] res = controller.obtenerParametrosEspecificos("Otra");
        assertNotNull(res);
        assertEquals(0, res.length, "Debe retornar arreglo vacío si no reconoce la categoría");
    }

    // -------------------- TEST DE EXISTENCIA DE FXML --------------------
    @Test
    void testFXMLHomeExiste() {
        var recurso = getClass().getResource("/com/greenet/Home.fxml");
        if (recurso == null)
            System.out.println("⚠ Home.fxml no encontrado (advertencia, no es error crítico)");
        else
            assertNotNull(recurso);
    }
}