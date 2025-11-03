// PublicacionHogar.java

package com.example.greenet;
import java.util.List;
import java.util.ArrayList;

public class PublicacionHogar implements Publicacion {
    private String titulo;
    private String descripcion;
    private String categoria = "Hogar";
    private String imagen;
    private int publicadorId;
    private List<String> etiquetas = new ArrayList<>();
    
    // Atributo específico
    private String tipoMueble;
     

    public PublicacionHogar(String titulo, String descripcion, String imagen, 
                           int publicadorId, String tipoMueble) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.publicadorId = publicadorId;
        this.tipoMueble = tipoMueble;
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
    // Getter específico
    public String getTipoMueble() { return tipoMueble; }

    
    public void aceptarVisita(VisitorPublicaciones visitor) {
        visitor.visitar(this);
    }
}
