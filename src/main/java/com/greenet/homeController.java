package com.greenet;

import com.greenet.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class homeController {

    @FXML
    public Button buscarButton;

    @FXML
    public Button publicarButton;

    @FXML
    public Button perfilButton;

    @FXML
    public Button salirButton;

    public int usuarioId;
    public String correo;

    // ✅ Variable para ignorar ventanas/alerts durante tests
    public boolean modoTest = false;

    public void setUsuarioActual(int usuarioId, String correoUsuario) {
        this.usuarioId = usuarioId;
        this.correo = correoUsuario;
        System.out.println("✅ Usuario cargado en Settings: ID=" + usuarioId + ", Correo=" + correoUsuario);
    }

    @FXML
    public void onGoToBuscar() {
        System.out.println("Botón 'Buscar' presionado");

        if (modoTest) return; // Ignorar apertura de FXML en test

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenet/ProductSearch.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) buscarButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Buscar");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onGoToPublicaciones() {
        System.out.println("Botón 'Publicar' presionado");

        if (modoTest) return; // Ignorar apertura de FXML en test

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenet/upload_material.fxml"));
            Scene scene = new Scene(loader.load());

            UploadMaterialController controller = loader.getController();
            controller.setUsuarioActual(usuarioId);
            Stage stage = (Stage) publicarButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Publicaciones");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onActionIralPerfil() {
        System.out.println("Botón 'Perfil' presionado");
        System.out.println(usuarioId + correo);

        if (modoTest) return; // Ignorar apertura de FXML en test

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/greenet/Profile.fxml"));
            Parent root = loader.load();
            SettingsProfileController controller = loader.getController();
            controller.setUsuarioActual(usuarioId, correo);
            Stage stage = (Stage) perfilButton.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 600));
            stage.setTitle("GREENET - HOME ");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onGoToSalir() {
        if (modoTest) return; // Ignorar Alert y Stage en test

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cerrar Sesión");
        confirmacion.setHeaderText("¿Desea cerrar sesión?");
        confirmacion.setContentText("Será redirigido a la pantalla de inicio de sesión");

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            cerrarSesionBD();
            mostrarAlerta("Sesión cerrada", "Gracias por usar GREENET");
            volverAlLogin();
        }
    }

    public void cerrarSesionBD() {
        int resultado = UsuarioService.cerrarSesion(usuarioId);

        switch (resultado) {
            case 0 -> System.out.println("✅ Sesión cerrada en BD para usuario ID: " + usuarioId);
            case 1 -> System.out.println("⚠ No había sesión activa para cerrar");
            default -> System.err.println("❌ Error al cerrar sesión. Código: " + resultado);
        }
    }

    void volverAlLogin() {
        if (modoTest) return; // Ignorar Stage en test

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("LOGIN.fxml")
            ));

            Stage stage = (Stage) salirButton.getScene().getWindow();
            stage.setScene(new Scene(root, 354, 600));
            stage.setTitle("GREENET - Login");

            System.out.println("✅ Redirigido a login");

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al volver al login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void mostrarAlerta(String titulo, String mensaje) {
        if (modoTest) {
            System.out.println("⚡ Alerta ignorada en test: " + titulo + " - " + mensaje);
            return;
        }

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
}