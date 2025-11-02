package com.example.greenet;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;

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

    private byte[] imagenBytes;
    private String imagenBase64Temp;
    private final int usuarioIdActual = 1;
    private PublicacionFactory publicacionFactory;

    @FXML
    public void initialize() {
        publicacionFactory = new PublicacionFactory();
        categoryCombo.getItems().addAll("Tecnología", "Ropa", "Hogar");
        categoryCombo.valueProperty().addListener((obs, oldVal, newVal) -> mostrarCamposEspecificos(newVal));
        ocultarTodosLosCampos();
    }

   
