module umu.tds.proyecto {
    requires javafx.controls;
    requires javafx.fxml;

    opens umu.tds.proyecto to javafx.fxml;
    exports umu.tds.proyecto;
}
