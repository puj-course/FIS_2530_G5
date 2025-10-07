package com.example.greenet;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UploadMaterialController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private ImageView previewImage;

    // Campos específicos
    @FXML private TextField modeloField;
    @FXML private TextField marcaField;
    @FXML private CheckBox garantiaCheck;
    @FXML private TextField tallaField;
    @FXML private TextField materialField;
    @FXML private TextField tipoMuebleField;

    private byte[] imagenBytes;
    private int usuarioIdActual = 1;

    @FXML
    public void initialize() {
        categoryCombo.getItems().addAll("Tecnología", "Ropa", "Hogar");
        categoryCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            mostrarCamposEspecificos(newVal);
        });
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
            previewImage.setImage(new Image(file.toURI().toString()));
            this.imagenBytes = Files.readAllBytes(file.toPath());
        }
    }

    @FXML
    private void onSubmit() {
        String titulo = titleField.getText().trim();
        String descripcion = descriptionArea.getText().trim();
        String categoria = categoryCombo.getValue();

        if (titulo.isEmpty() || descripcion.isEmpty() || categoria == null || imagenBytes == null) {
            mostrarAlerta("Campos incompletos", "Por favor completa todos los campos.");
            return;
        }

        try {
            // Crear material usando MaterialFactory
            Material material = MaterialFactory.crearMaterial(
                    categoria, titulo, descripcion, imagenBytes, usuarioIdActual,
                    obtenerParametrosEspecificos(categoria)
            );

            if (!material.validar()) {
                mostrarAlerta("Error", "Por favor completa todos los campos correctamente.");
                return;
            }

            // Registrar en BD
            int resultado = registrarMaterialEnBD(material);
            if (resultado == 0) {
                mostrarAlerta("Éxito", "Material publicado correctamente");
                limpiarFormulario();
            } else {
                mostrarAlerta("Error", "No se pudo publicar el material.");
            }

        } catch (Exception e) {
            mostrarAlerta("Error", "Error: " + e.getMessage());
            e.printStackTrace();
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

    private int registrarMaterialEnBD(Material material) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT registrar_material(?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, material.getTitulo());
                pstmt.setString(2, material.getDescripcion());
                pstmt.setString(3, material.getCategoria());
                pstmt.setBytes(4, material.getImagen());
                pstmt.setInt(5, material.getPublicadorId());

                try (ResultSet rs = pstmt.executeQuery()) {
                    return rs.next() ? rs.getInt(1) : -1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
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