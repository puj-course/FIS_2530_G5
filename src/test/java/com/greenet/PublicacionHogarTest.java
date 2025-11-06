package com.greenet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


class PublicacionHogarTest {

    private PublicacionHogar publicacion;

    @BeforeEach
    void setUp() {
        publicacion = new PublicacionHogar(
                "Silla de madera",
                "Silla clásica para comedor",
                "imagen.jpg",
                101,
                "Silla"
        );
    }

    @Test
    void testConstructorYGetters() {
        assertEquals("Silla de madera", publicacion.getTitulo());
        assertEquals("Silla clásica para comedor", publicacion.getDescripcion());
        assertEquals("imagen.jpg", publicacion.getImagen());
        assertEquals("Hogar", publicacion.getCategoria()); // Categoría fija
        assertEquals(101, publicacion.getPublicadorId());
        assertEquals("Silla", publicacion.getTipoMueble());
        assertTrue(publicacion.getEtiquetas().isEmpty());
    }

    @Test
    void testSetEtiquetas() {
        List<String> etiquetas = Arrays.asList("madera", "artesanal", "nuevo");
        publicacion.setEtiquetas(etiquetas);

        assertEquals(3, publicacion.getEtiquetas().size());
        assertTrue(publicacion.getEtiquetas().contains("madera"));
    }



    @Test
    void testSetNombreUsuarioNoLanzaErrores() {

        assertDoesNotThrow(() -> publicacion.setNombreUsuario("Mateo Zamora"));
    }
}
