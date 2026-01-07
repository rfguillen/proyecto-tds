package umu.tds.proyecto.vista;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import umu.tds.proyecto.Configuracion;
import umu.tds.proyecto.negocio.modelo.Categoria;
import umu.tds.proyecto.negocio.modelo.Cuenta;
import umu.tds.proyecto.negocio.modelo.CuentaCompartida;
import umu.tds.proyecto.negocio.modelo.Movimiento;
import umu.tds.proyecto.negocio.modelo.Notificacion;
import umu.tds.proyecto.negocio.modelo.Participante;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

public class VentanaPrincipalController {

    private static VentanaPrincipalController instance;
    private LocalDate fechaInicio, fechaFin;
    private Categoria filtroCategoria;
    private Double filtroMin, filtroMax;
    
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
    private ComboBox<Cuenta> selectorCuenta;
    
    @FXML 
    private TableView<Movimiento> tablaMovimientos;
    
    @FXML
    private ListView<Notificacion> listaNotificaciones;

    @FXML
    public void initialize() {
        instance = this;
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaConcepto.setCellValueFactory(new PropertyValueFactory<>("concepto"));
        columnaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        columnaImporte.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        
        cargarCuentas();
        
        selectorCuenta.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                aplicarFiltrosYActualizar();
                actualizarSaldo(newVal);
            }
        });
        
    }
    
    public static void setFiltros(LocalDate inicio, LocalDate fin, Categoria cat, Double min, Double max) {
        if (instance != null) {
            instance.fechaInicio = inicio;
            instance.fechaFin = fin;
            instance.filtroCategoria = cat;
            instance.filtroMin = min;
            instance.filtroMax = max;
            instance.aplicarFiltrosYActualizar();
        }
    }
    
 // Añade este método en cualquier parte de VentanaPrincipalController
    public static void mostrarNotificacion(String mensaje) {
        // Platform.runLater asegura que el popup salga aunque la llamada venga de un hilo secundario
        javafx.application.Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Sistema de Alertas");
            alert.setHeaderText("Límite de gasto superado");
            alert.setContentText(mensaje);
            alert.show(); // .show() no bloquea la app, .showAndWait() sí.
        });
    }

    	@FXML
    	    void actionBorrarGasto(ActionEvent event) {
    	        // Obtener movimiento seleccionado en la tabla
    	        Movimiento seleccionado = tablaMovimientos.getSelectionModel().getSelectedItem();
    	        Cuenta cuenta = selectorCuenta.getValue();
    	        
    	        if (seleccionado == null || cuenta == null) {
    	            System.out.println("Selecciona un movimiento y una cuenta primero");
    	            return;
    	        }
    	        
    	        try {
    	            Configuracion.getInstancia().getControladorGastos().eliminarGasto(cuenta, seleccionado);
    	            aplicarFiltrosYActualizar();
    	            actualizarSaldo(cuenta);
    	            System.out.println("Gasto eliminado.");
    	            
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }
    	    }

    	@FXML
    	    void actionModificarGasto(ActionEvent event) {
    	        Movimiento seleccionado = tablaMovimientos.getSelectionModel().getSelectedItem();
    	        Cuenta cuenta = selectorCuenta.getValue();
    	        
    	        if (seleccionado == null || cuenta == null) {
    	            System.out.println("Selecciona un movimiento para editar");
    	            return;
    	        }
    	        
    	        
    	        Configuracion.getInstancia().getSceneManager().showVistaGastoModificar(cuenta, seleccionado);

    	        aplicarFiltrosYActualizar();
    	        actualizarSaldo(cuenta);
    	    }
    
    public static void limpiarFiltrosGlobales() {
        if (instance != null) {
            instance.fechaInicio = null;
            instance.fechaFin = null;
            instance.filtroCategoria = null;
            instance.filtroMin = null;
            instance.filtroMax = null;
            instance.aplicarFiltrosYActualizar();
        }
    }
    
    private void aplicarFiltrosYActualizar() {
        Cuenta cuenta = selectorCuenta.getValue();
        if (cuenta == null) return;
        
        System.out.println("=== ANTES DE FILTRAR ===");
        System.out.println("Total movimientos en cuenta: " + cuenta.getMovimientos().size());
        System.out.println("Filtro categoria: '" + (filtroCategoria == null ? "null" : filtroCategoria.getNombre()) + "'");
        
        List<Movimiento> movimientos = Configuracion.getInstancia()
            .getControladorGastos()
            .filtrarMovimientos(cuenta, fechaInicio, fechaFin, filtroCategoria, filtroMin, filtroMax);
        
        System.out.println("=== DESPUES DE FILTRAR ===");
        System.out.println("Movimientos filtrados: " + movimientos.size());
        
        if (filtroCategoria != null && movimientos.isEmpty()) {
            System.out.println("=== CATEGORIAS REALES EN MOVIMIENTOS ===");
            cuenta.getMovimientos().stream()
                .limit(20)
                .forEach(m -> {
                    Categoria mc = m.getCategoria();
                    System.out.println("  '" + (mc == null ? "null" : mc.getNombre()) + "'");
                });
        }
        
        tablaMovimientos.setItems(FXCollections.observableArrayList(movimientos));
        tablaMovimientos.refresh();
    }
    
    private void cargarCuentas() {
        selectorCuenta.getItems().clear();
        List<Cuenta> cuentas = Configuracion.getInstancia().getControladorGastos().getCuentasUsuario();
        selectorCuenta.getItems().addAll(cuentas);
        if(!cuentas.isEmpty()) {
            selectorCuenta.getSelectionModel().selectFirst();
        }
    }
    
    private void actualizarSaldo(Cuenta cuenta) {
        if(cuenta != null ) {
            String nombreUsuario = Configuracion.getInstancia().getControladorGastos().getCuentaGlobal().getNombre().replace("Gastos de ", "");
            double saldo = cuenta.getSaldoParaUsuario(nombreUsuario);
            etiquetaSaldoTotal.setText(String.format("%.2f €", saldo));
        }
    }

    @FXML
    void actionAnadirGasto(ActionEvent event) {
        Cuenta cuentaSeleccionada = selectorCuenta.getValue();
        Configuracion.getInstancia().getSceneManager().showVistaGasto(cuentaSeleccionada);
        
        // La tabla no se actualizaba automaticamente al añadir gastos
        aplicarFiltrosYActualizar();
        if (cuentaSeleccionada != null) {
        	actualizarSaldo(cuentaSeleccionada);
        }
    }

    @FXML
    void actionCrearGrupo(ActionEvent event) {
        Configuracion.getInstancia().getSceneManager().showVistaGrupo();
        cargarCuentas();
    }

    @FXML
    void actionImportar(ActionEvent event) {
        Configuracion.getInstancia().getSceneManager().showVistaImportador();

        // IMPORTANTE: tras importar, recargar lista de cuentas en el ComboBox
        Cuenta seleccionAntes = selectorCuenta.getValue();
        cargarCuentas();

        // Intentar mantener selección; si ya no existe, seleccionar la global
        if (seleccionAntes != null && selectorCuenta.getItems().contains(seleccionAntes)) {
            selectorCuenta.getSelectionModel().select(seleccionAntes);
        } else {
            // si tienes una "cuenta global" accesible:
            Cuenta global = Configuracion.getInstancia().getControladorGastos().getCuentaGlobal();
            selectorCuenta.getSelectionModel().select(global);
        }

        aplicarFiltrosYActualizar();
        if (selectorCuenta.getValue() != null) actualizarSaldo(selectorCuenta.getValue());
    }

    @FXML 
    void actionAbrirFiltros(ActionEvent event) { 
    	Configuracion.getInstancia().getSceneManager().showVistaFiltrar(); 
    }
    
    @FXML
    void actionAbrirTerminal(ActionEvent event) {
        Cuenta cuenta = selectorCuenta.getValue();
        
        if (cuenta == null) {
            System.out.println("Selecciona una cuenta primero.");
            return;
        }
        
        System.out.println(">>> ABRIENDO TERMINAL PARA: " + cuenta.getNombre() + " <<<");
        System.out.println("(Interactúa desde la consola de Eclipse/IntelliJ)");

        new Thread(() -> {
            VistaTerminal terminal = new VistaTerminal(Configuracion.getInstancia().getControladorGastos(), cuenta);
            terminal.codigo(); 
            
            Platform.runLater(() -> {
                System.out.println("Terminal cerrada. Actualizando datos en pantalla...");
                aplicarFiltrosYActualizar();
                actualizarSaldo(cuenta);
            });
            
        }).start();
    }
    
    @FXML 
    void actionAnadirAlerta(ActionEvent event) { 
    	Configuracion.getInstancia().getSceneManager().showVistaAlerta(); 
    }
    
    @FXML
    void actionCambiarCuenta(ActionEvent event) {}
    
    @FXML 
    void actionLimpiarFiltros(ActionEvent event) { 
    	limpiarFiltrosGlobales(); 
    }
    
    @FXML 
    void actionVerEstadisticas(ActionEvent event) { 
    	Configuracion.getInstancia().getSceneManager().showVistaEstadistica(); 
    }
    
    @FXML
    void actionIngresarDinero(ActionEvent event) {
        Cuenta cuenta = selectorCuenta.getValue();
        if (cuenta == null) return;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ingresar Dinero");
        dialog.setHeaderText("Ingresar saldo en: " + cuenta.getNombre());
        dialog.setContentText("Cantidad:");

        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent()) {
            try {
                double cantidad = Double.parseDouble(result.get().trim());

                if (cuenta instanceof CuentaCompartida cc) {

                    String nombreUsuario = Configuracion.getInstancia()
                        .getControladorGastos()
                        .getCuentaGlobal()
                        .getNombre()
                        .replace("Gastos de ", "")
                        .trim();

                    Optional<Participante> participante = cc.getParticipantes().stream()
                        .filter(p -> p.getNombre() != null && p.getNombre().trim().equalsIgnoreCase(nombreUsuario))
                        .findFirst();

                    if (participante.isPresent()) {
                        cc.ingresarDinero(cantidad, participante.get());
                    } else {
                        System.out.println("No se pudo ingresar: el usuario '" + nombreUsuario + "' no es participante del grupo.");
                        System.out.println("Participantes actuales:");
                        cc.getParticipantes().forEach(p -> System.out.println(" - '" + p.getNombre() + "'"));
                        return; // salimos sin llamar a actualizar
                    }

                } else {
                    // Cuenta personal
                    cuenta.ingresarDinero(cantidad);
                }

                aplicarFiltrosYActualizar();
                actualizarSaldo(cuenta);

            } catch (NumberFormatException e) {
                System.out.println("Cantidad inválida");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}