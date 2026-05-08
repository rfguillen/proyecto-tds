package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDate;
import java.util.List;

/**
 * Estrategia concreta para alertas semanales
 */
public class EstrategiaAlertaSemanal implements EstrategiaAlerta {
    @Override
    public boolean superaUmbral(List<Movimiento> movimientos, double umbral, Categoria categoria) {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1);

        double total = movimientos.stream()
                .filter(m -> !m.getFecha().toLocalDate().isBefore(inicioSemana))
                .filter(m -> !m.getFecha().toLocalDate().isAfter(hoy))
                .filter(m -> categoria == null || m.getCategoria().equals(categoria))
                .filter(m -> !m.getCategoria().getNombre().equalsIgnoreCase("Ingreso"))
                .mapToDouble(Movimiento::getCantidad)
                .sum();

        return total >= umbral;
    }
}