package com.greenet;
import com.greenet.Publicacion;

import com.greenet.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import  com.greenet.service.UsuarioService;

public class ProductSearchController {

    @FXML
    private Button btnVolver;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> productCombo;

    private List<Publicacion> publicacionesBase = new ArrayList<>();
    @FXML
    public void initialize() {
        publicacionesBase=UsuarioService.ConsultarProductosDisponibles();
        actualizarCombo();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filtrarProductos());
        productCombo.setOnAction(e -> abrirPreview());
    }

    private void actualizarCombo() {
        productCombo.getItems().clear();
        for (Publicacion p : publicacionesBase) {
            productCombo.getItems().add(p.getTitulo());
        }
    }

    private void filtrarProductos() {
        String query = searchField.getText().trim().toLowerCase();
        productCombo.getItems().clear();
        for (Publicacion p : publicacionesBase) {
            if (p.getTitulo().toLowerCase().contains(query)) {
                productCombo.getItems().add(p.getTitulo());
            }
        }
        if (!productCombo.getItems().isEmpty()) {
            productCombo.show();
        }
    }

    private void abrirPreview() {
        String seleccionado = productCombo.getValue();
        if (seleccionado == null) return;

        Publicacion pub = publicacionesBase.stream()
                .filter(p -> p.getTitulo().equals(seleccionado))
                .findFirst()
                .orElse(null);

        if (pub == null) return;
        // pero igual es poquito ; si mi vida dormi hace unas 4 h  unos 30 min
        // descansa yo puedo seguir solito

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("preview_post.fxml"));
            Parent root = loader.load();
            PreviewPostController controller = loader.getController();
            String base64Imagen = pub.getImagen();
            System.out.println();
            String descripcion = UsuarioService.BuscarDescripcion(pub.getTitulo());
            System.out.println(descripcion);
            Integer publicador = UsuarioService.BuscarPublicadorId(pub.getTitulo());
            System.out.println(publicador);
            String nombre = UsuarioService.BuscarNombrePorId(publicador);

            controller.cargarPublicacion(pub.getTitulo(),nombre, descripcion, base64Imagen);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onRefresh() {
        searchField.clear();
        actualizarCombo();
    }

    @FXML
    private void onBack() {
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

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert;

        if ("Éxito".equals(titulo)) {
            alert = new Alert(Alert.AlertType.INFORMATION);
        } else if ("Error".equals(titulo)) {
            alert = new Alert(Alert.AlertType.ERROR);
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
        }

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void onSearch() {
        System.out.println("Buscando a nemo??? ");
    }

}

