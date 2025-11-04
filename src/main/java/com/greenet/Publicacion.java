package com.greenet;


import java.util.List;
public interface Publicacion {
    // Atributos comunes (implícitos en interface)
    String getTitulo();
    String getDescripcion();
    String getCategoria();
    String getImagen();
    int getPublicadorId();

    void aceptarVisita(VisitorPublicaciones visitor);
    List<String> getEtiquetas();
    void setEtiquetas(List<String> etiquetas);
}