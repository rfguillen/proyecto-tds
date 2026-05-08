package umu.tds.proyecto.vista;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import umu.tds.proyecto.Configuracion;
import umu.tds.proyecto.negocio.modelo.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Controlador de la ventana principal
 * 
 * Gestiona eventos de interfaz y delega las operaciones de negocio en 
 * GestionGastos, manteniendo separada la vista del modelo
 */
public class VentanaPrincipalController {

    private static VentanaPrincipalController instance;
    private LocalDate fechaInicio, fechaFin;
    private List<Integer> filtroMeses = new java.util.ArrayList<>();
    private List<Categoria> filtroCategorias = new java.util.ArrayList<>();
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
    private ListView<String> listaSaldosParticipantes;
    
    @FXML
    private ComboBox<Cuenta> selectorCuenta;
    
    @FXML 
    private TableView<Movimiento> tablaMovimientos;
    
    @FXML
    private ListView<Notificacion> listaNotificaciones;

    private enum OrdenTabla {
        FECHA_ASC, FECHA_DESC
    }

    private OrdenTabla ordenActual = OrdenTabla.FECHA_DESC;
    
    @FXML
    public void initialize() {
        instance = this;
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaConcepto.setCellValueFactory(new PropertyValueFactory<>("concepto"));
        columnaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        columnaImporte.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        columnaCategoria.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });

        cargarCuentas();

        selectorCuenta.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                aplicarFiltrosYActualizar();
                actualizarSaldosCuentaCompartida();
                actualizarListaNotificaciones();
            }
        });
        actualizarSaldosCuentaCompartida();
        actualizarListaNotificaciones();
    }
    
    public static void setFiltros(
            LocalDate inicio,
            LocalDate fin,
            List<Integer> meses,
            List<Categoria> categorias,
            Double min,
            Double max) {

        if (instance != null) {
            instance.fechaInicio = inicio;
            instance.fechaFin = fin;

            /*
             * Copiamos las listas para que la ventana principal no dependa
             * directamente de las colecciones internas de la ventana de filtros.
             */
            instance.filtroMeses = meses == null
                    ? new java.util.ArrayList<>()
                    : new java.util.ArrayList<>(meses);

            instance.filtroCategorias = categorias == null
                    ? new java.util.ArrayList<>()
                    : new java.util.ArrayList<>(categorias);

            instance.filtroMin = min;
            instance.filtroMax = max;
            instance.aplicarFiltrosYActualizar();
        }
    }

    
    @FXML
    void actionAnadirGasto(ActionEvent event) {
        Cuenta cuenta = selectorCuenta.getValue();

        if (cuenta == null) {
            System.out.println("Selecciona una cuenta primero.");
            return;
        }

        Configuracion.getInstancia().getSceneManager().showVistaGasto(cuenta);

        aplicarFiltrosYActualizar();
        actualizarSaldosCuentaCompartida();
        actualizarListaNotificaciones();
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
    	        actualizarSaldosCuentaCompartida();
    	        System.out.println("Gasto eliminado.");
    	            
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	    actualizarListaNotificaciones();
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
    	    actualizarSaldosCuentaCompartida();
    	    actualizarListaNotificaciones();
    	}
    
    	public static void limpiarFiltrosGlobales() {
    	    if (instance != null) {
    	        instance.fechaInicio = null;
    	        instance.fechaFin = null;
    	        instance.filtroMeses.clear();
    	        instance.filtroCategorias.clear();
    	        instance.filtroMin = null;
    	        instance.filtroMax = null;
    	        instance.aplicarFiltrosYActualizar();
    	    }
    	}
    
    private void aplicarFiltrosYActualizar() {
        Cuenta cuenta = selectorCuenta.getValue();
        if (cuenta == null) return;

        List<Movimiento> movimientos = Configuracion.getInstancia()
                .getControladorGastos()
                .filtrarMovimientos(cuenta, fechaInicio, fechaFin, filtroMeses, filtroCategorias, filtroMin, filtroMax);

        Comparator<Movimiento> comparador = Comparator.comparing(Movimiento::getFecha);
        if (ordenActual == OrdenTabla.FECHA_DESC) {
            comparador = comparador.reversed();
        }

        movimientos = movimientos.stream()
                .sorted(comparador)
                .toList();

        tablaMovimientos.setItems(FXCollections.observableArrayList(movimientos));
        tablaMovimientos.refresh();
    }

    private void cargarCuentas() {
        selectorCuenta.getItems().clear();
        List<Cuenta> cuentas = Configuracion.getInstancia().getControladorGastos().getCuentasUsuario();
        selectorCuenta.getItems().addAll(cuentas);
        if (!cuentas.isEmpty()) {
            selectorCuenta.getSelectionModel().selectFirst();
            // Forzar actualización aunque el listener no se dispare
            aplicarFiltrosYActualizar();
        }
    }

    @FXML
    void actionCrearGrupo(ActionEvent event) {
        Configuracion.getInstancia().getSceneManager().showVistaGrupo();
        cargarCuentas();
        actualizarSaldosCuentaCompartida();
        actualizarListaNotificaciones();
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
        actualizarSaldosCuentaCompartida();
        actualizarListaNotificaciones();
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
                actualizarSaldosCuentaCompartida();
                actualizarListaNotificaciones();
            });
            
        }).start();
    }
    
    @FXML 
    void actionAnadirAlerta(ActionEvent event) { 
    	Configuracion.getInstancia().getSceneManager().showVistaAlerta(); 
    	actualizarListaNotificaciones();
    }
    
    @FXML
    void actionCambiarCuenta(ActionEvent event) {}
    
    @FXML 
    void actionLimpiarFiltros(ActionEvent event) { 
    	limpiarFiltrosGlobales(); 
    }
    
    @FXML
    void actionVerEstadisticas(ActionEvent event) {
        Cuenta cuenta = selectorCuenta.getValue();
        if (cuenta == null) return;
        Configuracion.getInstancia().getSceneManager().showVistaEstadistica(cuenta);
    }

    public static void actualizarListaNotificaciones() {
        if (instance == null || instance.listaNotificaciones == null) return;

        Platform.runLater(() -> {
            List<Notificacion> notifs = Configuracion.getInstancia()
                    .getControladorGastos()
                    .getNotificaciones();

            instance.listaNotificaciones.setItems(
                    FXCollections.observableArrayList(notifs)
            );
            instance.listaNotificaciones.refresh();
        });
    }
    
    private void actualizarSaldosCuentaCompartida() {
        Cuenta cuenta = selectorCuenta.getValue();

        if (!(cuenta instanceof CuentaCompartida cc)) {
            listaSaldosParticipantes.setItems(FXCollections.observableArrayList());
            return;
        }

        List<String> resumen = cc.getSaldosParticipantes().entrySet().stream()
                .map(entry -> {
                    String nombre = entry.getKey().getNombre();
                    double saldo = entry.getValue();
                    return String.format("%s: %+.2f €", nombre, saldo);
                })
                .toList();

        listaSaldosParticipantes.setItems(FXCollections.observableArrayList(resumen));
        listaSaldosParticipantes.refresh();
    }

    @FXML
    void actionGestionarAlertas(ActionEvent event) {
        Configuracion.getInstancia().getSceneManager().showVistaGestionAlertas();
        actualizarListaNotificaciones();
    }
    
    @FXML
    void actionOrdenFechaAsc(ActionEvent event) {
        ordenActual = OrdenTabla.FECHA_ASC;
        aplicarFiltrosYActualizar();
    }

    @FXML
    void actionOrdenFechaDesc(ActionEvent event) {
        ordenActual = OrdenTabla.FECHA_DESC;
        aplicarFiltrosYActualizar();
    }
    
}