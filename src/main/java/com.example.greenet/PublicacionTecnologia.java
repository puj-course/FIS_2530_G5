// PublicacionTecnologia.java
package com.example.greenet;

public class PublicacionTecnologia implements Publicacion {
    private String titulo;
    private String descripcion;
    private String categoria = "Tecnología";
    private byte[] imagen;
    private int publicadorId;
    private List<String> etiquetas = new ArrayList<>();
    
    // Atributos específicos
    private String modelo;
    private String marca;
    private boolean garantia;

    public PublicacionTecnologia(String titulo, String descripcion, byte[] imagen, 
                               int publicadorId, String modelo, String marca, boolean garantia) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.publicadorId = publicadorId;
        this.modelo = modelo;
        this.marca = marca;
        this.garantia = garantia;
    }

    // Getters comunes
    @Override public String getTitulo() { return titulo; }
    @Override public String getDescripcion() { return descripcion; }
    @Override public String getCategoria() { return categoria; }
    @Override public byte[] getImagen() { return imagen; }
    @Override public int getPublicadorId() { return publicadorId; }
    @Override public List<String> getEtiquetas() { return etiquetas; }
    @Override public void setEtiquetas(List<String> etiquetas) { this.etiquetas = etiquetas; }
    @Override
    public void setNombreUsuario(String s) {
    }
    // Getters específicos
    public String getModelo() { return modelo; }
    public String getMarca() { return marca; }
    public boolean isGarantia() { return garantia; }

    @Override
    public void aceptarVisita(VisitorPublicaciones visitor) {
        visitor.visitar(this);
    }
}

