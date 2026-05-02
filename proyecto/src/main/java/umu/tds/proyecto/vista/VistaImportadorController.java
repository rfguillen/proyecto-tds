package umu.tds.proyecto.vista;

import java.io.File;
import java.nio.file.Path;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import umu.tds.proyecto.Configuracion;

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
        String ruta = textoFichero.getText();
        if (ruta == null || ruta.trim().isEmpty()) return;

        try {
            Configuracion.getInstancia()
                    .getControladorGastos()
                    .importarGastos(Path.of(ruta.trim()));

            cerrarVentana();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void cerrarVentana() {
    	Stage stage = (Stage) textoFichero.getScene().getWindow();
    	stage.close();
    }

}
