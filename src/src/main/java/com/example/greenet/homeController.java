package com.example.pantalla_inicio;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class homeController {

    @FXML
    private Button buscarButton;

    @FXML
    private Button publicarButton;

    @FXML
    private void onGoToBuscar() {
        System.out.println("Botón 'Buscar' presionado");
      
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pantalla_inicio/upload_material.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) buscarButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Buscar");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onGoToPublicaciones() {
        System.out.println("Botón 'Publicar' presionado");
     
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pantalla_inicio/ProductSearch.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) publicarButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Publicaciones");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
