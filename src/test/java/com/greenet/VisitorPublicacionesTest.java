package com.greenet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class VisitorPublicacionesTest {

    private PublicacionRopa publicacionRopa;
    private PublicacionHogar publicacionHogar;
    private PublicacionTecnologia publicacionTecnologia;

    @BeforeEach
    void setUp() {
        publicacionRopa = new PublicacionRopa(
                "Camiseta Deportiva",
                "Camiseta de algodón orgánico",
                "imagen.jpg",
                123,
                10.5f,
                "Algodón"
        );

        publicacionHogar = new PublicacionHogar(
                "Sofá Vintage",
                "Sofá en buen estado",
                "sofa.jpg",
                456,
                "Mueble de sala"
        );

        publicacionTecnologia = new PublicacionTecnologia(
                "Laptop Dell",
                "Laptop en excelente estado",
                "laptop.jpg",
                789,
                "Inspiron 15",
                "Dell",
                true
        );
    }

    @Test
    void testVisitorRopaEsLlamado() {
        ContadorVisitor visitor = new ContadorVisitor();

        publicacionRopa.aceptarVisita(visitor);

        assertEquals(1, visitor.getContadorRopa());
        assertEquals(0, visitor.getContadorHogar());
        assertEquals(0, visitor.getContadorTecnologia());
    }

    @Test
    void testVisitorHogarEsLlamado() {
        ContadorVisitor visitor = new ContadorVisitor();

        publicacionHogar.aceptarVisita(visitor);

        assertEquals(0, visitor.getContadorRopa());
        assertEquals(1, visitor.getContadorHogar());
        assertEquals(0, visitor.getContadorTecnologia());
    }

    @Test
    void testVisitorTecnologiaEsLlamado() {
        ContadorVisitor visitor = new ContadorVisitor();

        publicacionTecnologia.aceptarVisita(visitor);

        assertEquals(0, visitor.getContadorRopa());
        assertEquals(0, visitor.getContadorHogar());
        assertEquals(1, visitor.getContadorTecnologia());
    }

    @Test
    void testVisitorMultiplesPublicaciones() {
        ContadorVisitor visitor = new ContadorVisitor();

        publicacionRopa.aceptarVisita(visitor);
        publicacionRopa.aceptarVisita(visitor);
        publicacionHogar.aceptarVisita(visitor);
        publicacionTecnologia.aceptarVisita(visitor);
        publicacionTecnologia.aceptarVisita(visitor);
        publicacionTecnologia.aceptarVisita(visitor);

        assertEquals(2, visitor.getContadorRopa());
        assertEquals(1, visitor.getContadorHogar());
        assertEquals(3, visitor.getContadorTecnologia());
    }

    @Test
    void testVisitorRecibePublicacionCorrecta() {
        CapturadorVisitor visitor = new CapturadorVisitor();

        publicacionRopa.aceptarVisita(visitor);

        assertNotNull(visitor.getUltimaRopa());
        assertEquals(publicacionRopa, visitor.getUltimaRopa());
        assertNull(visitor.getUltimoHogar());
        assertNull(visitor.getUltimaTecnologia());
    }

    @Test
    void testVisitorProcesaDatosRopa() {
        ExtractorDatosVisitor visitor = new ExtractorDatosVisitor();

        publicacionRopa.aceptarVisita(visitor);

        assertEquals("Camiseta Deportiva", visitor.getUltimoTitulo());
        assertEquals("Ropa", visitor.getUltimaCategoria());
    }

    @Test
    void testVisitorProcesaDatosHogar() {
        ExtractorDatosVisitor visitor = new ExtractorDatosVisitor();

        publicacionHogar.aceptarVisita(visitor);

        assertEquals("Sofá Vintage", visitor.getUltimoTitulo());
        assertEquals("Hogar", visitor.getUltimaCategoria());
    }

    @Test
    void testVisitorProcesaDatosTecnologia() {
        ExtractorDatosVisitor visitor = new ExtractorDatosVisitor();

        publicacionTecnologia.aceptarVisita(visitor);

        assertEquals("Laptop Dell", visitor.getUltimoTitulo());
        assertEquals("Tecnología", visitor.getUltimaCategoria());
    }

    @Test
    void testVisitorAtributosEspecificosRopa() {
        ExtractorAtributosVisitor visitor = new ExtractorAtributosVisitor();

        publicacionRopa.aceptarVisita(visitor);

        assertEquals("Talla: 10.5, Material: Algodón", visitor.getAtributosRopa());
    }
    
    @Test
    void testVisitorAtributosEspecificosTecnologia() {
        ExtractorAtributosVisitor visitor = new ExtractorAtributosVisitor();

        publicacionTecnologia.aceptarVisita(visitor);

        assertEquals("Modelo: Inspiron 15, Marca: Dell, Garantía: true", visitor.getAtributosTecnologia());
    }

    @Test
    void testVisitorNoModificaPublicacion() {
        String tituloOriginal = publicacionRopa.getTitulo();
        String descripcionOriginal = publicacionRopa.getDescripcion();

        ContadorVisitor visitor = new ContadorVisitor();
        publicacionRopa.aceptarVisita(visitor);

        assertEquals(tituloOriginal, publicacionRopa.getTitulo());
        assertEquals(descripcionOriginal, publicacionRopa.getDescripcion());
    }

    @Test
    void testMismVisitorVariasVeces() {
        ContadorVisitor visitor = new ContadorVisitor();

        for (int i = 0; i < 5; i++) {
            publicacionRopa.aceptarVisita(visitor);
        }

        assertEquals(5, visitor.getContadorRopa());
    }

    // ===== Clases auxiliares para las pruebas =====

    // Visitor que cuenta cuántas veces se visita cada tipo
    private class ContadorVisitor implements VisitorPublicaciones {
        private int contadorRopa = 0;
        private int contadorHogar = 0;
        private int contadorTecnologia = 0;

        @Override
        public void visitar(PublicacionRopa ropa) {
            contadorRopa++;
        }

        @Override
        public void visitar(PublicacionHogar hogar) {
            contadorHogar++;
        }

        @Override
        public void visitar(PublicacionTecnologia tecnologia) {
            contadorTecnologia++;
        }

        public int getContadorRopa() { return contadorRopa; }
        public int getContadorHogar() { return contadorHogar; }
        public int getContadorTecnologia() { return contadorTecnologia; }
    }

    // Visitor que captura la última publicación visitada
    private class CapturadorVisitor implements VisitorPublicaciones {
        private PublicacionRopa ultimaRopa;
        private PublicacionHogar ultimoHogar;
        private PublicacionTecnologia ultimaTecnologia;

        @Override
        public void visitar(PublicacionRopa ropa) {
            this.ultimaRopa = ropa;
        }

        @Override
        public void visitar(PublicacionHogar hogar) {
            this.ultimoHogar = hogar;
        }

        @Override
        public void visitar(PublicacionTecnologia tecnologia) {
            this.ultimaTecnologia = tecnologia;
        }

        public PublicacionRopa getUltimaRopa() { return ultimaRopa; }
        public PublicacionHogar getUltimoHogar() { return ultimoHogar; }
        public PublicacionTecnologia getUltimaTecnologia() { return ultimaTecnologia; }
    }

    // Visitor que extrae datos comunes
    private class ExtractorDatosVisitor implements VisitorPublicaciones {
        private String ultimoTitulo;
        private String ultimaCategoria;

        @Override
        public void visitar(PublicacionRopa ropa) {
            this.ultimoTitulo = ropa.getTitulo();
            this.ultimaCategoria = ropa.getCategoria();
        }

        @Override
        public void visitar(PublicacionHogar hogar) {
            this.ultimoTitulo = hogar.getTitulo();
            this.ultimaCategoria = hogar.getCategoria();
        }

        @Override
        public void visitar(PublicacionTecnologia tecnologia) {
            this.ultimoTitulo = tecnologia.getTitulo();
            this.ultimaCategoria = tecnologia.getCategoria();
        }

        public String getUltimoTitulo() { return ultimoTitulo; }
        public String getUltimaCategoria() { return ultimaCategoria; }
    }

    // Visitor que extrae atributos específicos
    private class ExtractorAtributosVisitor implements VisitorPublicaciones {
        private String atributosRopa;
        private String atributosHogar;
        private String atributosTecnologia;

        @Override
        public void visitar(PublicacionRopa ropa) {
            this.atributosRopa = "Talla: " + ropa.getTalla() +
                    ", Material: " + ropa.getMaterial();
        }

        public void visitar(PublicacionHogar hogar) {
            this.atributosHogar = "Tipo de Mueble: " + hogar.getTipoMueble();
        }

        @Override
        public void visitar(PublicacionTecnologia tecnologia) {
            this.atributosTecnologia = "Modelo: " + tecnologia.getModelo() +
                    ", Marca: " + tecnologia.getMarca() +
                    ", Garantía: " + tecnologia.isGarantia();
        }

        public String getAtributosRopa() { return atributosRopa; }
        public String getAtributosHogar() { return atributosHogar; }
        public String getAtributosTecnologia() { return atributosTecnologia; }
    }
}
