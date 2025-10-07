package com.example.greenet;

public class MaterialBase extends Material {

    public MaterialBase(String titulo, String descripcion, String categoria,
                        byte[] imagen, int publicadorId) {
        super(titulo, descripcion, categoria, imagen, publicadorId);
    }

    @Override
    public boolean validar() {
        return titulo != null && !titulo.isEmpty() &&
                descripcion != null && !descripcion.isEmpty() &&
                categoria != null && !categoria.isEmpty() &&
                imagen != null && imagen.length > 0;
    }

    @Override
    public String getDetallesEspecificos() {
        return "Categoría: " + categoria;
    }
}