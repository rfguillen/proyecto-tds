package umu.tds.proyecto.vista;

import java.io.IOException;
import java.time.LocalDateTime;
import umu.tds.proyecto.negocio.modelo.Categoria;
import umu.tds.proyecto.negocio.modelo.Movimiento;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;


public class VentanaPrincipalController {

    @FXML
    private TableColumn<Movimiento, Categoria> columnaCategoria;

    @FXML
    private TableColumn<Movimiento, String> columnaConcepto;

    @FXML
    private TableColumn<Movimiento, LocalDateTime> columnaFecha;

    @FXML
    private TableColumn<Movimiento, Double> columnaImporte;

    @FXML
    private Label etiquetaSaldoTotal;

    @FXML
    private TextArea panelNotificaciones;

    @FXML
    private ComboBox<String> selectorCuenta;

    @FXML
    private TableView<Movimiento> tablaMovimientos;

    @FXML
    public void initialize() {
    	columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
    	columnaConcepto.setCellValueFactory(new PropertyValueFactory<>("concepto"));
    	columnaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
    	columnaImporte.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    }
    
    
    @FXML
    void actionAbrirFiltros(ActionEvent event) {
    	
    }

    @FXML
    void actionAbrirTerminal(ActionEvent event) {

    }

    @FXML
    void actionAnadirAlerta(ActionEvent event) {

    }

    @FXML
    void actionAnadirGasto(ActionEvent event) {

    }

    @FXML
    void actionCambiarCuenta(ActionEvent event) {

    }

    @FXML
    void actionCrearGrupo(ActionEvent event) {

    }

    @FXML
    void actionImportar(ActionEvent event) {

    }

    @FXML
    void actionLimpiarFiltros(ActionEvent event) {

    }

    @FXML
    void actionVerEstadisticas(ActionEvent event) {

    }

}
