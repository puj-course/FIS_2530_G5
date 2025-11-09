package com.greenet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

class PublicacionTecnologiaTest {
    
    private PublicacionTecnologia publicacion;
    
    @BeforeEach
    void setUp() {
        publicacion = new PublicacionTecnologia(
            "Laptop Dell",
            "Laptop en excelente estado con 16GB RAM",
            "laptop.jpg",
            456,
            "Inspiron 15",
            "Dell",
            true
        );
    }
    
    @Test
    void testConstructorYGetters() {
        assertEquals("Laptop Dell", publicacion.getTitulo());
        assertEquals("Laptop en excelente estado con 16GB RAM", publicacion.getDescripcion());
        assertEquals("laptop.jpg", publicacion.getImagen());
        assertEquals(456, publicacion.getPublicadorId());
        assertEquals("Inspiron 15", publicacion.getModelo());
        assertEquals("Dell", publicacion.getMarca());
        assertTrue(publicacion.isGarantia());
        assertEquals("Tecnología", publicacion.getCategoria());
    }
    
    @Test
    void testEtiquetas() {
        assertTrue(publicacion.getEtiquetas().isEmpty());
        
        List<String> etiquetas = Arrays.asList("Portátil", "Gaming", "Alta gama");
        publicacion.setEtiquetas(etiquetas);
        
        assertEquals(3, publicacion.getEtiquetas().size());
        assertTrue(publicacion.getEtiquetas().contains("Gaming"));
    }
    
    @Test
    void testGarantiaFalse() {
        PublicacionTecnologia sinGarantia = new PublicacionTecnologia(
            "Mouse",
            "Mouse óptico",
            "mouse.jpg",
            789,
            "M185",
            "Logitech",
            false
        );
        
        assertFalse(sinGarantia.isGarantia());
    }
    
    @Test
    void testAceptarVisita() {
        VisitorPublicaciones visitor = new VisitorPublicaciones() {
            @Override
            public void visitar(PublicacionTecnologia pub) {
                assertEquals(publicacion, pub);
            }
            
            @Override
            public void visitar(PublicacionRopa ropa) {}
            
            @Override
            public void visitar(PublicacionHogar hogar) {}
        };
        
        assertDoesNotThrow(() -> publicacion.aceptarVisita(visitor));
    }
    
    @Test
    void testSetNombreUsuario() {
        assertDoesNotThrow(() -> publicacion.setNombreUsuario("Usuario Test"));
    }
}
