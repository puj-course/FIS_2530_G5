package com.greenet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class AbstractFactoryPublicacionTest {
    
    private AbstractFactoryPublicacion factory;
    
    @BeforeEach
    void setUp() {
        // Usamos la implementación concreta para probar la interfaz
        factory = new PublicacionFactory();
    }
    
    // ===== Pruebas del contrato de la interfaz =====
    
    @Test
    void testFactoryPuedeCrearPublicacionTecnologia() {
        Publicacion publicacion = factory.crearPublicacionTecnologia(
            "Monitor Samsung",
            "Monitor 24 pulgadas",
            "monitor.jpg",
            100,
            "S24F350",
            "Samsung",
            true
        );
        
        assertNotNull(publicacion);
        assertInstanceOf(Publicacion.class, publicacion);
    }
    
    @Test
    void testFactoryPuedeCrearPublicacionRopa() {
        Publicacion publicacion = factory.crearPublicacionRopa(
            "Pantalón Levi's",
            "Pantalón de mezclilla",
            "pantalon.jpg",
            200,
            32.0f,
            "Denim"
        );
        
        assertNotNull(publicacion);
        assertInstanceOf(Publicacion.class, publicacion);
    }
    
    @Test
    void testFactoryPuedeCrearPublicacionHogar() {
        Publicacion publicacion = factory.crearPublicacionHogar(
            "Lámpara de pie",
            "Lámpara moderna",
            "lampara.jpg",
            300,
            "Iluminación"
        );
        
        assertNotNull(publicacion);
        assertInstanceOf(Publicacion.class, publicacion);
    }
    
    // ===== Pruebas de que los métodos retornan el tipo correcto =====
    
    @Test
    void testCrearPublicacionTecnologiaRetornaPublicacion() {
        Publicacion publicacion = factory.crearPublicacionTecnologia(
            "Teclado",
            "Teclado mecánico",
            "teclado.jpg",
            150,
            "K380",
            "Logitech",
            false
        );
        
        // Verificamos que retorna una instancia de Publicacion
        assertTrue(publicacion instanceof Publicacion);
    }
    
    @Test
    void testCrearPublicacionRopaRetornaPublicacion() {
        Publicacion publicacion = factory.crearPublicacionRopa(
            "Vestido",
            "Vestido casual",
            "vestido.jpg",
            250,
            8.0f,
            "Algodón"
        );
        
        assertTrue(publicacion instanceof Publicacion);
    }
    
    @Test
    void testCrearPublicacionHogarRetornaPublicacion() {
        Publicacion publicacion = factory.crearPublicacionHogar(
            "Espejo",
            "Espejo decorativo",
            "espejo.jpg",
            350,
            "Decoración"
        );
        
        assertTrue(publicacion instanceof Publicacion);
    }
    
    // ===== Pruebas de consistencia del contrato =====
    
    @Test
    void testTodosLosMetodosAceptanTituloDescripcionImagen() {
        // Verificamos que todos los métodos aceptan los parámetros comunes
        
        Publicacion tech = factory.crearPublicacionTecnologia(
            "Titulo Tech", "Desc Tech", "img_tech.jpg", 1, "Modelo", "Marca", true
        );
        assertEquals("Titulo Tech", tech.getTitulo());
        assertEquals("Desc Tech", tech.getDescripcion());
        assertEquals("img_tech.jpg", tech.getImagen());
        
        Publicacion ropa = factory.crearPublicacionRopa(
            "Titulo Ropa", "Desc Ropa", "img_ropa.jpg", 2, 10.0f, "Material"
        );
        assertEquals("Titulo Ropa", ropa.getTitulo());
        assertEquals("Desc Ropa", ropa.getDescripcion());
        assertEquals("img_ropa.jpg", ropa.getImagen());
        
        Publicacion hogar = factory.crearPublicacionHogar(
            "Titulo Hogar", "Desc Hogar", "img_hogar.jpg", 3, "Tipo"
        );
        assertEquals("Titulo Hogar", hogar.getTitulo());
        assertEquals("Desc Hogar", hogar.getDescripcion());
        assertEquals("img_hogar.jpg", hogar.getImagen());
    }
    
    @Test
    void testTodosLosMetodosAceptanPublicadorId() {
        Publicacion tech = factory.crearPublicacionTecnologia(
            "T", "D", "I", 111, "M", "Marca", true
        );
        assertEquals(111, tech.getPublicadorId());
        
        Publicacion ropa = factory.crearPublicacionRopa(
            "T", "D", "I", 222, 10.0f, "Mat"
        );
        assertEquals(222, ropa.getPublicadorId());
        
        Publicacion hogar = factory.crearPublicacionHogar(
            "T", "D", "I", 333, "Tipo"
        );
        assertEquals(333, hogar.getPublicadorId());
    }
    
    // ===== Pruebas de categorías =====
    
    @Test
    void testPublicacionTecnologiaTieneCategoriaCorrecta() {
        Publicacion publicacion = factory.crearPublicacionTecnologia(
            "Producto", "Desc", "img.jpg", 1, "Modelo", "Marca", true
        );
        
        assertEquals("Tecnología", publicacion.getCategoria());
    }
    
    @Test
    void testPublicacionRopaTieneCategoriaCorrecta() {
        Publicacion publicacion = factory.crearPublicacionRopa(
            "Producto", "Desc", "img.jpg", 1, 10.0f, "Material"
        );
        
        assertEquals("Ropa", publicacion.getCategoria());
    }
    
    @Test
    void testPublicacionHogarTieneCategoriaCorrecta() {
        Publicacion publicacion = factory.crearPublicacionHogar(
            "Producto", "Desc", "img.jpg", 1, "Tipo"
        );
        
        assertEquals("Hogar", publicacion.getCategoria());
    }
    
    // ===== Pruebas de parámetros específicos =====
    
    @Test
    void testCrearPublicacionTecnologiaConParametrosEspecificos() {
        Publicacion publicacion = factory.crearPublicacionTecnologia(
            "iPhone 14",
            "Smartphone",
            "iphone.jpg",
            500,
            "iPhone 14 Pro",
            "Apple",
            true
        );
        
        assertNotNull(publicacion);
        PublicacionTecnologia tech = (PublicacionTecnologia) publicacion;
        assertEquals("iPhone 14 Pro", tech.getModelo());
        assertEquals("Apple", tech.getMarca());
        assertTrue(tech.isGarantia());
    }
    
    @Test
    void testCrearPublicacionRopaConParametrosEspecificos() {
        Publicacion publicacion = factory.crearPublicacionRopa(
            "Camisa",
            "Camisa formal",
            "camisa.jpg",
            600,
            15.5f,
            "Seda"
        );
        
        assertNotNull(publicacion);
        PublicacionRopa ropa = (PublicacionRopa) publicacion;
        assertEquals(15.5f, ropa.getTalla(), 0.001);
        assertEquals("Seda", ropa.getMaterial());
    }
    
    @Test
    void testCrearPublicacionHogarConParametrosEspecificos() {
        Publicacion publicacion = factory.crearPublicacionHogar(
            "Cama",
            "Cama matrimonial",
            "cama.jpg",
            700,
            "Mueble de dormitorio"
        );
        
        assertNotNull(publicacion);
        PublicacionHogar hogar = (PublicacionHogar) publicacion;
        assertEquals("Mueble de dormitorio", hogar.getTipoMueble());
    }
    
    // ===== Pruebas con valores límite =====
    
    @Test
    void testCrearPublicacionesConValoresVacios() {
        Publicacion tech = factory.crearPublicacionTecnologia(
            "", "", "", 0, "", "", false
        );
        assertNotNull(tech);
        
        Publicacion ropa = factory.crearPublicacionRopa(
            "", "", "", 0, 0.0f, ""
        );
        assertNotNull(ropa);
        
        Publicacion hogar = factory.crearPublicacionHogar(
            "", "", "", 0, ""
        );
        assertNotNull(hogar);
    }
    
    @Test
    void testCrearPublicacionesConIdsNegativos() {
        Publicacion tech = factory.crearPublicacionTecnologia(
            "T", "D", "I", -1, "M", "Marca", true
        );
        assertEquals(-1, tech.getPublicadorId());
        
        Publicacion ropa = factory.crearPublicacionRopa(
            "T", "D", "I", -999, 10.0f, "Mat"
        );
        assertEquals(-999, ropa.getPublicadorId());
        
        Publicacion hogar = factory.crearPublicacionHogar(
            "T", "D", "I", -50, "Tipo"
        );
        assertEquals(-50, hogar.getPublicadorId());
    }
    
    // ===== Pruebas de inmutabilidad y aislamiento =====
    
    @Test
    void testCadaLlamadaCreaNuevaInstancia() {
        Publicacion pub1 = factory.crearPublicacionRopa(
            "Item", "Desc", "img.jpg", 1, 10.0f, "Material"
        );
        
        Publicacion pub2 = factory.crearPublicacionRopa(
            "Item", "Desc", "img.jpg", 1, 10.0f, "Material"
        );
        
        assertNotSame(pub1, pub2, "Cada llamada debe crear una nueva instancia");
    }
    
    @Test
    void testPublicacionesCreadasSonIndependientes() {
        Publicacion pub1 = factory.crearPublicacionRopa(
            "Titulo1", "Desc1", "img1.jpg", 1, 10.0f, "Material1"
        );
        
        Publicacion pub2 = factory.crearPublicacionRopa(
            "Titulo2", "Desc2", "img2.jpg", 2, 12.0f, "Material2"
        );
        
        // Verificamos que son independientes
        assertNotEquals(pub1.getTitulo(), pub2.getTitulo());
        assertNotEquals(pub1.getPublicadorId(), pub2.getPublicadorId());
    }
    
    // ===== Prueba de implementación de la interfaz =====
    
    @Test
    void testPublicacionFactoryImplementaInterfaz() {
        assertTrue(factory instanceof AbstractFactoryPublicacion,
            "PublicacionFactory debe implementar AbstractFactoryPublicacion");
    }
    
    @Test
    void testInterfazDefineTresMetodos() {
        // Verificamos que la interfaz tiene exactamente 3 métodos
        assertEquals(3, AbstractFactoryPublicacion.class.getDeclaredMethods().length,
            "La interfaz debe definir exactamente 3 métodos");
    }
}
