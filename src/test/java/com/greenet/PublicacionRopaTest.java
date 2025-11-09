package com.greenet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

class PublicacionRopaTest {
    
    private PublicacionRopa publicacion;
    
    @BeforeEach
    void setUp() {
        publicacion = new PublicacionRopa(
            "Camiseta Deportiva",
            "Camiseta de algodón orgánico en excelente estado",
            "imagen_camiseta.jpg",
            123,
            10.5f,
            "Algodón orgánico"
        );
    }
    
    @Test
    void testConstructorYGetters() {
        assertEquals("Camiseta Deportiva", publicacion.getTitulo());
        assertEquals("Camiseta de algodón orgánico en excelente estado", publicacion.getDescripcion());
        assertEquals("imagen_camiseta.jpg", publicacion.getImagen());
        assertEquals(123, publicacion.getPublicadorId());
        assertEquals(10.5f, publicacion.getTalla(), 0.001);
        assertEquals("Algodón orgánico", publicacion.getMaterial());
        assertEquals("Ropa", publicacion.getCategoria());
    }
    
    @Test
    void testEtiquetas() {
        assertTrue(publicacion.getEtiquetas().isEmpty());
        
        List<String> etiquetas = Arrays.asList("Sostenible", "Segunda mano");
        publicacion.setEtiquetas(etiquetas);
        
        assertEquals(2, publicacion.getEtiquetas().size());
        assertTrue(publicacion.getEtiquetas().contains("Sostenible"));
    }
    
    @Test
    void testAceptarVisita() {
        VisitorPublicaciones visitor = new VisitorPublicaciones() {
            @Override
            public void visitar(PublicacionRopa pub) {
                assertEquals(publicacion, pub);
            }
            
            @Override
            public void visitar(PublicacionHogar hogar) {}
            
            @Override
            public void visitar(PublicacionTecnologia tecnologia) {}
        };
        
        assertDoesNotThrow(() -> publicacion.aceptarVisita(visitor));
    }
    
    @Test
    void testSetNombreUsuario() {
        assertDoesNotThrow(() -> publicacion.setNombreUsuario("Usuario Test"));
    }
}
