// PublicacionTecnologia.java

package com.example.greenet;
import java.util.List;
import java.util.ArrayList;

public class PublicacionTecnologia implements Publicacion {
    private String titulo;
    private String descripcion;
    private String categoria = "Tecnología";
    private String imagen;
    private int publicadorId;
    private List<String> etiquetas = new ArrayList<>();
    
    // Atributos específicos
    private String modelo;
    private String marca;
    private boolean garantia;

    public PublicacionTecnologia(String titulo, String descripcion, String imagen, 
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
    public String getModelo() { return modelo; }
    public String getMarca() { return marca; }
    public boolean isGarantia() { return garantia; }

    public void aceptarVisita(VisitorPublicaciones visitor) {
        visitor.visitar(this);
    }
}

