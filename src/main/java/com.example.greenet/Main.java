package com.example.greenet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {

        DatabaseConnection.inicializarBaseDatosManual();

        // Lanzar aplicación JavaFX
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("\n🚀 Iniciando aplicación GreeNet...");

        // OPCIÓN 1: Si usas FXML

        Parent root = FXMLLoader.load(getClass().getResource("LOGIN.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        /*OPCIÓN 2: Si creas la UI programáticamente
        primaryStage.setTitle("GreeNet - Sistema de Gestión Ambiental");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);

        // Tu código de interfaz aquí



        System.out.println("✅ Interfaz gráfica cargada correctamente");*/
    }

    @Override
    public void stop() throws Exception {
        System.out.println("\n🛑 Cerrando aplicación...");

        // Cerrar consola H2
        DatabaseConnection.cerrarConsolaH2();

        // Cerrar base de datos
        DatabaseConnection.shutdown();

        super.stop();

        System.out.println("✅ Aplicación cerrada correctamente");
    }
}