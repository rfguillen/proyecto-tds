package umu.tds.proyecto.vista;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import umu.tds.proyecto.Configuracion;
import umu.tds.proyecto.negocio.modelo.Participante;

public class VistaGrupoController {

    @FXML
    private TableColumn<Participante, String> columnaNombreParticipante;

    @FXML
    private TableColumn<Participante, Double> columnaPorcentaje;

    @FXML
    private TableView<Participante> tablaParticipantes;

    @FXML
    private TextField textoNombreGrupo;

    @FXML
    private TextField textoNombreParticipante;

    @FXML
    private TextField textoPorcentaje;
    
    private ObservableList<Participante> listaParticipantes;
    
    @FXML
    public void initialize() {
    	columnaNombreParticipante.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	columnaPorcentaje.setCellValueFactory(new PropertyValueFactory<>("porcentajeParticipacion"));
    	
    	// Inicializar la lista vacia
    	listaParticipantes = FXCollections.observableArrayList();
    	tablaParticipantes.setItems(listaParticipantes);
    }

    @FXML
    void actionAnadirParticipante(ActionEvent event) {
    	String nombre = textoNombreParticipante.getText();
    	String porcentajePart = textoPorcentaje.getText();
    	
    	if (!nombre.isEmpty()) {
    		double porcentaje = 0.0;
    		try {
    			if (!porcentajePart.isEmpty()) {
    				porcentaje = Double.parseDouble(porcentajePart);
    			}
    		} catch (NumberFormatException e) {}
    		
    		Participante p = new Participante(nombre, porcentaje);
    		listaParticipantes.add(p);
    		
    		// Limpiar los campos
    		textoNombreParticipante.clear();
    		textoPorcentaje.clear();
    		
    	}
    }

    @FXML
    void actionCancelar(ActionEvent event) {
    	cerrarVentana();
    }

    @FXML
    void actionCrearGrupo(ActionEvent event) {
    	String nombreGrupo = textoNombreGrupo.getText();
    	
    	if (nombreGrupo.isEmpty() || listaParticipantes.isEmpty()) {
    		System.out.println("Faltan datos");
    		return;
    	}
    	
    	try {
    		List<Participante> participantes = new ArrayList<>(listaParticipantes);
    		Configuracion.getInstancia().getControladorGastos().crearCuentaCompartida(nombreGrupo, participantes);
    		System.out.println("Grupo " + nombreGrupo + " creado correctamente");
    		
    		cerrarVentana();
    		
    	} catch (Exception e) {
    		System.out.println("Error al crear grupo: " + e.getMessage());
    	}
    }
    
    private void cerrarVentana() {
    	Stage stage = (Stage) textoNombreGrupo.getScene().getWindow();
    	stage.close();
    }
}
