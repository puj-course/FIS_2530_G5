// PublicacionRopa.java

package com.greenet;
import java.util.List;
import java.util.ArrayList;

public class PublicacionRopa implements Publicacion {
    private String titulo;
    private String descripcion;
    private String categoria = "Ropa";
    private String imagen;
    private int publicadorId;
    private List<String> etiquetas = new ArrayList<>();
    
    // Atributos específicos
    private float talla;
    private String material;

    public PublicacionRopa(String titulo, String descripcion, String imagen, 
                          int publicadorId, float talla, String material) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.publicadorId = publicadorId;
        this.talla = talla;
        this.material = material;
    }

    // Getters comunes
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getCategoria() { return categoria; }
    public String getImagen() { return imagen; }
    public int getPublicadorId() { return publicadorId; }
    public List<String> getEtiquetas() { return etiquetas; }
    public void setEtiquetas(List<String> etiquetas) { this.etiquetas = etiquetas; }
    
    public void setNombreUsuario(String s) {
    }
    // Getters específicos
    public float getTalla() { return talla; }
    public String getMaterial() { return material; }

    
    public void aceptarVisita(VisitorPublicaciones visitor) {
        visitor.visitar(this);
    }
}
