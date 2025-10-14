package com.example.greenet;

public class VisitorValidaciones implements VisitorPublicaciones {
    private boolean esValido = true;
    private String mensajeError = "";

    @Override
    public void visitar(PublicacionHogar hogar) {
        if (hogar.getTitulo() == null || hogar.getTitulo().isEmpty())
            setError("El título es obligatorio.");
        else if (hogar.getDescripcion() == null || hogar.getDescripcion().isEmpty())
            setError("La descripción es obligatoria.");
        else if (hogar.getTipoMueble() == null || hogar.getTipoMueble().isEmpty())
            setError("El tipo de mueble no puede estar vacío.");
        else if (hogar.getImagen() == null || hogar.getImagen().length == 0)
            setError("Debe subir una imagen.");
    }

    @Override
    public void visitar(PublicacionRopa ropa) {
        if (ropa.getTitulo() == null || ropa.getTitulo().isEmpty())
            setError("El título es obligatorio.");
        else if (ropa.getMaterial() == null || ropa.getMaterial().isEmpty())
            setError("El material no puede estar vacío.");
        else if (ropa.getImagen() == null || ropa.getImagen().length == 0)
            setError("Debe subir una imagen.");
    }

    @Override
    public void visitar(PublicacionTecnologia tecnologia) {
        if (tecnologia.getTitulo() == null || tecnologia.getTitulo().isEmpty())
            setError("El título es obligatorio.");
        else if (tecnologia.getMarca() == null || tecnologia.getMarca().isEmpty())
            setError("La marca no puede estar vacía.");
        else if (tecnologia.getModelo() == null || tecnologia.getModelo().isEmpty())
            setError("El modelo no puede estar vacío.");
        else if (tecnologia.getImagen() == null || tecnologia.getImagen().length == 0)
            setError("Debe subir una imagen.");
    }

    private void setError(String mensaje) {
        esValido = false;
        mensajeError = mensaje;
    }

    public boolean esValido() {
        return esValido;
    }

    public String getMensajeError() {
        return mensajeError;
    }
}
