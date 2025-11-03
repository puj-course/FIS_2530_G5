// AbstractFactoryPublicacion.java
package com.example.greenet;

public interface AbstractFactoryPublicacion {
    Publicacion crearPublicacionTecnologia(String titulo, String descripcion, String imagen, 
                                         int publicadorId, String modelo, String marca, boolean garantia);
    
    Publicacion crearPublicacionRopa(String titulo, String descripcion, String imagen, 
                                   int publicadorId, float talla, String material);
    
    Publicacion crearPublicacionHogar(String titulo, String descripcion, String imagen, 
                                    int publicadorId, String tipoMueble);
}
