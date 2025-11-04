module com.greenetaplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.h2database;
    requires java.desktop;
    requires javafx.swing;
    requires jakarta.mail;
    requires twilio;
    opens com.greenet to javafx.fxml;
    exports com.greenet;
}