module socialnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens socialnetwork to javafx.fxml;
    exports socialnetwork;
    exports socialnetwork.controllers;
    opens socialnetwork.controllers to javafx.fxml;
    opens socialnetwork.domain to javafx.base;
}
