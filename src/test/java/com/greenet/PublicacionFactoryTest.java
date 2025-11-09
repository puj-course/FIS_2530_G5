package com.greenet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

class PublicacionFactoryTest {

    private PublicacionFactory factory;

    @BeforeEach
    void setUp() {
        factory = new PublicacionFactory();
    }

    // ===== Pruebas de crearPublicacionTecnologia =====

    @Test
    void testCrearPublicacionTecnologia() {
        Publicacion publicacion = factory.crearPublicacionTecnologia(
                "Laptop HP",
                "Laptop en buen estado",
                "laptop.jpg",
                100,
                "Pavilion",
                "HP",
                true
        );

        assertNotNull(publicacion);
        assertInstanceOf(PublicacionTecnologia.class, publicacion);
        assertEquals("Laptop HP", publicacion.getTitulo());
        assertEquals("Laptop en buen estado", publicacion.getDescripcion());
        assertEquals("laptop.jpg", publicacion.getImagen());
        assertEquals(100, publicacion.getPublicadorId());
        assertEquals("Tecnología", publicacion.getCategoria());

        PublicacionTecnologia tech = (PublicacionTecnologia) publicacion;
        assertEquals("Pavilion", tech.getModelo());
        assertEquals("HP", tech.getMarca());
        assertTrue(tech.isGarantia());
    }

    @Test
    void testCrearPublicacionTecnologiaSinGarantia() {
        Publicacion publicacion = factory.crearPublicacionTecnologia(
                "Mouse Logitech",
                "Mouse usado",
                "mouse.jpg",
                200,
                "M185",
                "Logitech",
                false
        );

        PublicacionTecnologia tech = (PublicacionTecnologia) publicacion;
        assertFalse(tech.isGarantia());
    }

    // ===== Pruebas de crearPublicacionRopa =====

    @Test
    void testCrearPublicacionRopa() {
        Publicacion publicacion = factory.crearPublicacionRopa(
                "Camiseta Nike",
                "Camiseta deportiva",
                "camiseta.jpg",
                150,
                10.5f,
                "Algodón"
        );

        assertNotNull(publicacion);
        assertInstanceOf(PublicacionRopa.class, publicacion);
        assertEquals("Camiseta Nike", publicacion.getTitulo());
        assertEquals("Camiseta deportiva", publicacion.getDescripcion());
        assertEquals("camiseta.jpg", publicacion.getImagen());
        assertEquals(150, publicacion.getPublicadorId());
        assertEquals("Ropa", publicacion.getCategoria());

        PublicacionRopa ropa = (PublicacionRopa) publicacion;
        assertEquals(10.5f, ropa.getTalla(), 0.001);
        assertEquals("Algodón", ropa.getMaterial());
    }

    @Test
    void testCrearPublicacionRopaConTallaEntera() {
        Publicacion publicacion = factory.crearPublicacionRopa(
                "Pantalón",
                "Pantalón de mezclilla",
                "pantalon.jpg",
                175,
                12.0f,
                "Denim"
        );

        PublicacionRopa ropa = (PublicacionRopa) publicacion;
        assertEquals(12.0f, ropa.getTalla(), 0.001);
    }

    // ===== Pruebas de crearPublicacionHogar =====

    @Test
    void testCrearPublicacionHogar() {
        Publicacion publicacion = factory.crearPublicacionHogar(
                "Sofá Moderno",
                "Sofá de tres puestos",
                "sofa.jpg",
                180,
                "Mueble de sala"
        );

        assertNotNull(publicacion);
        assertInstanceOf(PublicacionHogar.class, publicacion);
        assertEquals("Sofá Moderno", publicacion.getTitulo());
        assertEquals("Sofá de tres puestos", publicacion.getDescripcion());
        assertEquals("sofa.jpg", publicacion.getImagen());
        assertEquals(180, publicacion.getPublicadorId());
        assertEquals("Hogar", publicacion.getCategoria());

        PublicacionHogar hogar = (PublicacionHogar) publicacion;
        assertEquals("Mueble de sala", hogar.getTipoMueble());
    }

    // ===== Pruebas del método crearPublicacion con lista de parámetros =====

    @Test
    void testCrearPublicacionTecnologiaConLista() {
        List<String> parametros = Arrays.asList("ThinkPad", "Lenovo", "true");

        Publicacion publicacion = factory.crearPublicacion(
                "Tecnología",
                "Laptop Lenovo",
                "Laptop profesional",
                "lenovo.jpg",
                250,
                parametros
        );

        assertInstanceOf(PublicacionTecnologia.class, publicacion);
        PublicacionTecnologia tech = (PublicacionTecnologia) publicacion;
        assertEquals("ThinkPad", tech.getModelo());
        assertEquals("Lenovo", tech.getMarca());
        assertTrue(tech.isGarantia());
    }

    @Test
    void testCrearPublicacionTecnologiaMinuscula() {
        List<String> parametros = Arrays.asList("iPhone 13", "Apple", "false");

        Publicacion publicacion = factory.crearPublicacion(
                "tecnologia",
                "iPhone 13",
                "Teléfono seminuevo",
                "iphone.jpg",
                300,
                parametros
        );

        assertInstanceOf(PublicacionTecnologia.class, publicacion);
    }

    @Test
    void testCrearPublicacionRopaConLista() {
        List<String> parametros = Arrays.asList("8.5", "Poliéster");

        Publicacion publicacion = factory.crearPublicacion(
                "Ropa",
                "Chamarra",
                "Chamarra de invierno",
                "chamarra.jpg",
                275,
                parametros
        );

        assertInstanceOf(PublicacionRopa.class, publicacion);
        PublicacionRopa ropa = (PublicacionRopa) publicacion;
        assertEquals(8.5f, ropa.getTalla(), 0.001);
        assertEquals("Poliéster", ropa.getMaterial());
    }

    @Test
    void testCrearPublicacionRopaMinuscula() {
        List<String> parametros = Arrays.asList("10.0", "Lana");

        Publicacion publicacion = factory.crearPublicacion(
                "ropa",
                "Suéter",
                "Suéter tejido",
                "sueter.jpg",
                285,
                parametros
        );

        assertInstanceOf(PublicacionRopa.class, publicacion);
    }

    @Test
    void testCrearPublicacionHogarConLista() {
        List<String> parametros = Arrays.asList("Mesa de comedor");

        Publicacion publicacion = factory.crearPublicacion(
                "Hogar",
                "Mesa de madera",
                "Mesa para 6 personas",
                "mesa.jpg",
                320,
                parametros
        );

        assertInstanceOf(PublicacionHogar.class, publicacion);
        PublicacionHogar hogar = (PublicacionHogar) publicacion;
        assertEquals("Mesa de comedor", hogar.getTipoMueble());
    }

    @Test
    void testCrearPublicacionHogarMinuscula() {
        List<String> parametros = Arrays.asList("Escritorio");

        Publicacion publicacion = factory.crearPublicacion(
                "hogar",
                "Escritorio moderno",
                "Escritorio para oficina",
                "escritorio.jpg",
                330,
                parametros
        );

        assertInstanceOf(PublicacionHogar.class, publicacion);
    }

    // ===== Pruebas de excepciones =====

    @Test
    void testCrearPublicacionCategoriaInvalida() {
        List<String> parametros = Arrays.asList("param1", "param2");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.crearPublicacion(
                    "Deportes",
                    "Balón",
                    "Balón de fútbol",
                    "balon.jpg",
                    400,
                    parametros
            );
        });

        assertTrue(exception.getMessage().contains("Categoría no soportada"));
        assertTrue(exception.getMessage().contains("Deportes"));
    }

    @Test
    void testCrearPublicacionCategoriaVacia() {
        List<String> parametros = Arrays.asList("param1");

        assertThrows(IllegalArgumentException.class, () -> {
            factory.crearPublicacion(
                    "",
                    "Título",
                    "Descripción",
                    "imagen.jpg",
                    450,
                    parametros
            );
        });
    }

    @Test
    void testCrearPublicacionTecnologiaParametrosInsuficientes() {
        List<String> parametros = Arrays.asList("Modelo"); // Faltan marca y garantía

        assertThrows(IndexOutOfBoundsException.class, () -> {
            factory.crearPublicacion(
                    "Tecnología",
                    "Tablet",
                    "Tablet Android",
                    "tablet.jpg",
                    500,
                    parametros
            );
        });
    }

    @Test
    void testCrearPublicacionRopaParametrosInsuficientes() {
        List<String> parametros = Arrays.asList("10.5"); // Falta material

        assertThrows(IndexOutOfBoundsException.class, () -> {
            factory.crearPublicacion(
                    "Ropa",
                    "Zapatos",
                    "Zapatos deportivos",
                    "zapatos.jpg",
                    550,
                    parametros
            );
        });
    }

    @Test
    void testCrearPublicacionHogarParametrosInsuficientes() {
        List<String> parametros = new ArrayList<>(); // Lista vacía

        assertThrows(IndexOutOfBoundsException.class, () -> {
            factory.crearPublicacion(
                    "Hogar",
                    "Silla",
                    "Silla de oficina",
                    "silla.jpg",
                    600,
                    parametros
            );
        });
    }

    @Test
    void testCrearPublicacionRopaTallaInvalida() {
        List<String> parametros = Arrays.asList("no_es_numero", "Algodón");

        assertThrows(NumberFormatException.class, () -> {
            factory.crearPublicacion(
                    "Ropa",
                    "Falda",
                    "Falda larga",
                    "falda.jpg",
                    650,
                    parametros
            );
        });
    }

    @Test
    void testCrearPublicacionTecnologiaGarantiaInvalida() {
        List<String> parametros = Arrays.asList("Modelo X", "Marca Y", "no_es_boolean");

        // Boolean.parseBoolean retorna false para cualquier string que no sea "true"
        Publicacion publicacion = factory.crearPublicacion(
                "Tecnología",
                "Producto",
                "Descripción",
                "img.jpg",
                700,
                parametros
        );

        PublicacionTecnologia tech = (PublicacionTecnologia) publicacion;
        assertFalse(tech.isGarantia()); // parseBoolean("no_es_boolean") = false
    }

    // ===== Pruebas de casos especiales =====

    @Test
    void testCrearPublicacionConTituloVacio() {
        Publicacion publicacion = factory.crearPublicacionHogar(
                "",
                "Descripción válida",
                "imagen.jpg",
                800,
                "Decoración"
        );

        assertEquals("", publicacion.getTitulo());
    }

    @Test
    void testCrearPublicacionConIdCero() {
        Publicacion publicacion = factory.crearPublicacionRopa(
                "Título",
                "Descripción",
                "imagen.jpg",
                0,
                10.0f,
                "Material"
        );

        assertEquals(0, publicacion.getPublicadorId());
    }

    @Test
    void testCrearPublicacionConIdNegativo() {
        Publicacion publicacion = factory.crearPublicacionTecnologia(
                "Título",
                "Descripción",
                "imagen.jpg",
                -1,
                "Modelo",
                "Marca",
                true
        );

        assertEquals(-1, publicacion.getPublicadorId());
    }

    @Test
    void testFactoryImplementaAbstractFactory() {
        assertInstanceOf(AbstractFactoryPublicacion.class, factory);
    }
}
