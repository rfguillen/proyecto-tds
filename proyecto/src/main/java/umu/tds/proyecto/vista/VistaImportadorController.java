package umu.tds.proyecto.vista;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class VistaImportadorController {

    @FXML
    private TextField textoFichero;

    @FXML
    void actionCancelar(ActionEvent event) {
    	cerrarVentana();
    }

    @FXML
    void actionExaminar(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Seleccionar archivo de datos");
    	
    	fileChooser.getExtensionFilters().addAll(
    			new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"),
    			new FileChooser.ExtensionFilter("Archivos JSON", "*.json"),
    			new FileChooser.ExtensionFilter("Otro Tipo", "*.*"));
    	
    	Stage stage = (Stage) textoFichero.getScene().getWindow();
    	File file = fileChooser.showOpenDialog(stage);
    	
    	if (file != null) {
    		textoFichero.setText(file.getAbsolutePath());
    	}
    }

    @FXML
    void actionImportar(ActionEvent event) {
    	// hacer
    }
    
    private void cerrarVentana() {
    	Stage stage = (Stage) textoFichero.getScene().getWindow();
    	stage.close();
    }

}
