package umu.tds.proyecto.vista;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umu.tds.proyecto.negocio.modelo.Categoria;
import umu.tds.proyecto.App;
import umu.tds.proyecto.Configuracion;
import umu.tds.proyecto.negocio.modelo.Cuenta;
import umu.tds.proyecto.negocio.modelo.Movimiento;

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
