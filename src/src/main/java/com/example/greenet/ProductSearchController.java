package com.example.greenet;
import com.example.greenet.Publicacion;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductSearchController {

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> productCombo;

    private final List<Publicacion> publicacionesBase = new ArrayList<>();

    @FXML
    public void initialize() {
        // Ejemplo: añadir algunas publicaciones de distintas categorías
        publicacionesBase.add(new PublicacionTecnologia("Laptop Gaming","Laptop reacondicionada con garantía",FachadaImagen.codificarImagenAString(new byte[0]), 1, "ModeloY", "MarcaX", true));
        publicacionesBase.add(new PublicacionRopa("Camisa futbol colombiano", "Camiseta oficial de la selección", FachadaImagen.codificarImagenAString(new byte[0]), 1, 42.0f, "Algodón"));
        publicacionesBase.add(new PublicacionHogar("Lámpara de oso", "Lámpara decorativa restaurada", FachadaImagen.codificarImagenAString(new byte[0]), 1, "Madera"));

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

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PreviewPost.fxml"));
            Parent root = loader.load();
            PreviewPostController controller = loader.getController();

            String base64Imagen = pub.getImagen();
            controller.cargarPublicacion(pub.getTitulo(), "Usuario Ejemplo", pub.getDescripcion(), base64Imagen);

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
        System.out.println("Volviendo a la pantalla anterior...");
    }
}
