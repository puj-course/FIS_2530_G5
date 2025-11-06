package com.greenet.metricas;

import java.io.*;
import java.util.regex.*;

public class MetricsCalculator {

    public static int calcularComplejidadCiclomatica(File archivo) throws IOException {
        int complejidad = 1;
        Pattern p = Pattern.compile("\\b(if|for|while|case|\\?|&&|\\|\\|)\\b");
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea;
        while ((linea = br.readLine()) != null) {
            Matcher m = p.matcher(linea);
            while (m.find()) {
                complejidad++;
            }
        }
        br.close();
        return complejidad;
    }

    public static long contarLineasArchivo(File archivo) throws IOException {
        long count = 0;
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        while (br.readLine() != null) {
            count++;
        }
        br.close();
        return count;
    }

    public static void analizarCarpeta(File carpeta) throws IOException {
        File[] archivos = carpeta.listFiles();
        if (archivos == null) {
            return;
        }
        for (File archivo : archivos) {
            if (archivo.isDirectory()) {
                analizarCarpeta(archivo);
            } else {
                if (archivo.getName().endsWith(".java")) {
                    long lineas = contarLineasArchivo(archivo);
                    int complejidad = calcularComplejidadCiclomatica(archivo);
                    System.out.println("Archivo: " + archivo.getName());
                    System.out.println("Lineas: " + lineas);
                    System.out.println("Complejidad: " + complejidad);
                    System.out.println();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        File raiz = new File("src/main/java/com/greenet");
        analizarCarpeta(raiz);
    }
}
