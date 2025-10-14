package com.example.greenet;
import java.util.List;
public interface Publicacion {
    // Atributos comunes (implícitos en interface)
    String getTitulo();
    String getDescripcion();
    String getCategoria();
    byte[] getImagen();
    int getPublicadorId();
    
    // Método visitor del diagrama
    void aceptarVisita(VisitorPublicaciones visitor);
    List<String> getEtiquetas();
    void setEtiquetas(List<String> etiquetas);
}
