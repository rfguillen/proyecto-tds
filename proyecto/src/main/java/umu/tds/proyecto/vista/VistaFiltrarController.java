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
            Categoria cat = selectorCategoria.getValue();

            Double min = null;
            if (!textoImporteMin.getText().isBlank()) {
                min = Double.parseDouble(textoImporteMin.getText());
            }

            Double max = null;
            if (!textoImporteMax.getText().isBlank()) {
                max = Double.parseDouble(textoImporteMax.getText());
            }

            if (inicio != null && fin != null && inicio.isAfter(fin)) {
                mostrarError("Fechas incorrectas",
                        "La fecha de inicio no puede ser posterior a la fecha de fin.");
                return;
            }

            if (min != null && max != null && min > max) {
                mostrarError("Importes incorrectos",
                        "El importe mínimo no puede ser mayor que el máximo.");
                return;
            }

            VentanaPrincipalController.setFiltros(inicio, fin, cat, min, max);
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarError("Formato incorrecto",
                    "Los importes deben ser números válidos.");
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
    
    private void mostrarError(String titulo, String mensaje) {
        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
