package umu.tds.proyecto.vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umu.tds.proyecto.negocio.modelo.Categoria;

public class VistaAlertaController {

    @FXML
    private ComboBox<Categoria> selectorCategoria;

    @FXML
    private TextField textoCantidad;

    @FXML
    // terminar
    public void initialize() {
    	selectorCategoria.getItems().add(new Categoria("Comida"));
    	selectorCategoria.getSelectionModel().selectFirst();
    }
    
    @FXML
    void actionCancelar(ActionEvent event) {
    	cerrarVentana();
    }

    @FXML
    void actionGuardar(ActionEvent event) {
    	// hacer
    }
    
    private void cerrarVentana() {
    	Stage stage = (Stage) textoCantidad.getScene().getWindow();
    	stage.close();
    }

}
