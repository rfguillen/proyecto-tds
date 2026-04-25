package umu.tds.proyecto.vista;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import umu.tds.proyecto.Configuracion;
import umu.tds.proyecto.negocio.modelo.Categoria;
import umu.tds.proyecto.negocio.modelo.Cuenta;
import umu.tds.proyecto.negocio.modelo.Movimiento;
import umu.tds.proyecto.negocio.modelo.CuentaCompartida;
import umu.tds.proyecto.negocio.modelo.GastoCompartido;
import umu.tds.proyecto.negocio.modelo.Participante;

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
    
    @FXML
    private ComboBox<Participante> selectorPagador;

    @FXML
    private Label labelPagador;
    
    public void setCuenta(Cuenta cuenta) {
        this.cuentaDestino = cuenta;
        actualizarSelectorPagador();
    }
    
    @FXML
    public void initialize() {
        dateFecha.setValue(LocalDate.now());
        selectorCategoria.getItems().addAll(
                Configuracion.getInstancia().getControladorGastos().getCategoriasDisponibles()
        );
        selectorCategoria.getSelectionModel().selectFirst();

        labelPagador.setVisible(false);
        labelPagador.setManaged(false);
        selectorPagador.setVisible(false);
        selectorPagador.setManaged(false);
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
        
        if (gasto instanceof GastoCompartido gc) {
            selectorPagador.setValue(gc.getPagador());
        }
    }
   
    @FXML
    void actionGuardarGasto(ActionEvent event) {
        try {
            String concepto = textoConcepto.getText();
            if (concepto.isEmpty() || textoImporte.getText().isEmpty()) {
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

            Cuenta destinoFinal = (cuentaDestino != null)
                    ? cuentaDestino
                    : Configuracion.getInstancia().getControladorGastos().getCuentaGlobal();

            Movimiento nuevoMovimiento;

            if (destinoFinal instanceof CuentaCompartida) {
                Participante pagador = selectorPagador.getValue();
                if (pagador == null) {
                    System.out.println("Error: Debes seleccionar un pagador");
                    return;
                }

                nuevoMovimiento = new GastoCompartido(concepto, importe, fecha, cat, pagador);
            } else {
                nuevoMovimiento = new Movimiento(concepto, importe, fecha, cat);
            }

            if (gastoOriginal != null) {
                Configuracion.getInstancia().getControladorGastos()
                        .modificarGasto(destinoFinal, nuevoMovimiento, gastoOriginal);
            } else {
                Configuracion.getInstancia().getControladorGastos()
                        .registrarGasto(nuevoMovimiento, destinoFinal);
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
    
    private void actualizarSelectorPagador() {
        boolean esCompartida = cuentaDestino instanceof CuentaCompartida;

        labelPagador.setVisible(esCompartida);
        labelPagador.setManaged(esCompartida);
        selectorPagador.setVisible(esCompartida);
        selectorPagador.setManaged(esCompartida);

        if (esCompartida) {
            CuentaCompartida cc = (CuentaCompartida) cuentaDestino;
            selectorPagador.getItems().setAll(cc.getParticipantes());

            if (!selectorPagador.getItems().isEmpty() && selectorPagador.getValue() == null) {
                selectorPagador.getSelectionModel().selectFirst();
            }
        } else {
            selectorPagador.getItems().clear();
        }
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) textoConcepto.getScene().getWindow();
        stage.close();
    }
}