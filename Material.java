package com.example.greenet;

import java.util.Date;

public abstract class Material {
    protected int id;
    protected String titulo;
    protected String descripcion;
    protected String categoria;
    protected byte[] imagen;
    protected Date fechaPublicacion;
    protected int publicadorId;
    protected int estado;

    // Constructor común
    public Material(String titulo, String descripcion, String categoria,
                    byte[] imagen, int publicadorId) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.imagen = imagen;
        this.publicadorId = publicadorId;
        this.estado = 1;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public byte[] getImagen() { return imagen; }
    public void setImagen(byte[] imagen) { this.imagen = imagen; }
    public Date getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(Date fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
    public int getPublicadorId() { return publicadorId; }
    public void setPublicadorId(int publicadorId) { this.publicadorId = publicadorId; }
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public String getEstadoTexto() {
        return switch (estado) {
            case 1 -> "Disponible";
            case 2 -> "Borrado";
            case 3 -> "Intercambiado";
            default -> "Desconocido";
        };
    }

    // Métodos abstractos
    public abstract boolean validar();
    public abstract String getDetallesEspecificos();
}