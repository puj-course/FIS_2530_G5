package com.example.greenet;

public interface Publicacion {
    // Atributos comunes (implícitos en interface)
    String getTitulo();
    String getDescripcion();
    String getCategoria();
    byte[] getImagen();
    int getPublicadorId();
    
    // Método visitor del diagrama
    void aceptarVisita(VisitorPublicaciones visitor);
}