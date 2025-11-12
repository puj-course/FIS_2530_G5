package com.greenet;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VisitorValidacionesTest {

    // Tests para PublicacionHogar
    @Test
    public void testVisitarPublicacionHogar_Valido() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionHogar hogar = new PublicacionHogar(
            "Mesa de comedor", "Mesa de madera en buen estado", "imagen.jpg", 1, "Mesa"
        );
        
        visitor.visitar(hogar);
        
        assertTrue(visitor.esValido());
        assertEquals("", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionHogar_TituloVacio() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionHogar hogar = new PublicacionHogar(
            "", "Mesa de madera en buen estado", "imagen.jpg", 1, "Mesa"  // Titulo vacío
        );
        
        visitor.visitar(hogar);
        
        assertFalse(visitor.esValido());
        assertEquals("El título es obligatorio.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionHogar_TituloNull() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionHogar hogar = new PublicacionHogar(
            null, "Mesa de madera en buen estado", "imagen.jpg", 1, "Mesa"  // Titulo null
        );
        
        visitor.visitar(hogar);
        
        assertFalse(visitor.esValido());
        assertEquals("El título es obligatorio.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionHogar_DescripcionVacia() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionHogar hogar = new PublicacionHogar(
            "Mesa de comedor", "", "imagen.jpg", 1, "Mesa"  // Descripción vacía
        );
        
        visitor.visitar(hogar);
        
        assertFalse(visitor.esValido());
        assertEquals("La descripción es obligatoria.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionHogar_DescripcionNull() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionHogar hogar = new PublicacionHogar(
            "Mesa de comedor", null, "imagen.jpg", 1, "Mesa"  // Descripción null
        );
        
        visitor.visitar(hogar);
        
        assertFalse(visitor.esValido());
        assertEquals("La descripción es obligatoria.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionHogar_TipoMuebleVacio() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionHogar hogar = new PublicacionHogar(
            "Mesa de comedor", "Mesa de madera en buen estado", "imagen.jpg", 1, ""  // Tipo mueble vacío
        );
        
        visitor.visitar(hogar);
        
        assertFalse(visitor.esValido());
        assertEquals("El tipo de mueble no puede estar vacío.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionHogar_TipoMuebleNull() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionHogar hogar = new PublicacionHogar(
            "Mesa de comedor", "Mesa de madera en buen estado", "imagen.jpg", 1, null  // Tipo mueble null
        );
        
        visitor.visitar(hogar);
        
        assertFalse(visitor.esValido());
        assertEquals("El tipo de mueble no puede estar vacío.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionHogar_ImagenVacia() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionHogar hogar = new PublicacionHogar(
            "Mesa de comedor", "Mesa de madera en buen estado", "", 1, "Mesa"  // Imagen vacía
        );
        
        visitor.visitar(hogar);
        
        assertFalse(visitor.esValido());
        assertEquals("Debe subir una imagen.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionHogar_ImagenNull() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionHogar hogar = new PublicacionHogar(
            "Mesa de comedor", "Mesa de madera en buen estado", null, 1, "Mesa"  // Imagen null
        );
        
        visitor.visitar(hogar);
        
        assertFalse(visitor.esValido());
        assertEquals("Debe subir una imagen.", visitor.getMensajeError());
    }

    // Tests para PublicacionRopa
    @Test
    public void testVisitarPublicacionRopa_Valido() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionRopa ropa = new PublicacionRopa(
            "Camiseta básica", "Camiseta de algodón", "imagen.jpg", 1, 38.0f, "Algodón"
        );
        
        visitor.visitar(ropa);
        
        assertTrue(visitor.esValido());
        assertEquals("", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionRopa_TituloVacio() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionRopa ropa = new PublicacionRopa(
            "", "Camiseta de algodón", "imagen.jpg", 1, 38.0f, "Algodón"  // Titulo vacío
        );
        
        visitor.visitar(ropa);
        
        assertFalse(visitor.esValido());
        assertEquals("El título es obligatorio.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionRopa_MaterialVacio() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionRopa ropa = new PublicacionRopa(
            "Camiseta básica", "Camiseta de algodón", "imagen.jpg", 1, 38.0f, ""  // Material vacío
        );
        
        visitor.visitar(ropa);
        
        assertFalse(visitor.esValido());
        assertEquals("El material no puede estar vacío.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionRopa_MaterialNull() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionRopa ropa = new PublicacionRopa(
            "Camiseta básica", "Camiseta de algodón", "imagen.jpg", 1, 38.0f, null  // Material null
        );
        
        visitor.visitar(ropa);
        
        assertFalse(visitor.esValido());
        assertEquals("El material no puede estar vacío.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionRopa_ImagenVacia() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionRopa ropa = new PublicacionRopa(
            "Camiseta básica", "Camiseta de algodón", "", 1, 38.0f, "Algodón"  // Imagen vacía
        );
        
        visitor.visitar(ropa);
        
        assertFalse(visitor.esValido());
        assertEquals("Debe subir una imagen.", visitor.getMensajeError());
    }

    // Tests para PublicacionTecnologia
    @Test
    public void testVisitarPublicacionTecnologia_Valido() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "Laptop Gaming", "Laptop para juegos", "imagen.jpg", 1, "ROG", "Asus", true
        );
        
        visitor.visitar(tecnologia);
        
        assertTrue(visitor.esValido());
        assertEquals("", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionTecnologia_TituloVacio() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "", "Laptop para juegos", "imagen.jpg", 1, "ROG", "Asus", true  // Titulo vacío
        );
        
        visitor.visitar(tecnologia);
        
        assertFalse(visitor.esValido());
        assertEquals("El título es obligatorio.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionTecnologia_MarcaVacia() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "Laptop Gaming", "Laptop para juegos", "imagen.jpg", 1, "ROG", "", true  // Marca vacía
        );
        
        visitor.visitar(tecnologia);
        
        assertFalse(visitor.esValido());
        assertEquals("La marca no puede estar vacía.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionTecnologia_MarcaNull() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "Laptop Gaming", "Laptop para juegos", "imagen.jpg", 1, "ROG", null, true  // Marca null
        );
        
        visitor.visitar(tecnologia);
        
        assertFalse(visitor.esValido());
        assertEquals("La marca no puede estar vacía.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionTecnologia_ModeloVacio() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "Laptop Gaming", "Laptop para juegos", "imagen.jpg", 1, "", "Asus", true  // Modelo vacío
        );
        
        visitor.visitar(tecnologia);
        
        assertFalse(visitor.esValido());
        assertEquals("El modelo no puede estar vacío.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionTecnologia_ModeloNull() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "Laptop Gaming", "Laptop para juegos", "imagen.jpg", 1, null, "Asus", true  // Modelo null
        );
        
        visitor.visitar(tecnologia);
        
        assertFalse(visitor.esValido());
        assertEquals("El modelo no puede estar vacío.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionTecnologia_ImagenVacia() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "Laptop Gaming", "Laptop para juegos", "", 1, "ROG", "Asus", true  // Imagen vacía
        );
        
        visitor.visitar(tecnologia);
        
        assertFalse(visitor.esValido());
        assertEquals("Debe subir una imagen.", visitor.getMensajeError());
    }

    @Test
    public void testVisitarPublicacionTecnologia_ImagenNull() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "Laptop Gaming", "Laptop para juegos", null, 1, "ROG", "Asus", true  // Imagen null
        );
        
        visitor.visitar(tecnologia);
        
        assertFalse(visitor.esValido());
        assertEquals("Debe subir una imagen.", visitor.getMensajeError());
    }

    // Test para verificar que solo se guarda el primer error
    @Test
    public void testVisitarPublicacionHogar_MultiplesErrores_SoloPrimerError() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        PublicacionHogar hogar = new PublicacionHogar(
            "", "", "", 1, ""  // Todos los campos vacíos
        );
        
        visitor.visitar(hogar);
        
        assertFalse(visitor.esValido());
        // Solo debe mostrar el primer error encontrado (titulo)
        assertEquals("El título es obligatorio.", visitor.getMensajeError());
    }

    // Test para verificar reset del visitor
    @Test
    public void testVisitorValidaciones_MultiplesUsos() {
        VisitorValidaciones visitor = new VisitorValidaciones();
        
        // Primera validación - inválida
        PublicacionHogar hogarInvalido = new PublicacionHogar("", "Desc", "img.jpg", 1, "Mesa");
        visitor.visitar(hogarInvalido);
        assertFalse(visitor.esValido());
        
        // Segunda validación - válida (debería crear nuevo visitor)
        VisitorValidaciones visitor2 = new VisitorValidaciones();
        PublicacionHogar hogarValido = new PublicacionHogar("Titulo", "Desc", "img.jpg", 1, "Mesa");
        visitor2.visitar(hogarValido);
        assertTrue(visitor2.esValido());
    }
}