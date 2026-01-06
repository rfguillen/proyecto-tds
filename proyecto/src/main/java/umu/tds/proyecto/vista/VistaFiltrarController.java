package umu.tds.proyecto.vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umu.tds.proyecto.negocio.modelo.Categoria;

public class VistaFiltrarController {

    @FXML
    private DatePicker dateFin;

    @FXML
    private DatePicker dateInicio;

    @FXML
    private ComboBox<Categoria> selectorCategoria;

    @FXML
    private TextField textoImporteMax;

    @FXML
    private TextField textoImporteMin;

    @FXML
    public void initialize() {
    	selectorCategoria.getItems().add(new Categoria("Comida"));
    	selectorCategoria.getItems().add(new Categoria("Ocio"));
    	selectorCategoria.getSelectionModel().selectFirst();
    }
    
    @FXML
    void actionAplicar(ActionEvent event) {
    	// hacer
    	cerrarVentana();
    }

    @FXML
    void actionLimpiar(ActionEvent event) {
    	dateInicio.setValue(null);
    	dateFin.setValue(null);
    	textoImporteMin.clear();
    	textoImporteMax.clear();
    	selectorCategoria.getSelectionModel().selectFirst();
    }
    
    public void cerrarVentana() {
    	Stage stage = (Stage) textoImporteMin.getScene().getWindow();
    	stage.close();
    }
}
