package com.example.interfazadmin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Vista_Admin_Bloqueos {
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
        cbUsuariosRestringidos.getItems().addAll("Usuariowdhcuiewfuiewufiewuifewubfyuewbfyubfyuebfyuebfyubfyuewbfyuebfyuewfyuvewyuveyuvfhuewjfeyufyuevf1", "Usuario2", "Usuario3","usuario 5 ");
        double h = 3;
        // Aqui si quieres pones lo del select * from usuarios whre id = 1  osea lo de que sena usuarios
        txtNumeroUsuarios.setText(String.valueOf(h));
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
}
// el  deberia tener estas proporciones  356, 65
