package com.greenet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

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

    @FXML private Button btnVolver;

    private byte[] imagenBytes;
    private String imagenBase64Temp;
    private final int usuarioIdActual = 2;
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
            publicacion.aceptarVisita(validador);
            if (!validador.esValido()) {
                mostrarAlerta("Error de validación", validador.getMensajeError());
                return -1;
            }

            VisitorEtiquetado visitorEtiquetado = new VisitorEtiquetado();
            publicacion.aceptarVisita(visitorEtiquetado);

            return registrarPublicacionEnBD(publicacion, usuarioId);

        } catch (Exception e) {
            System.err.println("Error al crear publicación: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
/*
    private int registrarPublicacionEnBD(Publicacion publicacion, int usuarioId) {
        String sql = "INSERT INTO publicaciones (titulo, descripcion, categoria, usuario_id, fecha) VALUES (?, ?, ?, ?, ?)";

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
*/
private int registrarPublicacionEnBD(Publicacion publicacion, int usuarioId) {
    String sql = """
        INSERT INTO publicaciones 
        (titulo, descripcion, categoria_id, imagen, publicador_id, estados, fecha_publicacion)
        VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
    """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


        stmt.setString(1, publicacion.getTitulo());
        stmt.setString(2, publicacion.getDescripcion());
        stmt.setInt(3, 2);
        stmt.setString(4, publicacion.getImagen());
        stmt.setInt(5, usuarioId);
        stmt.setInt(6, 1);
        int filas = stmt.executeUpdate();
        if (filas == 0) {
            System.err.println("⚠ No se insertó ninguna publicación.");
            return -1;
        }


        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

    } catch (SQLException e) {
        System.err.println("❌ Error al registrar publicación en BD: " + e.getMessage());
        e.printStackTrace();
    }

    return -1;
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

    @FXML
    public void onGoBack() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("Home.fxml")
            ));

            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root, 354, 600));
            stage.setTitle("GREENET - home");

            System.out.println("✅ Redirigido a home");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al regresar a la ventana principal");
            e.printStackTrace();
        }
    }
}


   
