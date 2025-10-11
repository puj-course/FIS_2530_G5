package com.example.greenet;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.nio.file.Paths;

public class UploadMaterialController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private ImageView previewImage;
    int i = 0;


    private String imagenBase64Temp;

    private ControladorPublicaciones controladorPublicaciones;

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
        controladorPublicaciones = new ControladorPublicaciones();
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
            Image image = new Image(file.toURI().toString());
            previewImage.setImage(image);

            byte[] imageBytes = Files.readAllBytes(file.toPath());


            String base64 = Base64.getEncoder().encodeToString(imageBytes);





            this.imagenBase64Temp = base64;





            String nom = "imagen_restaurada_" + i + ".png";


            Files.write(Paths.get("src/main/resources/com/example/subir_material/"+nom), imageBytes);


            i++;

        }

    }

    @FXML
    private void onSubmit() {
        String titulo = titleField.getText().trim();
        String descripcion = descriptionArea.getText().trim();
        String categoria = categoryCombo.getValue();

        if (titulo.isEmpty() || descripcion.isEmpty() || categoria == null || imagenBytes == null) {
            mostrarAlerta("Campos incompletos", "Por favor completa todos los campos incluyendo la imagen.");
            return;
        }
        if (imagenBase64Temp == null) {


            mostrarAlerta("Imagen faltante", "Por favor sube una imagen primero.");


            return;

        }

        try {
            // Crear publicación usando ControladorPublicaciones
            int resultado = controladorPublicaciones.crearPublicacion(
                    usuarioIdActual,
                    titulo,
                    descripcion,
                    categoria,
                    imagenBytes,
                    obtenerParametrosEspecificos(categoria)
            );

            if (resultado > 0) {
                mostrarAlerta("Éxito", "Publicación creada correctamente con ID: " + resultado);
                limpiarFormulario();
            } else if (resultado == -2) {
                mostrarAlerta("Error de validación", "Por favor completa todos los campos correctamente.");
            } else {
                mostrarAlerta("Error", "No se pudo crear la publicación. Código error: " + resultado);
            }

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al crear publicación: " + e.getMessage());
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