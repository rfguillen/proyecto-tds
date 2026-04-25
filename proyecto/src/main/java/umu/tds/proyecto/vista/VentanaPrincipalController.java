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
import java.util.List;
import java.util.Optional;

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
    private ListView<String> listaSaldosParticipantes;
    
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
    void actionAnadirGasto(ActionEvent event) {
        // Asumiendo que llamas a otra ventana aquí:
        // Configuracion.getInstancia().getSceneManager().showVistaGastoNuevo(selectorCuenta.getValue());
        
        // 6. Actualizar saldos tras la operación
        aplicarFiltrosYActualizar();
        actualizarSaldosCuentaCompartida();
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
        tablaMovimientos.setItems(FXCollections.observableArrayList(movimientos));
        tablaMovimientos.refresh();
        // Preservar orden seleccionado por el usuario
        if (!tablaMovimientos.getSortOrder().isEmpty()) {
            tablaMovimientos.sort();
        }
    }
    
    private void cargarCuentas() {
        selectorCuenta.getItems().clear();
        List<Cuenta> cuentas = Configuracion.getInstancia().getControladorGastos().getCuentasUsuario();
        selectorCuenta.getItems().addAll(cuentas);
        if(!cuentas.isEmpty()) {
            selectorCuenta.getSelectionModel().selectFirst();
        }
    }

    @FXML
    void actionCrearGrupo(ActionEvent event) {
        Configuracion.getInstancia().getSceneManager().showVistaGrupo();
        cargarCuentas();
        actualizarSaldosCuentaCompartida();
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
                    FXCollections.observableArrayList(notifs));
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
    }
    
}