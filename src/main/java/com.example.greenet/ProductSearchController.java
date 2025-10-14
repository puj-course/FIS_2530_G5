package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ProductSearchController {

    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> productList;

    @FXML
    private void onSearch() {
        String query = searchField.getText().trim().toLowerCase();
        productList.getItems().clear();

        if (query.isEmpty()) {
            productList.getItems().addAll("Camisa futbol colombiano", "Lámpara de oso", "Bolso coach sin correa");
        } else {
            productList.getItems().addAll("Camisa futbol colombiano", "Lámpara de oso", "Bolso coach sin correa");
            productList.getItems().removeIf(item -> !item.toLowerCase().contains(query));
        }
    }

    @FXML
    private void onRefresh() {
        searchField.clear();
        productList.getItems().setAll("Camisa futbol colombiano", "Lámpara de oso", "Bolso coach sin correa");
    }

    @FXML
    private void onBack() {
        System.out.println("Volviendo a la pantalla anterior...");
    }
}
