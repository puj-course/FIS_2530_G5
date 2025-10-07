package com.example.greenet;

public class MaterialRopa extends Material {
    private float talla;
    private String material;

    public MaterialRopa(String titulo, String descripcion, byte[] imagen,
                        int publicadorId, float talla, String material) {
        super(titulo, descripcion, "Ropa", imagen, publicadorId);
        this.talla = talla;
        this.material = material;
    }

    public float getTalla() { return talla; }
    public void setTalla(float talla) { this.talla = talla; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    @Override
    public boolean validar() {
        return titulo != null && !titulo.isEmpty() &&
                descripcion != null && !descripcion.isEmpty() &&
                material != null && !material.isEmpty() &&
                talla > 0;
    }

    @Override
    public String getDetallesEspecificos() {
        return String.format("Talla: %.1f, Material: %s", talla, material);
    }
}