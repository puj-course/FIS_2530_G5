package com.example.greenet;

import java.util.Date;

public class Publicacion {
    private int id;
    private String titulo;
    private String descripcion;
    private String estado;
    private Date fechaPublicacion;
    private int usuarioId;
    private String nombreUsuario;

    // COMPOSICIÓN: La publicación contiene un material
    private Material material;

    // Constructor
    public Publicacion(String titulo, String descripcion, int usuarioId, Material material) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.usuarioId = usuarioId;
        this.material = material;
        this.estado = "activa";
        this.fechaPublicacion = new Date();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Date getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(Date fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public Material getMaterial() { return material; }
    public void setMaterial(Material material) { this.material = material; }

    // Métodos de utilidad
    public void editarPublicacion(String nuevoTitulo, String nuevaDescripcion) {
        this.titulo = nuevoTitulo;
        this.descripcion = nuevaDescripcion;
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public String getDetallesCompletos() {
        return String.format("""
            Título: %s
            Descripción: %s
            Categoría: %s
            %s
            Estado: %s
            Publicado: %s
            """,
                titulo, descripcion, material.getCategoria(),
                material.getDetallesEspecificos(), estado, fechaPublicacion);
    }

    @Override
    public String toString() {
        return "Publicacion{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                ", material=" + material.getCategoria() +
                '}';
    }
}