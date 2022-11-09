module com.example.soutenance2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    requires htmlunit;
    requires sib.api.v3.sdk;
    requires java.sql;
    requires java.desktop;

    opens com.example.soutenance2 to javafx.fxml;
    exports com.example.soutenance2;
}