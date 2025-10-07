package com.example.greenet;

public class MaterialFactory {

    // Método principal que crea el tipo específico de Material
    public static Material crearMaterial(String categoria, String titulo, String descripcion,
                                         byte[] imagen, int publicadorId, String... parametrosEspecificos) {
        return switch (categoria) {
            case "Tecnología" -> new MaterialTecnologia(titulo, descripcion, imagen, publicadorId,
                    parametrosEspecificos[0], parametrosEspecificos[1],
                    Boolean.parseBoolean(parametrosEspecificos[2]));
            case "Ropa" -> new MaterialRopa(titulo, descripcion, imagen, publicadorId,
                    Float.parseFloat(parametrosEspecificos[0]), parametrosEspecificos[1]);
            case "Hogar" -> new MaterialHogar(titulo, descripcion, imagen, publicadorId, parametrosEspecificos[0]);
            default -> new MaterialBase(titulo, descripcion, categoria, imagen, publicadorId);
        };
    }

    // Método de validación para cada categoría
    public static boolean validarParametros(String categoria, String... parametros) {
        return switch (categoria) {
            case "Tecnología" -> parametros.length >= 3 &&
                    !parametros[0].isEmpty() && !parametros[1].isEmpty();
            case "Ropa" -> parametros.length >= 2 &&
                    !parametros[0].isEmpty() && !parametros[1].isEmpty();
            case "Hogar" -> parametros.length >= 1 && !parametros[0].isEmpty();
            default -> true;
        };
    }

    // Método para obtener los campos requeridos por categoría
    public static String[] getCamposRequeridos(String categoria) {
        return switch (categoria) {
            case "Tecnología" -> new String[]{"Modelo", "Marca", "Garantía"};
            case "Ropa" -> new String[]{"Talla", "Material"};
            case "Hogar" -> new String[]{"Tipo de Mueble"};
            default -> new String[]{};
        };
    }
}