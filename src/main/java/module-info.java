module com.example.connect6 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.rmi;

    opens com.example.connect6 to javafx.fxml;
    exports com.example.connect6;
}