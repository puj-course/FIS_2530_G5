package com.example.greenet;

import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class FachadaImagen {

    public static String codificarImagenAString(byte[] imagenBytes) {
        if (imagenBytes == null || imagenBytes.length == 0) {
            return null;
        }
        return Base64.getEncoder().encodeToString(imagenBytes);
    }

    public static byte[] decodificarStringAImagen(String imagenBase64) {
        if (imagenBase64 == null || imagenBase64.isEmpty()) {
            return new byte[0];
        }
        return Base64.getDecoder().decode(imagenBase64);
    }

    public static byte[] convertirImageABytes(Image image) {
        if (image == null) return new byte[0];

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Error al convertir imagen a bytes: " + e.getMessage());
            return new byte[0];
        }
    }

    public static Image convertirBytesAImage(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            return new Image(bis);
        } catch (Exception e) {
            System.err.println("Error al convertir bytes a imagen: " + e.getMessage());
            return null;
        }
    }
}
