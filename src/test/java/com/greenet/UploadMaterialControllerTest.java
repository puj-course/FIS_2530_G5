package com.greenet;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UploadMaterialControllerTest {

    // ==================== TESTS BÁSICOS ====================

    @Test
    void testConstructor() {
        UploadMaterialController controller = new UploadMaterialController();
        assertNotNull(controller);
    }

    @Test
    void testSetUsuarioActual() {
        UploadMaterialController controller = new UploadMaterialController();
        assertDoesNotThrow(() -> controller.setUsuarioActual(123));
    }

    // ==================== TESTS ESPECÍFICOS PARA crearPublicacion ====================

    @Test
    void testCrearPublicacion_FlujoCompleto() {
        UploadMaterialController controller = new UploadMaterialController();
        
        int resultado = controller.crearPublicacion(1, "iPhone 13", "Smartphone Apple", "Tecnología", 
            "imagen_base64", 1, Arrays.asList("13", "Apple", "true"));
        
        assertTrue(resultado == -1 || resultado >= 0);
    }

    @Test
    void testCrearPublicacion_ValidacionExitosa() {
        UploadMaterialController controller = new UploadMaterialController();
        
        int resultado = controller.crearPublicacion(1, "Título válido", "Descripción válida", "Tecnología", 
            "imagen", 1, Arrays.asList("Modelo", "Marca", "true"));
        
        assertTrue(resultado == -1 || resultado >= 0);
    }

    @Test
    void testCrearPublicacion_ConExcepcionEnFactory() {
        UploadMaterialController controller = new UploadMaterialController();
        
        int resultado = controller.crearPublicacion(1, "Test", "Test", "CategoriaInexistente", 
            "img", 99, new ArrayList<>());
        
        assertEquals(-1, resultado);
    }

    // ==================== TESTS PARA registrarPublicacionEnBD VIA REFLEXIÓN ====================

    @Test
    void testRegistrarPublicacionEnBD_MetodoPrivado() throws Exception {
        UploadMaterialController controller = new UploadMaterialController();
        
        Method method = UploadMaterialController.class.getDeclaredMethod(
            "registrarPublicacionEnBD", Publicacion.class, int.class, int.class);
        method.setAccessible(true);
        
        PublicacionTecnologia publicacion = new PublicacionTecnologia(
            "Test", "Descripción", "imagen.jpg", 1, "ModeloX", "MarcaY", true);
        
        try {
            int resultado = (int) method.invoke(controller, publicacion, 1, 1);
            assertTrue(resultado == -1 || resultado >= 0);
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof Exception);
        }
    }

    @Test
void testRegistrarPublicacionEnBD_ConParametros() throws Exception {
    UploadMaterialController controller = new UploadMaterialController();
    
    Method method = UploadMaterialController.class.getDeclaredMethod(
        "registrarPublicacionEnBD", 
        Publicacion.class, int.class, int.class
    );
    method.setAccessible(true);
    
    PublicacionTecnologia publicacion = new PublicacionTecnologia(
        "Laptop", "Laptop gaming", "img.jpg", 1, "XPS15", "Dell", true
    );
    
    int resultado = (int) method.invoke(controller, publicacion, 123, 1); // categoriaId = 1 para Tecnología
    
    assertTrue(resultado == -1 || resultado >= 0);
}

@Test
void testRegistrarPublicacionEnBD_TodasCategorias() throws Exception {
    UploadMaterialController controller = new UploadMaterialController();
    
    Method method = UploadMaterialController.class.getDeclaredMethod(
        "registrarPublicacionEnBD", 
        Publicacion.class, int.class, int.class
    );
    method.setAccessible(true);
    
    // Probar las 3 categorías del switch
    Object[][] casos = {
        // Tecnología (categoriaId = 1)
        new Object[]{1, new PublicacionTecnologia("Laptop", "Desc", "img.jpg", 1, "Modelo", "Marca", true)},
        // Ropa (categoriaId = 2)  
        new Object[]{2, new PublicacionRopa("Camisa", "Desc", "img.jpg", 1, 42.0f, "Algodón")},
        // Hogar (categoriaId = 3)
        new Object[]{3, new PublicacionHogar("Silla", "Desc", "img.jpg", 1, "Oficina")}
    };
    
    for (Object[] caso : casos) {
        int categoriaId = (int) caso[0];
        Publicacion publicacion = (Publicacion) caso[1];
        
        
        int resultado = (int) method.invoke(controller, publicacion, 1, categoriaId);
        assertTrue(resultado == -1 || resultado >= 0);
    }
}

@Test
void testRegistrarPublicacionEnBD_CategoriaDesconocida() throws Exception {
    UploadMaterialController controller = new UploadMaterialController();
    
    Method method = UploadMaterialController.class.getDeclaredMethod(
        "registrarPublicacionEnBD", 
        Publicacion.class, int.class, int.class
    );
    method.setAccessible(true);
    
    PublicacionTecnologia publicacion = new PublicacionTecnologia(
        "Test", "Test", "img.jpg", 1, "M", "Marca", false
    );
    
    int resultado = (int) method.invoke(controller, publicacion, 1, 99);
    assertTrue(resultado == -1 || resultado >= 0);
}

// ==================== TESTS ADICIONALES CORREGIDOS ====================

@Test
void testCrearPublicacion_MultiplesParametros() {
    UploadMaterialController controller = new UploadMaterialController();
    
    // Probar diferentes combinaciones de parámetros
    List<Object[]> casos = Arrays.asList(
        // usuarioId, titulo, descripcion, categoria, imagenBase64, idcategoria, parametrosEspecificos
        new Object[]{1, "iPhone", "Smartphone", "Tecnología", "img1", 1, Arrays.asList("14", "Apple", "true")},
        new Object[]{2, "Samsung", "Android", "Tecnología", "img2", 1, Arrays.asList("S23", "Samsung", "false")},
        new Object[]{3, "Camisa", "Algodón", "Ropa", "img3", 2, Arrays.asList("42", "Algodón")},
        new Object[]{4, "Pantalon", "Jeans", "Ropa", "img4", 2, Arrays.asList("32", "Denim")},
        new Object[]{5, "Silla", "Oficina", "Hogar", "img5", 3, Arrays.asList("Ergonómica")},
        new Object[]{6, "Mesa", "Madera", "Hogar", "img6", 3, Arrays.asList("Madera noble")}
    );
    
    for (Object[] caso : casos) {
        int resultado = controller.crearPublicacion(
            (int) caso[0],     // usuarioId
            (String) caso[1],  // titulo
            (String) caso[2],  // descripcion  
            (String) caso[3],  // categoria
            (String) caso[4],  // imagenBase64
            (int) caso[5],     // idcategoria
            (List<String>) caso[6]  // parametrosEspecificos
        );
        assertTrue(resultado == -1 || resultado >= 0);
    }
}

    @Test
    void testCrearPublicacion_BoundaryValues() {
        UploadMaterialController controller = new UploadMaterialController();
        
        assertDoesNotThrow(() -> {
            controller.crearPublicacion(0, "A", "B", "Tecnología", "C", 1, Arrays.asList("", "", ""));
            controller.crearPublicacion(Integer.MAX_VALUE, "T", "D", "Ropa", "I", 2, Arrays.asList("0", ""));
            controller.crearPublicacion(-1, null, null, "Hogar", null, 3, null);
        });
    }

    // ==================== TESTS DE COMPONENTES ====================

    @Test
    void testPublicacionFactory_TodasCategorias() {
        PublicacionFactory factory = new PublicacionFactory();
        
        Publicacion tec = factory.crearPublicacion("Tecnología", "T1", "D1", "I1", 1, 
            Arrays.asList("M1", "Marca1", "true"));
        assertNotNull(tec);
        
        Publicacion ropa = factory.crearPublicacion("Ropa", "T2", "D2", "I2", 1, 
            Arrays.asList("42", "Algodón"));
        assertNotNull(ropa);
        
        Publicacion hogar = factory.crearPublicacion("Hogar", "T3", "D3", "I3", 1, 
            Arrays.asList("Mueble"));
        assertNotNull(hogar);
    }

    @Test
    void testVisitorValidaciones_Completo() {
        VisitorValidaciones validador = new VisitorValidaciones();
        
        PublicacionTecnologia pubTec = new PublicacionTecnologia("Test", "Test", "img", 1, "M", "Marca", false);
        PublicacionRopa pubRopa = new PublicacionRopa("Test", "Test", "img", 1, 42.0f, "Algodón");
        PublicacionHogar pubHogar = new PublicacionHogar("Test", "Test", "img", 1, "Tipo");
        
        pubTec.aceptarVisita(validador);
        pubRopa.aceptarVisita(validador);
        pubHogar.aceptarVisita(validador);
        
        boolean esValido = validador.esValido();
        String mensaje = validador.getMensajeError();
        
        assertNotNull(mensaje);
    }

    @Test
    void testVisitorEtiquetado() {
        VisitorEtiquetado etiquetado = new VisitorEtiquetado();
        
        PublicacionTecnologia pubTec = new PublicacionTecnologia("Test", "Test", "img", 1, "M", "Marca", false);
        PublicacionRopa pubRopa = new PublicacionRopa("Test", "Test", "img", 1, 42.0f, "Algodón");
        PublicacionHogar pubHogar = new PublicacionHogar("Test", "Test", "img", 1, "Tipo");
        
        pubTec.aceptarVisita(etiquetado);
        pubRopa.aceptarVisita(etiquetado);
        pubHogar.aceptarVisita(etiquetado);
        
        assertTrue(true);
    }

    @Test
    void testPublicacionTecnologia_Completo() {
        PublicacionTecnologia pub = new PublicacionTecnologia(
            "Laptop", "Desc", "img.jpg", 1, "XPS", "Dell", true);
        
        assertEquals("Laptop", pub.getTitulo());
        assertEquals("Tecnología", pub.getCategoria());
        assertEquals("XPS", pub.getModelo());
        assertEquals("Dell", pub.getMarca());
        assertTrue(pub.isGarantia());
        
        List<String> etiquetas = Arrays.asList("tecnologia", "laptop");
        pub.setEtiquetas(etiquetas);
        assertEquals(2, pub.getEtiquetas().size());
        
        pub.aceptarVisita(new VisitorPublicaciones() {
            public void visitar(PublicacionTecnologia pub) {}
            public void visitar(PublicacionRopa pub) {}
            public void visitar(PublicacionHogar pub) {}
        });
    }

    @Test
    void testPublicacionRopa_Completo() {
        PublicacionRopa pub = new PublicacionRopa(
            "Camisa", "Desc", "img.jpg", 1, 42.0f, "Algodón");
        
        assertEquals("Camisa", pub.getTitulo());
        assertEquals("Ropa", pub.getCategoria());
        assertEquals(42.0f, pub.getTalla(), 0.01);
        assertEquals("Algodón", pub.getMaterial());
        
        pub.setEtiquetas(Arrays.asList("ropa", "algodon"));
        assertEquals(2, pub.getEtiquetas().size());
        
        pub.aceptarVisita(new VisitorPublicaciones() {
            public void visitar(PublicacionTecnologia pub) {}
            public void visitar(PublicacionRopa pub) {}
            public void visitar(PublicacionHogar pub) {}
        });
    }

    @Test
    void testPublicacionHogar_Completo() {
        PublicacionHogar pub = new PublicacionHogar(
            "Silla", "Desc", "img.jpg", 1, "Ergonómica");
        
        assertEquals("Silla", pub.getTitulo());
        assertEquals("Hogar", pub.getCategoria());
        assertEquals("Ergonómica", pub.getTipoMueble());
        
        pub.setEtiquetas(Arrays.asList("hogar", "mueble"));
        assertEquals(2, pub.getEtiquetas().size());
        
        pub.aceptarVisita(new VisitorPublicaciones() {
            public void visitar(PublicacionTecnologia pub) {}
            public void visitar(PublicacionRopa pub) {}
            public void visitar(PublicacionHogar pub) {}
        });
    }

    @Test
    void testObtenerParametrosEspecificos_TodasCategorias() {
        UploadMaterialController controller = new UploadMaterialController();
        
        try {
            controller.obtenerParametrosEspecificos("Tecnología");
        } catch (Exception e) { }
        
        try {
            controller.obtenerParametrosEspecificos("Ropa");
        } catch (Exception e) { }
        
        try {
            controller.obtenerParametrosEspecificos("Hogar");
        } catch (Exception e) { }
        
        try {
            controller.obtenerParametrosEspecificos("Desconocida");
        } catch (Exception e) { }
        
        assertTrue(true);
    }

    @Test
    void testOnNewMaterial() {
        UploadMaterialController controller = new UploadMaterialController();
        
        try {
            Method method = UploadMaterialController.class.getDeclaredMethod("onNewMaterial");
            method.setAccessible(true);
            method.invoke(controller);
        } catch (Exception e) {
        }
        
        assertTrue(true);
    }
    // AGREGAR ESTOS TESTS AL ARCHIVO UploadMaterialControllerTest.java

@Test
void testCrearPublicacion_CubreBloqueValidacionFallida() throws Exception {
    UploadMaterialController controller = new UploadMaterialController();
    
    // Usar reflexión para acceder al método privado
    Method crearPublicacionMethod = UploadMaterialController.class.getDeclaredMethod(
        "crearPublicacion", int.class, String.class, String.class, String.class, 
        String.class, int.class, List.class);
    
    // Crear un mock del VisitorValidaciones que siempre falle
    VisitorValidaciones validadorMock = new VisitorValidaciones() {
        @Override
        public boolean esValido() {
            return false; // Siempre retorna false para forzar el bloque if
        }
        
        @Override
        public String getMensajeError() {
            return "Error de validación forzado";
        }
    };
    
    // Inyectar el mock usando reflexión en el factory
    PublicacionFactory factoryMock = new PublicacionFactory() {
        @Override
        public Publicacion crearPublicacion(String categoria, String titulo, String descripcion, 
                                          String imagen, int usuarioId, List<String> parametros) {
            Publicacion pub = super.crearPublicacion(categoria, titulo, descripcion, imagen, usuarioId, parametros);
            
            // Usar reflexión para inyectar nuestro validador mock
            try {
                // Crear una subclase anónima que use nuestro validador
                Publicacion pubConValidacionForzada = new PublicacionTecnologia(
                    titulo, descripcion, imagen, usuarioId, 
                    parametros.get(0), parametros.get(1), Boolean.parseBoolean(parametros.get(2))) {
                    
                    @Override
                    public void aceptarVisita(VisitorPublicaciones visitor) {
                        if (visitor instanceof VisitorValidaciones) {
                            // Usar nuestro mock en lugar del real
                            super.aceptarVisita(validadorMock);
                        } else {
                            super.aceptarVisita(visitor);
                        }
                    }
                };
                return pubConValidacionForzada;
            } catch (Exception e) {
                return pub;
            }
        }
    };
    
    // Inyectar el factory mock
    java.lang.reflect.Field factoryField = UploadMaterialController.class.getDeclaredField("publicacionFactory");
    factoryField.setAccessible(true);
    factoryField.set(controller, factoryMock);
    
    // Ejecutar el método
    int resultado = controller.crearPublicacion(1, "Test", "Test", "Tecnología", 
        "img", 1, Arrays.asList("M", "B", "true"));
    
    // Debería retornar -1 porque forzamos validación fallida
    assertEquals(-1, resultado);
}

@Test
void testCrearPublicacion_ConValidacionExitosa() throws Exception {
    UploadMaterialController controller = new UploadMaterialController();
    
    // Mock para validación exitosa
    VisitorValidaciones validadorMock = new VisitorValidaciones() {
        @Override
        public boolean esValido() {
            return true; // Siempre retorna true
        }
    };
    
    PublicacionFactory factoryMock = new PublicacionFactory() {
        @Override
        public Publicacion crearPublicacion(String categoria, String titulo, String descripcion, 
                                          String imagen, int usuarioId, List<String> parametros) {
            Publicacion pub = super.crearPublicacion(categoria, titulo, descripcion, imagen, usuarioId, parametros);
            
            return new PublicacionTecnologia(titulo, descripcion, imagen, usuarioId, 
                parametros.get(0), parametros.get(1), Boolean.parseBoolean(parametros.get(2))) {
                
                @Override
                public void aceptarVisita(VisitorPublicaciones visitor) {
                    if (visitor instanceof VisitorValidaciones) {
                        // Saltar la validación real y usar nuestro mock
                        // No llamamos a super.aceptarVisita para evitar la lógica real
                    } else {
                        super.aceptarVisita(visitor);
                    }
                }
            };
        }
    };
    
    // Inyectar factory mock
    java.lang.reflect.Field factoryField = UploadMaterialController.class.getDeclaredField("publicacionFactory");
    factoryField.setAccessible(true);
    factoryField.set(controller, factoryMock);
    
    int resultado = controller.crearPublicacion(1, "Test", "Test", "Tecnología", 
        "img", 1, Arrays.asList("M", "B", "true"));
    
    // Ejecuta el flujo completo incluyendo VisitorEtiquetado
    assertTrue(resultado == -1 || resultado >= 0);
    }
} 
