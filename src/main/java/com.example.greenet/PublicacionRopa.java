// PublicacionRopa.java
package com.example.greenet;

public class PublicacionRopa implements Publicacion {
    private String titulo;
    private String descripcion;
    private String categoria = "Ropa";
    private byte[] imagen;
    private int publicadorId;
    
    // Atributos específicos
    private float talla;
    private String material;

    public PublicacionRopa(String titulo, String descripcion, byte[] imagen, 
                          int publicadorId, float talla, String material) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.publicadorId = publicadorId;
        this.talla = talla;
        this.material = material;
    }

    // Getters comunes
    @Override public String getTitulo() { return titulo; }
    @Override public String getDescripcion() { return descripcion; }
    @Override public String getCategoria() { return categoria; }
    @Override public byte[] getImagen() { return imagen; }
    @Override public int getPublicadorId() { return publicadorId; }
    
    // Getters específicos
    public float getTalla() { return talla; }
    public String getMaterial() { return material; }

    @Override
    public void aceptarVisita(VisitorPublicaciones visitor) {
        visitor.visitar(this);
    }
}