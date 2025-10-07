package com.example.greenet;

public class MaterialHogar extends Material {
    private String tipoMueble;

    public MaterialHogar(String titulo, String descripcion, byte[] imagen,
                         int publicadorId, String tipoMueble) {
        super(titulo, descripcion, "Hogar", imagen, publicadorId);
        this.tipoMueble = tipoMueble;
    }

    public String getTipoMueble() { return tipoMueble; }
    public void setTipoMueble(String tipoMueble) { this.tipoMueble = tipoMueble; }

    @Override
    public boolean validar() {
        return titulo != null && !titulo.isEmpty() &&
                descripcion != null && !descripcion.isEmpty() &&
                tipoMueble != null && !tipoMueble.isEmpty();
    }

    @Override
    public String getDetallesEspecificos() {
        return String.format("Tipo de mueble: %s", tipoMueble);
    }
}