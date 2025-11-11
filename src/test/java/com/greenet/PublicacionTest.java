package com.greenet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicacionTest {

    private Publicacion publicacion;

    // Implementación concreta para testing
    private static class PublicacionImpl implements Publicacion {
        private String titulo;
        private String descripcion;
        private String categoria;
        private String imagen;
        private int publicadorId;
        private List<String> etiquetas;

        public PublicacionImpl(String titulo, String descripcion, String categoria,
                               String imagen, int publicadorId, List<String> etiquetas) {
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.categoria = categoria;
            this.imagen = imagen;
            this.publicadorId = publicadorId;
            this.etiquetas = etiquetas;
        }

        @Override
        public String getTitulo() { return titulo; }

        @Override
        public String getDescripcion() { return descripcion; }

        @Override
        public String getCategoria() { return categoria; }

        @Override
        public String getImagen() { return imagen; }

        @Override
        public int getPublicadorId() { return publicadorId; }

        @Override
        public void aceptarVisita(VisitorPublicaciones visitor) {
            // Implementación básica para testing
        }

        @Override
        public List<String> getEtiquetas() { return etiquetas; }

        @Override
        public void setEtiquetas(List<String> etiquetas) {
            this.etiquetas = etiquetas;
        }
    }

    @BeforeEach
    void setUp() {
        List<String> etiquetas = Arrays.asList("ecología", "sostenible", "verde");
        publicacion = new PublicacionImpl(
                "Proyecto Verde",
                "Iniciativa de reciclaje comunitario",
                "Medio Ambiente",
                "imagen.jpg",
                101,
                etiquetas
        );
    }

    @Test
    void testGettersRetornanValoresCorrectos() {
        // Verificar que todos los getters retornan los valores correctos
        assertEquals("Proyecto Verde", publicacion.getTitulo());
        assertEquals("Iniciativa de reciclaje comunitario", publicacion.getDescripcion());
        assertEquals("Medio Ambiente", publicacion.getCategoria());
        assertEquals("imagen.jpg", publicacion.getImagen());
        assertEquals(101, publicacion.getPublicadorId());

        List<String> etiquetasEsperadas = Arrays.asList("ecología", "sostenible", "verde");
        assertEquals(etiquetasEsperadas, publicacion.getEtiquetas());
    }

    @Test
    void testSetEtiquetasActualizaCorrectamente() {
        // Crear nuevas etiquetas
        List<String> nuevasEtiquetas = Arrays.asList("reciclaje", "comunidad", "educación");

        // Actualizar etiquetas
        publicacion.setEtiquetas(nuevasEtiquetas);

        // Verificar que las etiquetas se actualizaron correctamente
        assertEquals(nuevasEtiquetas, publicacion.getEtiquetas());
        assertEquals(3, publicacion.getEtiquetas().size());
        assertTrue(publicacion.getEtiquetas().contains("reciclaje"));
        assertTrue(publicacion.getEtiquetas().contains("comunidad"));
        assertTrue(publicacion.getEtiquetas().contains("educación"));
    }
}
