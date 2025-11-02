package com.example.greenet;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;

public class UploadMaterialController {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private ImageView previewImage;

    @FXML private TextField modeloField;
    @FXML private TextField marcaField;
    @FXML private CheckBox garantiaCheck;
    @FXML private TextField tallaField;
    @FXML private TextField materialField;
    @FXML private TextField tipoMuebleField;

    private byte[] imagenBytes;
    private String imagenBase64Temp;
    private final int usuarioIdActual = 1;
    private PublicacionFactory publicacionFactory;

    @FXML
    public void initialize() {
        publicacionFactory = new PublicacionFactory();
        categoryCombo.getItems().addAll("Tecnología", "Ropa", "Hogar");
        categoryCombo.valueProperty().addListener((obs, oldVal, newVal) -> mostrarCamposEspecificos(newVal));
        ocultarTodosLosCampos();
    }
     private void mostrarCamposEspecificos(String categoria) {
        ocultarTodosLosCampos();
        switch (categoria) {
            case "Tecnología" -> {
                modeloField.setVisible(true);
                marcaField.setVisible(true);
                garantiaCheck.setVisible(true);
            }
            case "Ropa" -> {
                tallaField.setVisible(true);
                materialField.setVisible(true);
            }
            case "Hogar" -> tipoMuebleField.setVisible(true);
        }
    }

    private void ocultarTodosLosCampos() {
        modeloField.setVisible(false);
        marcaField.setVisible(false);
        garantiaCheck.setVisible(false);
        tallaField.setVisible(false);
        materialField.setVisible(false);
        tipoMuebleField.setVisible(false);
    }

    @FXML
    private void onUploadImage() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            previewImage.setImage(image);

            imagenBytes = Files.readAllBytes(file.toPath());
            imagenBase64Temp = FachadaImagen.codificarImagenAString(imagenBytes);
        }
    }

    @FXML
    private void onSubmit() {
        String titulo = titleField.getText().trim();
        String descripcion = descriptionArea.getText().trim();
        String categoria = categoryCombo.getValue();

        if (titulo.isEmpty() || descripcion.isEmpty() || categoria == null || imagenBase64Temp == null) {
            mostrarAlerta("Campos incompletos", "Por favor completa todos los campos incluyendo la imagen.");
            return;
        }

        int resultado = crearPublicacion(
                usuarioIdActual,
                titulo,
                descripcion,
                categoria,
                imagenBase64Temp,
                obtenerParametrosEspecificos(categoria)
        );

        if (resultado > 0) {
            mostrarAlerta("Éxito", "Publicación creada correctamente con ID: " + resultado);
            limpiarFormulario();
        } else {
            mostrarAlerta("Error", "No se pudo crear la publicación.");
        }
    }

    private int crearPublicacion(int usuarioId, String titulo, String descripcion,
                                 String categoria, String imagenBase64, String... parametrosEspecificos) {
        try {
            Publicacion publicacion = publicacionFactory.crearPublicacion(
                    categoria, titulo, descripcion, imagenBase64, usuarioId, parametrosEspecificos
            );

            VisitorValidaciones validador = new VisitorValidaciones();
            publicacion.aceptar(validador);
            if (!validador.esValido()) {
                mostrarAlerta("Error de validación", validador.getMensajeError());
                return -1;
            }

            VisitorEtiquetado visitorEtiquetado = new VisitorEtiquetado();
            publicacion.aceptar(visitorEtiquetado);

            return registrarPublicacionEnBD(publicacion, usuarioId);

        } catch (Exception e) {
            System.err.println("Error al crear publicación: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    private int registrarPublicacionEnBD(Publicacion publicacion, int usuarioId) {
        String sql = "{ ? = call crear_publicacion(?, ?, ?, ?, ?) }";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, usuarioId);
            stmt.setString(3, publicacion.getTitulo());
            stmt.setString(4, publicacion.getDescripcion());
            stmt.setString(5, publicacion.getCategoria());
            stmt.setString(6, publicacion.getImagen());
            stmt.execute();

            return stmt.getInt(1);
        } catch (Exception e) {
            System.err.println("Error al registrar publicación en BD: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    private String[] obtenerParametrosEspecificos(String categoria) {
        return switch (categoria) {
            case "Tecnología" -> new String[]{
                    modeloField.getText(),
                    marcaField.getText(),
                    String.valueOf(garantiaCheck.isSelected())
            };
            case "Ropa" -> new String[]{
                    tallaField.getText(),
                    materialField.getText()
            };
            case "Hogar" -> new String[]{tipoMuebleField.getText()};
            default -> new String[]{};
        };
    }

    @FXML
    private void onNewMaterial() {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        titleField.clear();
        descriptionArea.clear();
        categoryCombo.getSelectionModel().clearSelection();
        previewImage.setImage(null);
        imagenBytes = null;
        imagenBase64Temp = null;
        modeloField.clear();
        marcaField.clear();
        garantiaCheck.setSelected(false);
        tallaField.clear();
        materialField.clear();
        tipoMuebleField.clear();
        ocultarTodosLosCampos();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}


   
