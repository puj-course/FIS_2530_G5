package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Base64;

public class UploadMaterialController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private ImageView previewImage;
    int i = 0;
    private String imagenBase64Temp;

    @FXML
    public void initialize() {
        categoryCombo.getItems().addAll("Hogar", "Tecnología", "Ropa");
    }

    @FXML
    private void onUploadImage() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg")
        );
        
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            previewImage.setImage(new Image(file.toURI().toString()));
            
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
    String titulo = titleField.getText();
    String descripcion = descriptionArea.getText();
    String categoria = categoryCombo.getValue();

    if (titulo.isEmpty() || descripcion.isEmpty() || categoria == null) {
        mostrarAlerta("Campos incompletos", "Por favor completa todos los campos.");
        return;
    }

    if (imagenBase64Temp == null) {
        mostrarAlerta("Imagen faltante", "Por favor sube una imagen primero.");
        return;
    }

    try (Connection conn = DatabaseConnection.getConnection()) {
        
        String sql = "SELECT registrar_material(?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, titulo);
            pstmt.setString(2, descripcion);
            pstmt.setString(3, categoria);
            
            pstmt.setString(4, imagenBase64Temp);
            
            pstmt.setInt(5, 1); // ID del publicador que no se como obtener
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int resultado = rs.getInt(1);
                    
                    if (resultado == 0) {
                        mostrarAlerta("Confirmado", "Publicación registrada ");
                        onNewMaterial();
                    } else {
                        mostrarAlerta("Error", "Error");
                    }
                }
            }
        }
        
    } catch (Exception e) {
        mostrarAlerta("Base de datos falló", "Error: " + e.getMessage());
        e.printStackTrace();
    }
}

       

    @FXML
    private void onNewMaterial() {
        titleField.clear();
        descriptionArea.clear();
        categoryCombo.getSelectionModel().clearSelection();
        previewImage.setImage(null);
        imagenBase64Temp = null;
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}
