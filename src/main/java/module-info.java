
/** Document that helps to build an application telling what dependencies the module needs */

module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires junit;
    requires org.testng;

    opens client to javafx.fxml;
    exports client;
}