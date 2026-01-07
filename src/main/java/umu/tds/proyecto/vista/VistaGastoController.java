package umu.tds.proyecto.vista;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import umu.tds.proyecto.Configuracion;
import umu.tds.proyecto.negocio.modelo.Categoria;
import umu.tds.proyecto.negocio.modelo.Cuenta;
import umu.tds.proyecto.negocio.modelo.Movimiento;

public class VistaGastoController {

    @FXML 
    private DatePicker dateFecha;
    
    @FXML
    private Movimiento gastoOriginal;
    
    @FXML 
    private ComboBox<Categoria> selectorCategoria;
    
    @FXML 
    private TextField textoConcepto;
    
    @FXML 
    private TextField textoImporte;

    private Cuenta cuentaDestino;
    
    public void setCuenta(Cuenta cuenta) {
    	this.cuentaDestino = cuenta;
    }
    
    @FXML
    public void initialize() {
        dateFecha.setValue(LocalDate.now());
        selectorCategoria.getItems().addAll(Configuracion.getInstancia().getControladorGastos().getCategoriasDisponibles());
        selectorCategoria.getSelectionModel().selectFirst();
    }
    public void setGastoAEditar(Movimiento gasto) {
        this.gastoOriginal = gasto;
        
        // Rellenar campos con los datos 
        if (gasto != null) {
            textoConcepto.setText(gasto.getConcepto());
            textoImporte.setText(String.valueOf(gasto.getCantidad()));
            dateFecha.setValue(gasto.getFecha().toLocalDate());
            selectorCategoria.setValue(gasto.getCategoria());
        }
    }
    @FXML
  
    void actionGuardarGasto(ActionEvent event) {
        try {
            String concepto = textoConcepto.getText();
            if(concepto.isEmpty() || textoImporte.getText().isEmpty()) {
                 System.out.println("Error: Campos vacíos");
                 return;
            }

            double importe = Double.parseDouble(textoImporte.getText());
            Categoria cat = selectorCategoria.getValue();
            
            if (cat == null) {
                System.out.println("Error: Debes seleccionar una categoría");
                return;
            }

            LocalDateTime fecha = LocalDateTime.of(dateFecha.getValue(), LocalTime.now());
            Cuenta destinoFinal;
            if (cuentaDestino != null) {
            	destinoFinal = cuentaDestino;
            } else {
            	destinoFinal = Configuracion.getInstancia().getControladorGastos().getCuentaGlobal();
            }
            
            if (gastoOriginal != null) {
            	// Modificar gasto
            	Movimiento gastoModificado = new Movimiento(concepto, importe, fecha, cat);
            	Configuracion.getInstancia().getControladorGastos().modificarGasto(destinoFinal, gastoModificado, gastoOriginal);
            } else {
            	// Crear gasto
            	Movimiento m = new Movimiento(concepto, importe, fecha, cat);
                Configuracion.getInstancia().getControladorGastos().registrarGasto(m, destinoFinal);
            }

            cerrarVentana();
            
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }

    @FXML
    void actionNuevaCategoria(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Categoría");
        dialog.setHeaderText("Crear nueva categoría");
        dialog.setContentText("Nombre de la categoría:");

        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String nombreCategoria = result.get().trim();
            Categoria nuevaCategoria = Configuracion.getInstancia().getControladorGastos().confirmarCategoria(nombreCategoria);
            
            if (!selectorCategoria.getItems().contains(nuevaCategoria)) {
                selectorCategoria.getItems().add(nuevaCategoria);
            }
            selectorCategoria.getSelectionModel().select(nuevaCategoria);
        }
    }
    
    @FXML 
    void actionCambiarCategoria(ActionEvent event) {}
    
    @FXML 
    void actionCancelar(ActionEvent event) { 
    	cerrarVentana(); 
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) textoConcepto.getScene().getWindow();
        stage.close();
    }
}