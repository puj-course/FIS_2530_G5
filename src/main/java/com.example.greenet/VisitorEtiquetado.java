package com.example.greenet;

import java.util.ArrayList;
import java.util.List;

public class VisitorEtiquetado implements VisitorPublicaciones {

    @Override
    public void visitar(PublicacionHogar hogar) {
        List<String> etiquetas = new ArrayList<>();

        if (hogar.getDescripcion().toLowerCase().contains("madera reciclada") ||
            hogar.getDescripcion().toLowerCase().contains("restaurado")) {
            etiquetas.add("eco-friendly");
        }

        if (hogar.getDescripcion().toLowerCase().contains("donar") ||
            hogar.getDescripcion().toLowerCase().contains("intercambio")) {
            etiquetas.add("solidario");
        }

        etiquetas.add("hogar");
        hogar.setEtiquetas(etiquetas);
        System.out.println("Etiquetas generadas para publicación de hogar: " + etiquetas);
    }

    @Override
    public void visitar(PublicacionRopa ropa) {
        List<String> etiquetas = new ArrayList<>();

        if (ropa.getMaterial().toLowerCase().contains("algodón reciclado") ||
            ropa.getMaterial().toLowerCase().contains("lana ecológica")) {
            etiquetas.add("eco-friendly");
        }

        if (ropa.getDescripcion().toLowerCase().contains("donar") ||
            ropa.getDescripcion().toLowerCase().contains("intercambio")) {
            etiquetas.add("solidario");
        }

        etiquetas.add("ropa");
        ropa.setEtiquetas(etiquetas);
        System.out.println("Etiquetas generadas para publicación de ropa: " + etiquetas);
    }

    @Override
    public void visitar(PublicacionTecnologia tecnologia) {
        List<String> etiquetas = new ArrayList<>();

        if (tecnologia.getDescripcion().toLowerCase().contains("reacondicionado") ||
            tecnologia.getDescripcion().toLowerCase().contains("reparado")) {
            etiquetas.add("reciclado");
        }

        if (tecnologia.isGarantia()) {
            etiquetas.add("garantía");
        }

        if (tecnologia.getDescripcion().toLowerCase().contains("donar") ||
            tecnologia.getDescripcion().toLowerCase().contains("intercambio")) {
            etiquetas.add("solidario");
        }

        etiquetas.add("tecnología");
        tecnologia.setEtiquetas(etiquetas);
        System.out.println("Etiquetas generadas para publicación tecnológica: " + etiquetas);
    }
}

