module umu.tds.proyecto {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;



    exports umu.tds.proyecto;
    opens umu.tds.proyecto to javafx.fxml;
    opens umu.tds.proyecto.vista to javafx.fxml;
    opens umu.tds.proyecto.adapters.repository.json.dto to com.fasterxml.jackson.databind;
    opens umu.tds.proyecto.negocio.modelo to javafx.base;
    exports umu.tds.proyecto.adapters.repository;
}
