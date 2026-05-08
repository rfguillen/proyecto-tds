package umu.tds.proyecto.vista;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import umu.tds.proyecto.App;
import umu.tds.proyecto.Configuracion;
import umu.tds.proyecto.negocio.modelo.Alerta;

import java.io.IOException;
import java.util.List;

/**
 * Controlador para listar, modificar y eliminar alertas configuradas
 */
public class VistaGestionAlertasController {

    @FXML
    private ListView<Alerta> listaAlertas;

    @FXML
    public void initialize() {
        cargarAlertas();
    }

    private void cargarAlertas() {
        List<Alerta> alertas = Configuracion.getInstancia()
                .getControladorGastos()
                .getAlertas();
        listaAlertas.setItems(FXCollections.observableArrayList(alertas));
    }

    @FXML
    void actionEliminar(ActionEvent event) {
        Alerta seleccionada = listaAlertas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAviso("Selecciona una alerta para eliminar.");
            return;
        }
        Configuracion.getInstancia().getControladorGastos().eliminarAlerta(seleccionada);
        cargarAlertas();
    }

    @FXML
    void actionModificar(ActionEvent event) {
        Alerta seleccionada = listaAlertas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAviso("Selecciona una alerta para modificar.");
            return;
        }
        try {
            FXMLLoader fxml = new FXMLLoader(App.class.getResource("/umu/tds/proyecto/VistaAlerta.fxml"));
            Parent root = fxml.load();
            VistaAlertaController controller = fxml.getController();
            controller.setAlertaAEditar(seleccionada);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modificar Alerta");
            stage.setResizable(false);
            stage.sizeToScene();
            stage.showAndWait();

            cargarAlertas(); // refrescar tras editar
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void actionCerrar(ActionEvent event) {
        Stage stage = (Stage) listaAlertas.getScene().getWindow();
        stage.close();
    }

    private void mostrarAviso(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}