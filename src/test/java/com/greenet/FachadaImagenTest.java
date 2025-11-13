package com.greenet;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class FachadaImagenTest {

    private BufferedImage imagenPrueba;
    private byte[] bytesImagen;
    private String base64Valido;

    @BeforeEach
    void setUp() throws IOException {
        imagenPrueba = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imagenPrueba, "png", baos);
        bytesImagen = baos.toByteArray();
        base64Valido = Base64.getEncoder().encodeToString(bytesImagen);
    }

    @Test
    void testCodificarImagenAStringCorrecto() {
        String resultado = FachadaImagen.codificarImagenAString(bytesImagen);
        assertNotNull(resultado);
        assertEquals(base64Valido, resultado);
    }

    @Test
    void testCodificarImagenAStringConNulo() {
        assertNull(FachadaImagen.codificarImagenAString(null));
    }

    @Test
    void testCodificarImagenAStringConVacio() {
        assertNull(FachadaImagen.codificarImagenAString(new byte[0]));
    }

    @Test
    void testDecodificarStringAImagenCorrecto() {
        byte[] resultado = FachadaImagen.decodificarStringAImagen(base64Valido);
        assertArrayEquals(bytesImagen, resultado);
    }

    @Test
    void testDecodificarStringAImagenConNulo() {
        byte[] resultado = FachadaImagen.decodificarStringAImagen(null);
        assertEquals(0, resultado.length);
    }

    @Test
    void testDecodificarStringAImagenConVacio() {
        byte[] resultado = FachadaImagen.decodificarStringAImagen("");
        assertEquals(0, resultado.length);
    }

    @Test
    void testConvertirImageABytesCorrecto() {
        Image image = SwingFXUtils.toFXImage(imagenPrueba, null);
        byte[] resultado = FachadaImagen.convertirImageABytes(image);
        assertTrue(resultado.length > 0);
    }

    @Test
    void testConvertirImageABytesConNulo() {
        assertEquals(0, FachadaImagen.convertirImageABytes(null).length);
    }

    @Test
    void testConvertirBytesAImageCorrecto() {
        Image resultado = FachadaImagen.convertirBytesAImage(bytesImagen);
        assertNotNull(resultado);
    }

    @Test
    void testConvertirBytesAImageConNulo() {
        assertNull(FachadaImagen.convertirBytesAImage(null));
    }

    @Test
    void testConvertirBytesAImageConVacio() {
        assertNull(FachadaImagen.convertirBytesAImage(new byte[0]));
    }

    @Test
    void testConvertirBytesAImageConError() {
        byte[] bytesInvalidos = new byte[]{1, 2, 3, 4, 5};
        assertDoesNotThrow(() -> {
            Image resultado = FachadaImagen.convertirBytesAImage(bytesInvalidos);
            // aceptamos null o imagen vacía
            assertTrue(resultado == null || resultado.getWidth() >= 0);
        });
    }

    @Test
    void testBase64ToImageCorrecto() {
        Image resultado = FachadaImagen.base64ToImage(base64Valido);
        assertNotNull(resultado);
    }

    @Test
    void testBase64ToImageConNulo() {
        assertNull(FachadaImagen.base64ToImage(null));
    }

    @Test
    void testBase64ToImageConVacio() {
        assertNull(FachadaImagen.base64ToImage(""));
    }

    @Test
    void testBase64ToImageConBase64Invalido() {
        try {
            Image img = FachadaImagen.base64ToImage("###invalido_base64###");
            assertTrue(img == null || img.getWidth() >= 0);
        } catch (IllegalArgumentException e) {
            // Si lanza excepción por base64 inválido, también es comportamiento válido
            assertTrue(e.getMessage().contains("Illegal base64"));
        }
    }

    @Test
    void testCodificarYDecodificarCoinciden() {
        String base64 = FachadaImagen.codificarImagenAString(bytesImagen);
        byte[] restaurado = FachadaImagen.decodificarStringAImagen(base64);
        assertArrayEquals(bytesImagen, restaurado);
    }
}