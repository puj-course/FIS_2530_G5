package com.greenet;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class VisitorEtiquetadoTest {

    // Tests para PublicacionHogar
    @Test
    public void testVisitarPublicacionHogar_EcoFriendlyMaderaReciclada() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionHogar hogar = new PublicacionHogar(
            "Mesa", "Mesa de madera reciclada", "img.jpg", 1, "Mesa"
        );
        
        visitor.visitar(hogar);
        
        List<String> etiquetas = hogar.getEtiquetas();
        assertTrue(etiquetas.contains("eco-friendly"));
        assertTrue(etiquetas.contains("hogar"));
    }

    @Test
    public void testVisitarPublicacionHogar_EcoFriendlyRestaurado() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionHogar hogar = new PublicacionHogar(
            "Silla", "Silla restaurado", "img.jpg", 1, "Silla"  // 
        );
        
        visitor.visitar(hogar);
        
        List<String> etiquetas = hogar.getEtiquetas();
        assertTrue(etiquetas.contains("eco-friendly"));
        assertTrue(etiquetas.contains("hogar"));
    }

    @Test
    public void testVisitarPublicacionHogar_SolidarioDonar() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionHogar hogar = new PublicacionHogar(
            "Mueble", "Mueble para donar", "img.jpg", 1, "Armario"
        );
        
        visitor.visitar(hogar);
        
        List<String> etiquetas = hogar.getEtiquetas();
        assertTrue(etiquetas.contains("solidario"));
        assertTrue(etiquetas.contains("hogar"));
    }

    @Test
    public void testVisitarPublicacionHogar_SolidarioIntercambio() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionHogar hogar = new PublicacionHogar(
            "Mesa", "Mesa para intercambio", "img.jpg", 1, "Mesa"
        );
        
        visitor.visitar(hogar);
        
        List<String> etiquetas = hogar.getEtiquetas();
        assertTrue(etiquetas.contains("solidario"));
        assertTrue(etiquetas.contains("hogar"));
    }

    @Test
    public void testVisitarPublicacionHogar_MultiplesEtiquetas() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionHogar hogar = new PublicacionHogar(
            "Mueble", "Mueble restaurado para intercambio", "img.jpg", 1, "Mueble"  // 
        );
        
        visitor.visitar(hogar);
        
        List<String> etiquetas = hogar.getEtiquetas();
        assertTrue(etiquetas.contains("eco-friendly"));
        assertTrue(etiquetas.contains("solidario"));
        assertTrue(etiquetas.contains("hogar"));
        assertEquals(3, etiquetas.size());
    }

    @Test
    public void testVisitarPublicacionHogar_SinEtiquetasEspeciales() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionHogar hogar = new PublicacionHogar(
            "Silla", "Silla común", "img.jpg", 1, "Silla"
        );
        
        visitor.visitar(hogar);
        
        List<String> etiquetas = hogar.getEtiquetas();
        assertTrue(etiquetas.contains("hogar"));
        assertEquals(1, etiquetas.size());
        assertFalse(etiquetas.contains("eco-friendly"));
        assertFalse(etiquetas.contains("solidario"));
    }

    // Tests para PublicacionRopa
    @Test
    public void testVisitarPublicacionRopa_EcoFriendlyAlgodonReciclado() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionRopa ropa = new PublicacionRopa(
            "Camiseta", "Camiseta básica", "img.jpg", 1, 38.0f, "algodón reciclado"
        );
        
        visitor.visitar(ropa);
        
        List<String> etiquetas = ropa.getEtiquetas();
        assertTrue(etiquetas.contains("eco-friendly"));
        assertTrue(etiquetas.contains("ropa"));
    }

    @Test
    public void testVisitarPublicacionRopa_EcoFriendlyLanaEcologica() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionRopa ropa = new PublicacionRopa(
            "Suéter", "Suéter abrigado", "img.jpg", 1, 40.0f, "lana ecológica"
        );
        
        visitor.visitar(ropa);
        
        List<String> etiquetas = ropa.getEtiquetas();
        assertTrue(etiquetas.contains("eco-friendly"));
        assertTrue(etiquetas.contains("ropa"));
    }

    @Test
    public void testVisitarPublicacionRopa_SolidarioDonar() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionRopa ropa = new PublicacionRopa(
            "Chaquetas", "Chaquetas para donar", "img.jpg", 1, 42.0f, "Poliéster"
        );
        
        visitor.visitar(ropa);
        
        List<String> etiquetas = ropa.getEtiquetas();
        assertTrue(etiquetas.contains("solidario"));
        assertTrue(etiquetas.contains("ropa"));
    }

    @Test
    public void testVisitarPublicacionRopa_SolidarioIntercambio() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionRopa ropa = new PublicacionRopa(
            "Pantalones", "Pantalones para intercambio", "img.jpg", 1, 32.0f, "Algodón"
        );
        
        visitor.visitar(ropa);
        
        List<String> etiquetas = ropa.getEtiquetas();
        assertTrue(etiquetas.contains("solidario"));
        assertTrue(etiquetas.contains("ropa"));
    }

    @Test
    public void testVisitarPublicacionRopa_SinEtiquetasEspeciales() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionRopa ropa = new PublicacionRopa(
            "Camisa", "Camisa normal", "img.jpg", 1, 38.0f, "Poliéster"
        );
        
        visitor.visitar(ropa);
        
        List<String> etiquetas = ropa.getEtiquetas();
        assertTrue(etiquetas.contains("ropa"));
        assertEquals(1, etiquetas.size());
        assertFalse(etiquetas.contains("eco-friendly"));
        assertFalse(etiquetas.contains("solidario"));
    }

    // Tests para PublicacionTecnologia
    @Test
    public void testVisitarPublicacionTecnologia_RecicladoReacondicionado() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "Laptop", "Laptop reacondicionado", "img.jpg", 1, "XPS", "Dell", false  //
        );
        
        visitor.visitar(tecnologia);
        
        List<String> etiquetas = tecnologia.getEtiquetas();
        assertTrue(etiquetas.contains("reciclado"));
        assertTrue(etiquetas.contains("tecnología"));
    }

    @Test
    public void testVisitarPublicacionTecnologia_RecicladoReparado() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "Teléfono", "Teléfono reparado", "img.jpg", 1, "Galaxy", "Samsung", false
        );
        
        visitor.visitar(tecnologia);
        
        List<String> etiquetas = tecnologia.getEtiquetas();
        assertTrue(etiquetas.contains("reciclado"));
        assertTrue(etiquetas.contains("tecnología"));
    }

    @Test
    public void testVisitarPublicacionTecnologia_Garantia() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "Tablet", "Tablet nueva", "img.jpg", 1, "iPad", "Apple", true
        );
        
        visitor.visitar(tecnologia);
        
        List<String> etiquetas = tecnologia.getEtiquetas();
        assertTrue(etiquetas.contains("garantía"));
        assertTrue(etiquetas.contains("tecnología"));
    }

    @Test
    public void testVisitarPublicacionTecnologia_SolidarioDonar() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "Monitor", "Monitor para donar", "img.jpg", 1, "UltraSharp", "Dell", false
        );
        
        visitor.visitar(tecnologia);
        
        List<String> etiquetas = tecnologia.getEtiquetas();
        assertTrue(etiquetas.contains("solidario"));
        assertTrue(etiquetas.contains("tecnología"));
    }

    @Test
    public void testVisitarPublicacionTecnologia_SolidarioIntercambio() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "Teclado", "Teclado para intercambio", "img.jpg", 1, "K120", "Logitech", false
        );
        
        visitor.visitar(tecnologia);
        
        List<String> etiquetas = tecnologia.getEtiquetas();
        assertTrue(etiquetas.contains("solidario"));
        assertTrue(etiquetas.contains("tecnología"));
    }

    @Test
    public void testVisitarPublicacionTecnologia_TodasLasEtiquetas() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "PC", "PC reacondicionado para intercambio con garantía", "img.jpg", 1, "ThinkPad", "Lenovo", true
        );
        
        visitor.visitar(tecnologia);
        
        List<String> etiquetas = tecnologia.getEtiquetas();
        assertTrue(etiquetas.contains("reciclado"));
        assertTrue(etiquetas.contains("garantía"));
        assertTrue(etiquetas.contains("solidario"));
        assertTrue(etiquetas.contains("tecnología"));
        assertEquals(4, etiquetas.size());
    }

    @Test
    public void testVisitarPublicacionTecnologia_SinEtiquetasEspeciales() {
        VisitorEtiquetado visitor = new VisitorEtiquetado();
        PublicacionTecnologia tecnologia = new PublicacionTecnologia(
            "Mouse", "Mouse normal", "img.jpg", 1, "M185", "Logitech", false
        );
        
        visitor.visitar(tecnologia);
        
        List<String> etiquetas = tecnologia.getEtiquetas();
        assertTrue(etiquetas.contains("tecnología"));
        assertEquals(1, etiquetas.size());
        assertFalse(etiquetas.contains("reciclado"));
        assertFalse(etiquetas.contains("garantía"));
        assertFalse(etiquetas.contains("solidario"));
    }
}