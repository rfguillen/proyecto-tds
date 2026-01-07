module umu.tds.proyecto {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;

    opens umu.tds.proyecto to javafx.fxml;
    exports umu.tds.proyecto;
    opens umu.tds.proyecto.vista to javafx.fxml;
    opens umu.tds.proyecto.negocio.modelo to javafx.base;
}
