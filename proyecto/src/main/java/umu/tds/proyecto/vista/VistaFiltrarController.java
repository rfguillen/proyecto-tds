package umu.tds.proyecto.vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umu.tds.proyecto.Configuracion;
import umu.tds.proyecto.negocio.modelo.Categoria;

import java.time.LocalDate;
import java.util.List;

public class VistaFiltrarController {

    @FXML
    private DatePicker dateFin;

    @FXML
    private DatePicker dateInicio;

    @FXML
    private ListView<String> listaMeses;

    @FXML
    private ListView<Categoria> listaCategorias;

    @FXML
    private TextField textoImporteMax;

    @FXML
    private TextField textoImporteMin;

    @FXML
    public void initialize() {
        /*
         * Permitimos selección múltiple para poder filtrar
         * por una lista de meses y por una lista de categorías
         */
        listaMeses.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listaCategorias.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        listaMeses.getItems().setAll(
                "Enero",
                "Febrero",
                "Marzo",
                "Abril",
                "Mayo",
                "Junio",
                "Julio",
                "Agosto",
                "Septiembre",
                "Octubre",
                "Noviembre",
                "Diciembre"
        );

        var categorias = Configuracion.getInstancia()
                .getControladorGastos()
                .getCategoriasDisponibles();

        listaCategorias.getItems().setAll(categorias);
    }

    @FXML
    void actionAplicar(ActionEvent event) {
        try {
            LocalDate inicio = dateInicio.getValue();
            LocalDate fin = dateFin.getValue();

            /*
             * Convertimos los índices seleccionados en números de mes.
             * Enero está en índice 0, por eso se suma 1
             */
            List<Integer> meses = listaMeses.getSelectionModel()
                    .getSelectedIndices()
                    .stream()
                    .map(indice -> indice + 1)
                    .toList();

            /*
             * Categorías seleccionadas por el usuario: si no selecciona ninguna,
             * el controlador interpretará que no se aplica filtro de categoría
             */
            List<Categoria> categorias = List.copyOf(
                    listaCategorias.getSelectionModel().getSelectedItems()
            );

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

            VentanaPrincipalController.setFiltros(
                    inicio,
                    fin,
                    meses,
                    categorias,
                    min,
                    max
            );

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
        listaMeses.getSelectionModel().clearSelection();
        listaCategorias.getSelectionModel().clearSelection();
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