module com.ventaboletas {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.ventaboletas to javafx.fxml;
    opens com.ventaboletas.controller to javafx.fxml;
    opens com.ventaboletas.model to javafx.base;
    exports com.ventaboletas;
}
