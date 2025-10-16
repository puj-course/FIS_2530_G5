// PublicacionFactory.java
package com.example.greenet;

public class PublicacionFactory implements AbstractFactoryPublicacion {
    
    @Override
    public Publicacion crearPublicacionTecnologia(String titulo, String descripcion, String imagen, 
                                                int publicadorId, String modelo, String marca, boolean garantia) {
        return new PublicacionTecnologia(titulo, descripcion, imagen, publicadorId, modelo, marca, garantia);
    }
    
    @Override
    public Publicacion crearPublicacionRopa(String titulo, String descripcion, String imagen, 
                                          int publicadorId, float talla, String material) {
        return new PublicacionRopa(titulo, descripcion, imagen, publicadorId, talla, material);
    }
    
    @Override
    public Publicacion crearPublicacionHogar(String titulo, String descripcion, String imagen, 
                                           int publicadorId, String tipoMueble) {
        return new PublicacionHogar(titulo, descripcion, imagen, publicadorId, tipoMueble);
    }
    
    // Métodos de conveniencia
    public Publicacion crearPublicacion(String categoria, String titulo, String descripcion, 
                                      String imagen, int publicadorId, String... parametros) {
        return switch (categoria) {
            case "Tecnología" -> crearPublicacionTecnologia(titulo, descripcion, imagen, publicadorId,
                    parametros[0], parametros[1], Boolean.parseBoolean(parametros[2]));
            case "Ropa" -> crearPublicacionRopa(titulo, descripcion, imagen, publicadorId,
                    Float.parseFloat(parametros[0]), parametros[1]);
            case "Hogar" -> crearPublicacionHogar(titulo, descripcion, imagen, publicadorId, parametros[0]);
            default -> throw new IllegalArgumentException("Categoría no soportada: " + categoria);
        };
    }
}
