package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.List;

public class myPublicationsController {

    @FXML
    private VBox publicationsContainer;

    @FXML
    public void initialize() {
        List<Publication> publicaciones = new ArrayList<>();
        publicaciones.add(new Publication("Chaqueta de cuero negra", "Chaqueta en perfecto estado, de marca pollito.", "/images/chaqueta.jpeg"));
        publicaciones.add(new Publication("Nevera Haceb", "Nevera 6 meses de uso, una sola puerta.", "/images/nevera.jpg"));
        publicaciones.add(new Publication("Blusa babydoll crochet", "Top tipo babydoll en crochet color morado. ", "/images/blusa.jpeg"));
        publicaciones.add(new Publication("Televisor Samsung", "Uso por 1 año, sin defectos en funcionamiento ni externos.", "/images/televisor.jpg"));

        for (Publication p : publicaciones) {
            publicationsContainer.getChildren().add(createPublicationCard(p));
        }
    }

    private static class Publication {
        String titulo;
        String descripcion;
        String imagenPath;

        Publication(String titulo, String descripcion, String imagenPath) {
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.imagenPath = imagenPath;
        }
    }

    private HBox createPublicationCard(Publication pub) {
        HBox card = new HBox(10);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-padding: 10; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Image image = new Image(getClass().getResource(pub.imagenPath).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);

        VBox infoBox = new VBox(6);
        Label title = new Label(pub.titulo);
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");
        Label description = new Label(pub.descripcion);
        description.setWrapText(true);
        description.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

        Button delete = new Button("Eliminar");
        delete.setStyle("-fx-background-color: #E57373; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 11px;");

        delete.setOnAction(e -> {
            publicationsContainer.getChildren().remove(card);
            showAlert(pub.titulo);
        });

        infoBox.getChildren().addAll(title, description, delete);
        card.getChildren().addAll(imageView, infoBox);

        return card;
    }

    private void showAlert(String titulo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Publicación eliminada");
        alert.setHeaderText(null);
        alert.setContentText("La publicación \"" + titulo + "\" ha sido eliminada.");
        alert.showAndWait();
    }
}
