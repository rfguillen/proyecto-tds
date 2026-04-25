package umu.tds.proyecto.negocio.modelo;

import java.util.Objects;

public class Alerta {

    public enum Periodo { SEMANAL, MENSUAL }

    private final Periodo periodo;
    private final double umbral;
    private final Categoria categoria;
    private final EstrategiaAlerta estrategia;

    public Alerta(Periodo periodo, double umbral, Categoria categoria) {
        this.periodo = Objects.requireNonNull(periodo);
        if (umbral <= 0) throw new IllegalArgumentException("Umbral debe ser > 0");
        this.umbral = umbral;
        this.categoria = categoria;
        this.estrategia = (periodo == Periodo.SEMANAL)
                ? new EstrategiaAlertaSemanal()
                : new EstrategiaAlertaMensual();
    }

    public boolean estaSuperad(java.util.List<Movimiento> movimientos) {
        return estrategia.superaUmbral(movimientos, umbral, categoria);
    }

    public Periodo getPeriodo() { return periodo; }
    public double getUmbral() { return umbral; }
    public Categoria getCategoria() { return categoria; }

    @Override
    public String toString() {
        String cat = (categoria == null) ? "todas las categorías" : categoria.getNombre();
        return periodo + " - " + umbral + "€ en " + cat;
    }
}