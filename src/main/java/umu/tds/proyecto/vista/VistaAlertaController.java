package umu.tds.proyecto.vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umu.tds.proyecto.Configuracion;
import umu.tds.proyecto.negocio.modelo.Alerta;
import umu.tds.proyecto.negocio.modelo.Categoria;

public class VistaAlertaController {

	@FXML
	private ComboBox<Alerta.Periodo> selectorPeriodo;

	@FXML
	private ComboBox<Categoria> selectorCategoria;

	@FXML
	private TextField textoCantidad;

	@FXML
	public void initialize() {
	    // Cargar periodos
	    selectorPeriodo.getItems().addAll(Alerta.Periodo.values());
	    selectorPeriodo.getSelectionModel().selectFirst();

	    // Cargar categorías (con opción "Todas")
	    selectorCategoria.getItems().add(null); // null = todas las categorías
	    selectorCategoria.getItems().addAll(
	        Configuracion.getInstancia().getControladorGastos().getCategoriasDisponibles()
	    );
	    selectorCategoria.getSelectionModel().selectFirst();
	    
	    // Mostrar "Todas" en vez de "null"
	    selectorCategoria.setButtonCell(new javafx.scene.control.ListCell<>() {
	        @Override
	        protected void updateItem(Categoria item, boolean empty) {
	            super.updateItem(item, empty);
	            setText(item == null ? "Todas las categorías" : item.getNombre());
	        }
	    });
	    selectorCategoria.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
	        @Override
	        protected void updateItem(Categoria item, boolean empty) {
	            super.updateItem(item, empty);
	            setText(item == null ? "Todas las categorías" : item.getNombre());
	        }
	    });
	}

	@FXML
	void actionGuardar(ActionEvent event) {
	    try {
	        Alerta.Periodo periodo = selectorPeriodo.getValue(); // Obtener el periodo seleccionado
	        Categoria cat = selectorCategoria.getValue();
	        double cantidad = Double.parseDouble(textoCantidad.getText());

	        // ENVIAR AL MODELO con el periodo real
	        Configuracion.getInstancia().getControladorGastos().crearAlerta(periodo, cantidad, cat);

	        cerrarVentana();
	    } catch (NumberFormatException e) {
	        // Mostrar un aviso si la cantidad no es un número
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setContentText("Por favor, introduce una cantidad válida.");
	        alert.show();
	    }
	}
	@FXML
	void actionCancelar(ActionEvent event) {
	    cerrarVentana();
	}
	    
    private void cerrarVentana() {
    	Stage stage = (Stage) textoCantidad.getScene().getWindow();
    	stage.close();
    }

}
