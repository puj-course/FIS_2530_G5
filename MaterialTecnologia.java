package com.example.greenet;

public class MaterialTecnologia extends Material {
    private String modelo;
    private String marca;
    private boolean garantia;

    public MaterialTecnologia(String titulo, String descripcion, byte[] imagen,
                              int publicadorId, String modelo, String marca, boolean garantia) {
        super(titulo, descripcion, "Tecnología", imagen, publicadorId);
        this.modelo = modelo;
        this.marca = marca;
        this.garantia = garantia;
    }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public boolean isGarantia() { return garantia; }
    public void setGarantia(boolean garantia) { this.garantia = garantia; }

    @Override
    public boolean validar() {
        return titulo != null && !titulo.isEmpty() &&
                descripcion != null && !descripcion.isEmpty() &&
                modelo != null && !modelo.isEmpty() &&
                marca != null && !marca.isEmpty();
    }

    @Override
    public String getDetallesEspecificos() {
        return String.format("Modelo: %s, Marca: %s, Garantía: %s",
                modelo, marca, garantia ? "Sí" : "No");
    }
}