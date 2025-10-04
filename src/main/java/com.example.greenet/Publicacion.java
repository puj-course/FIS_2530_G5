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
    private int materialId;
    private String tipoMaterial;
    private int unidadMedia;

    // Constructor vacío
    public Publicacion() {
    }

    // Constructor con parámetros principales
    public Publicacion(int id, String titulo, String descripcion, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(String tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public int getUnidadMedia() {
        return unidadMedia;
    }

    public void setUnidadMedia(int unidadMedia) {
        this.unidadMedia = unidadMedia;
    }

    // Métodos de utilidad
    public void editarPublicacion(String nuevoTitulo, String nuevaDescripcion) {
        this.titulo = nuevoTitulo;
        this.descripcion = nuevaDescripcion;
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    @Override
    public String toString() {
        return "Publicacion{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", tipoMaterial='" + tipoMaterial + '\'' +
                '}';
    }
}
