package umu.tds.proyecto.vista;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import umu.tds.proyecto.negocio.modelo.Cuenta;
import umu.tds.proyecto.negocio.modelo.Movimiento;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadisticasController {

    @FXML
    private BarChart<String, Number> graficoBarras;

    @FXML
    private PieChart graficoCircular;

    private Cuenta cuenta;

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
        cargarEstadisticas();
    }

    private void cargarEstadisticas() {
        // Solo gastos, excluimos ingresos
        List<Movimiento> gastos = cuenta.getMovimientos().stream()
                .filter(m -> !m.getCategoria().getNombre().equalsIgnoreCase("Ingreso"))
                .collect(Collectors.toList());

        // Agrupar por categoría y sumar
        Map<String, Double> porCategoria = new LinkedHashMap<>();
        for (Movimiento m : gastos) {
            String cat = m.getCategoria().getNombre();
            porCategoria.merge(cat, m.getCantidad(), Double::sum);
        }

        cargarBarras(porCategoria);
        cargarCircular(porCategoria);
    }

    private void cargarBarras(Map<String, Double> porCategoria) {
        graficoBarras.getData().clear();
        graficoBarras.setLegendVisible(false);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Gasto por categoría");

        porCategoria.forEach((cat, total) ->
                serie.getData().add(new XYChart.Data<>(cat, total))
        );

        graficoBarras.getData().add(serie);
    }

    private void cargarCircular(Map<String, Double> porCategoria) {
        graficoCircular.getData().clear();

        List<PieChart.Data> datos = porCategoria.entrySet().stream()
                .map(e -> new PieChart.Data(
                        e.getKey() + String.format(" (%.2f€)", e.getValue()),
                        e.getValue()
                ))
                .collect(Collectors.toList());

        graficoCircular.setData(FXCollections.observableArrayList(datos));
        graficoCircular.setLabelsVisible(true);
    }

    @FXML
    void actionCerrar(ActionEvent event) {
        Stage stage = (Stage) graficoBarras.getScene().getWindow();
        stage.close();
    }
}