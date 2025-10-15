package com.example.padron_decorador_modificado;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Vista_Admin_Bloqueos extends Publisher implements Suscribe {
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
    private ComboBox<String> cbUsuariosRestringidos;

    @FXML
    private Text txtNumeroUsuarios;

    @FXML
    public void initialize() { // idea usarlo como un metodo externo para que vuleva a cargar
        // Aca llenas lo del usuario con un select nombre usuario o nombre where estatus == 2
        cbUsuariosRestringidos.getItems().addAll("Usuario 1", "Usuario 2", "Usuario 3","usuario 5 ");
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
            //Cambia los set estado = 2 where usario = ususario ;
            notificarSubs("Por parte del cuerpo de greenet corporation se nos permite informar que el cliente con nombre de usuario : " + usuario + " ha sido bloqueado ");
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Usuario Bloqueado");
            alert.setContentText("El "+usuario+" Fue bloqueado correctamente");

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


}





// el  deberia tener estas proporciones  356, 65
