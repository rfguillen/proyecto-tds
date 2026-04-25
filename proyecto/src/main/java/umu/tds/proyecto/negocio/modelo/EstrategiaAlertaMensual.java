package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDate;
import java.util.List;

public class EstrategiaAlertaMensual implements EstrategiaAlerta {
    @Override
    public boolean superaUmbral(List<Movimiento> movimientos, double umbral, Categoria categoria) {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);

        double total = movimientos.stream()
                .filter(m -> !m.getFecha().toLocalDate().isBefore(inicioMes))
                .filter(m -> !m.getFecha().toLocalDate().isAfter(hoy))
                .filter(m -> categoria == null || m.getCategoria().equals(categoria))
                .filter(m -> !m.getCategoria().getNombre().equalsIgnoreCase("Ingreso"))
                .mapToDouble(Movimiento::getCantidad)
                .sum();

        return total >= umbral;
    }
}