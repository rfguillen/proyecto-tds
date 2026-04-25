package umu.tds.proyecto.vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umu.tds.proyecto.Configuracion;
import umu.tds.proyecto.negocio.modelo.Categoria;

import java.time.LocalDate;

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
    	selectorCategoria.getItems().clear();
        selectorCategoria.getItems().add(null); // "Todas" (null)
        
        var categorias = Configuracion.getInstancia()
            .getControladorGastos()
            .getCategoriasDisponibles();
        
        selectorCategoria.getItems().addAll(categorias);
    }
    
    @FXML
    void actionAplicar(ActionEvent event) {
        try {
            LocalDate inicio = dateInicio.getValue();
            LocalDate fin = dateFin.getValue();
            Categoria cat = selectorCategoria.getValue(); // Si es "Todas" será null o lo manejamos
            System.out.println("=== FILTRO ===");
            System.out.println("Cat seleccionada: " + cat);
            System.out.println("Cat nombre: '" + (cat == null ? "null" : cat.getNombre()) + "'");
            
            Double min = null;
            if (!textoImporteMin.getText().isEmpty()) {
            	min = Double.parseDouble(textoImporteMin.getText());
            }
            
            Double max = null;
            if (!textoImporteMax.getText().isEmpty()) {
            	max = Double.parseDouble(textoImporteMax.getText());
            }

            // Validar que inicio no sea posterior a fin
            if (inicio != null && fin != null && inicio.isAfter(fin)) {
                javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.WARNING);
                alerta.setTitle("Fechas incorrectas");
                alerta.setHeaderText(null);
                alerta.setContentText("La fecha de inicio no puede ser posterior a la fecha de fin.");
                alerta.showAndWait();
                return;
            }

            // Validar que min no sea mayor que max
            if (min != null && max != null && min > max) {
                javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.WARNING);
                alerta.setTitle("Importes incorrectos");
                alerta.setHeaderText(null);
                alerta.setContentText("El importe mínimo no puede ser mayor que el máximo.");
                alerta.showAndWait();
                return;
            }
            VentanaPrincipalController.setFiltros(inicio, fin, cat, min, max);
            
            cerrarVentana();
           
            
            
        } catch (NumberFormatException e) {
            System.out.println("Error en formato de números");
        }
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
