// PublicacionHogar.java
package com.example.greenet;

public class PublicacionHogar implements Publicacion {
    private String titulo;
    private String descripcion;
    private String categoria = "Hogar";
    private byte[] imagen;
    private int publicadorId;
    
    // Atributo específico
    private String tipoMueble;

    public PublicacionHogar(String titulo, String descripcion, byte[] imagen, 
                           int publicadorId, String tipoMueble) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.publicadorId = publicadorId;
        this.tipoMueble = tipoMueble;
    }

    // Getters comunes
    @Override public String getTitulo() { return titulo; }
    @Override public String getDescripcion() { return descripcion; }
    @Override public String getCategoria() { return categoria; }
    @Override public byte[] getImagen() { return imagen; }
    @Override public int getPublicadorId() { return publicadorId; }
    
    // Getter específico
    public String getTipoMueble() { return tipoMueble; }

    @Override
    public void aceptarVisita(VisitorPublicaciones visitor) {
        visitor.visitar(this);
    }
}