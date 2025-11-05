package com.greenet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import com.greenet.service.UsuarioService;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class Vista_Admin_BloqueosController extends Publisher implements Suscribe {
    String azul = "#0000FF";
    @FXML
    private Text TxtUsuarioSeleccionado;
    @FXML
    private Button btnQuitarRestriccion;
    @FXML
    private Button btnhome;
    @FXML
    private Button btnBloquear;
    @FXML
    private Button btnVolver;

    @FXML
    private ComboBox<String> cbUsuariosRestringidos;

    @FXML
    private Text txtNumeroUsuarios;
    private int  usuarioId;

    @FXML
    public void initialize() { // idea usarlo como un metodo externo para que vuleva a cargar
        // Aca llenas lo del usuario con un select nombre usuario o sombre where estatus == 2
        cbUsuariosRestringidos.getItems().addAll("Bloqueduser@gmail.com", "Usuario 2", "Usuario 3","usuario 5 ");
        double h = 3000000;
        // Aqui si quieres pones lo del select * from usuarios whre id = 1  osea lo de que sena usuarios
        txtNumeroUsuarios.setText(String.valueOf(h));
        String[][] datosAdmins = {
                {"Mateo", "mate12185@gmail.com", "3150639689"},
                {"Samuel", "samuelreyparaps4@gmail.com", "3167672300"}
        };
 // se simula como quedarian guardado los administardores 
        for (String[] datos : datosAdmins) {
            String nombre = datos[0];
            String correo = datos[1];
            Long telefono = Long.parseLong(datos[2]);

            Admin admin = new Admin(nombre, correo, telefono);
            this.suscribir(admin);
            System.out.println("Suscrito admin: " + nombre + " - " + correo + " - " + telefono);
        }


        System.out.println("Total de administradores suscritos: " + datosAdmins.length);
    }

    @FXML
    public void onUsuarioSeleccionado(ActionEvent event) {
        String seleccionado = cbUsuariosRestringidos.getSelectionModel().getSelectedItem();
        TxtUsuarioSeleccionado.setText(seleccionado);
    }

    @FXML
    public void OnActionBloquearUusario(ActionEvent actionEvent) {
        String usuario = cbUsuariosRestringidos.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            int usuarioID=UsuarioService.BuscarId(usuario);
            System.out.println(usuarioID);
            boolean verificacion=UsuarioService.bloquearUsuario(usuarioID);

            notificarSubs("Por parte del cuerpo de greenet corporation se nos permite informar que el cliente con correo : " + usuario + " ha sido bloqueado ");
            Alert alert = new Alert(AlertType.CONFIRMATION);
            if(verificacion) {
                alert.setTitle("Usuario Bloqueado");
                alert.setContentText("La cuenta " + usuario + " Fue bloqueado correctamente");
                alert.showAndWait();
                cbUsuariosRestringidos.getItems().remove(usuario);
            }

        }
        if (usuario == null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Usuario No Valido");
            alert.setContentText("Porfavor escoja un usuario valido para continuar");
            alert.showAndWait();

        }
    }

    public void OnActionQuitarRestriccionUusario(ActionEvent actionEvent) {

        String usuario = cbUsuariosRestringidos.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            // Aqui mete lo de de cambio de 1 a 0 para que ya no este en revision
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Usuario liberado");
            alert.setContentText("El " + usuario + " Se le quito la restriccion de manera adecuada");
            alert.showAndWait();
            cbUsuariosRestringidos.getItems().remove(usuario);

        }
        if (usuario == null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Usuario No Valido");
            alert.setContentText("Porfavor escoja un usuario valido para continuar");
            alert.showAndWait();

        }

    }

    public void OnActionVolverAtras(ActionEvent actionEvent) {
        System.out.println("Salio del la pantalla gracias por usarla");
        // Aqui se conecta con la pantalla anterior
    }

@Override
public void actualizar(String mensaje) {
    System.out.println("AdminBloqueos recibió notificación: " + mensaje);
    if (mensaje.contains("bloqueado")) {
        System.out.println("AdminBloqueos: acción tomada tras recibir mensaje de bloqueo.");
    }
}


    @FXML
    public void OnActionVolver() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cerrar Sesión");
        confirmacion.setHeaderText("¿Desea cerrar sesión?");
        confirmacion.setContentText("Será redirigido a la pantalla de inicio de sesión");

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // ✅ NUEVO: Usar UsuarioService en lugar de función de BD
            cerrarSesionBD();
            mostrarAlerta("Sesión cerrada", "Gracias por usar GREENET");
            volverAlLogin();
        }
    }

    private void cerrarSesionBD() {
        int resultado = UsuarioService.cerrarSesion(usuarioId);

        switch (resultado) {
            case 0 -> System.out.println("✅ Sesión cerrada en BD para admin: ");
            case 1 -> System.out.println("⚠️ No había sesión activa para cerrar");
            default -> System.err.println("❌ Error al cerrar sesión. Código: " + resultado);
        }
    }
    private void volverAlLogin() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("LOGIN.fxml")
            ));

            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root, 354, 600));
            stage.setTitle("GREENET - Login");

            System.out.println("✅ Redirigido a login");

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al volver al login: " + e.getMessage());
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

    public void setUsuarioActual(int usuarioId) {
        this.usuarioId = usuarioId;
    }
}






// el  deberia tener estas proporciones  356, 65
