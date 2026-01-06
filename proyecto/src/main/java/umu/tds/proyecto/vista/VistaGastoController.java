package umu.tds.proyecto.vista;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umu.tds.proyecto.Configuracion;
import umu.tds.proyecto.negocio.modelo.Categoria;
import umu.tds.proyecto.negocio.modelo.CuentaPersonal;
import umu.tds.proyecto.negocio.modelo.Movimiento;

public class VistaGastoController {

    @FXML
    private DatePicker dateFecha;

    @FXML
    private ComboBox<Categoria> selectorCategoria;

    @FXML
    private TextField textoConcepto;

    @FXML
    private TextField textoImporte;

    @FXML
    public void initialize() {
    	// Inicializar fecha a hoy
    	dateFecha.setValue(LocalDate.now());
    	
    	// Cargar categorías, pongo unas de ejemplo por ahora
    	selectorCategoria.getItems().add(new Categoria("Entretenimiento"));
    	selectorCategoria.getItems().add(new Categoria("Comida"));
    	
    	selectorCategoria.getSelectionModel().selectFirst();
    }
    
    @FXML
    void actionCambiarCategoria(ActionEvent event) {
    }

    @FXML
    void actionCancelar(ActionEvent event) {
    	cerrarVentana();
    }

    @FXML
    void actionGuardarGasto(ActionEvent event) {
    	try {
    		// Obtener los datos
    		String concepto = textoConcepto.getText();
    		double importe = Double.parseDouble(textoImporte.getText());
    		LocalDate fecha = dateFecha.getValue();
    		Categoria categoria = selectorCategoria.getValue();
    		
    		// Crear el movimiento
    		LocalDateTime fechaHora = LocalDateTime.of(fecha, LocalTime.now());
    		Movimiento movimiento = new Movimiento(concepto, importe, fechaHora, categoria);
    		
    		// Por defecto usamos la cuenta personal del usuario
    		// TODO: saber en que cuenta estamos, personal o compartida
    		CuentaPersonal cuentaDestino = Configuracion.getInstancia().getControladorGastos().getCuentaGlobal();
    		
    		Configuracion.getInstancia().getControladorGastos().registrarGasto(movimiento, cuentaDestino);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	cerrarVentana();
    }

    @FXML
    void actionNuevaCategoria(ActionEvent event) {
    	// hacer
    }
    
    private void cerrarVentana() {
    	Stage stage = (Stage) textoConcepto.getScene().getWindow();
    	stage.close();
    }
    
}
