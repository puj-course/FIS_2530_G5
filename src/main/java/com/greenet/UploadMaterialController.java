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
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
// como el setting no guarda el id cuando sale se reinicia
public class UploadMaterialController {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> categoryCombo;
    @FXML
    ImageView previewImage;

    @FXML private TextField modeloField;
    @FXML private TextField marcaField;
    @FXML private CheckBox garantiaCheck;
    @FXML private TextField tallaField;
    @FXML private TextField materialField;
    @FXML private TextField tipoMuebleField;

    @FXML private Button btnVolver;

    private byte[] imagenBytes;
    private String imagenBase64Temp;
    private  int usuarioIdActual ;
    public PublicacionFactory publicacionFactory;

    @FXML
    public void initialize() {
        publicacionFactory = new PublicacionFactory();
        categoryCombo.getItems().addAll("Tecnología", "Ropa", "Hogar");
        categoryCombo.valueProperty().addListener((obs, oldVal, newVal) -> mostrarCamposEspecificos(newVal));
        ocultarTodosLosCampos();
    }
    public void setUsuarioActual(int usuarioId) {
        this.usuarioIdActual = usuarioId;
        System.out.println("✅ Usuario cargado en Settings: ID=" + usuarioId);
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
                new FileChooser.ExtensionFilter("Imágenes", ".png", ".jpg", "*.jpeg")
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
        List<String> publicaciones=new ArrayList<>();
        String titulo = titleField.getText().trim();
        String descripcion = descriptionArea.getText().trim();
        String categoria = categoryCombo.getValue();

        if (titulo.isEmpty() || descripcion.isEmpty() || categoria == null || imagenBase64Temp == null) {
            mostrarAlerta("Campos incompletos", "Por favor completa todos los campos incluyendo la imagen.");
            return;
        }

        int categoriaId = 0;

        // 🔹 Asignar ID según la categoría seleccionada
        switch (categoria.toLowerCase()) {
            case "tecnologia":
            case "tecnología":
                categoriaId = 1;
                publicaciones.add (modeloField.getText());
                publicaciones.add (marcaField.getText());
                if(garantiaCheck.isSelected()){
                    publicaciones.add("True");
                }else {
                    publicaciones.add("False");
                }
                break;
            case "ropa":
                categoriaId = 2;
                publicaciones.add (tallaField.getText());
                publicaciones.add (materialField.getText());
                break;
            case "hogar":
                categoriaId = 3;
                publicaciones.add (tipoMuebleField.getText());
                publicaciones.add (materialField.getText());
                break;
            default:
                mostrarAlerta("Error", "Categoría no válida seleccionada.");
                return;
        }

        int resultado = crearPublicacion(usuarioIdActual, titulo, descripcion, categoria, imagenBase64Temp,categoriaId,publicaciones);

        if (resultado > 0) {
            mostrarAlerta("Éxito", "Publicación creada correctamente con ID: " + resultado);
            limpiarFormulario();
        } else {
            mostrarAlerta("Error", "No se pudo crear la publicación.");
        }
    }


    public int crearPublicacion(int usuarioId, String titulo, String descripcion, String categoria, String imagenBase64, int idcategoria, List<String> parametrosEspecificos) {
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

            return registrarPublicacionEnBD(publicacion, usuarioId,idcategoria);

        } catch (Exception e) {
            System.err.println("Error al crear publicación: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }



    private int registrarPublicacionEnBD(Publicacion publicacion, int usuarioId, int categoriaId) {
        String sqlPublicacion = """
        INSERT INTO publicaciones 
        (titulo, descripcion, categoria_id, imagen, publicador_id, fecha_publicacion)
        VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlPublicacion, Statement.RETURN_GENERATED_KEYS)) {


            stmt.setString(1, publicacion.getTitulo());
            stmt.setString(2, publicacion.getDescripcion());
            stmt.setInt(3, categoriaId);
            stmt.setString(4, publicacion.getImagen());
            stmt.setInt(5, usuarioId);

            int filas = stmt.executeUpdate();
            if (filas == 0) {
                System.err.println(" No se insertó ninguna publicación.");
                return -1;
            }


            int idPublicacion = -1;
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    idPublicacion = rs.getInt(1);
                }
            }

            if (idPublicacion == -1) {
                System.err.println(" No se pudo obtener el ID de la publicación.");
                return -1;
            }


            String[] parametrosEspecificos = switch (publicacion.getCategoria()) {
                case "Tecnología", "tecnologia" -> new String[]{
                        modeloField.getText(),
                        marcaField.getText(),
                        String.valueOf(garantiaCheck.isSelected())
                };
                case "Ropa", "ropa" -> new String[]{
                        tallaField.getText(),
                        materialField.getText()
                };
                case "Hogar", "hogar" -> new String[]{
                        tipoMuebleField.getText()
                };
                default -> new String[]{};
            };

            switch (categoriaId) {
                case 1 -> {
                    String sqlTec = "INSERT INTO publicacion_tecnologia (id_publicacion, modelo, marca, garantia) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement stmtTec = conn.prepareStatement(sqlTec)) {
                        stmtTec.setInt(1, idPublicacion);
                        stmtTec.setString(2, parametrosEspecificos[0]);
                        stmtTec.setString(3, parametrosEspecificos[1]);
                        stmtTec.setBoolean(4, Boolean.parseBoolean(parametrosEspecificos[2]));
                        stmtTec.executeUpdate();
                    }
                }
                case 2 -> { // Ropa
                    String sqlRopa = "INSERT INTO publicacion_ropa (id_publicacion, talla, material) VALUES (?, ?, ?)";
                    try (PreparedStatement stmtRopa = conn.prepareStatement(sqlRopa)) {
                        stmtRopa.setInt(1, idPublicacion);
                        stmtRopa.setInt(2, Integer.parseInt(parametrosEspecificos[0]));
                        stmtRopa.setString(3, parametrosEspecificos[1]);
                        stmtRopa.executeUpdate();
                    }
                }
                case 3 -> { // Hogar
                    String sqlHogar = "INSERT INTO publicacion_hogar (id_publicacion, tipo_mueble) VALUES (?, ?)";
                    try (PreparedStatement stmtHogar = conn.prepareStatement(sqlHogar)) {
                        stmtHogar.setInt(1, idPublicacion);
                        stmtHogar.setString(2, parametrosEspecificos[0]);
                        stmtHogar.executeUpdate();
                    }
                }
            }

            System.out.println("✅ Publicación registrada con ID " + idPublicacion + " y detalles según categoría.");
            return idPublicacion;

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar publicación: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }


    public String[] obtenerParametrosEspecificos(String categoria) {
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

    public void mostrarAlerta(String titulo, String mensaje) {
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
