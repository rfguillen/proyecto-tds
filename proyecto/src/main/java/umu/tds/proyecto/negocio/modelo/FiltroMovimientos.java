package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDate;

/**
 * Objeto de filtro para movimientos
 * 
 * Encapsula criterios de filtrado independientes de la vista
 */
public class FiltroMovimientos {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Categoria categoria;
    private Double importeMin;
    private Double importeMax;

    public FiltroMovimientos() {}

    public FiltroMovimientos(LocalDate fechaInicio, LocalDate fechaFin,
                             Categoria categoria, Double importeMin, Double importeMax) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.categoria = categoria;
        this.importeMin = importeMin;
        this.importeMax = importeMax;
    }

    public boolean cumple(Movimiento m) {
        if (fechaInicio != null && m.getFecha().toLocalDate().isBefore(fechaInicio)) return false;
        if (fechaFin != null && m.getFecha().toLocalDate().isAfter(fechaFin)) return false;
        if (categoria != null && !m.getCategoria().equals(categoria)) return false;
        if (importeMin != null && m.getCantidad() < importeMin) return false;
        if (importeMax != null && m.getCantidad() > importeMax) return false;
        return true;
    }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public Categoria getCategoria() { return categoria; }
    public Double getImporteMin() { return importeMin; }
    public Double getImporteMax() { return importeMax; }

    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public void setImporteMin(Double importeMin) { this.importeMin = importeMin; }
    public void setImporteMax(Double importeMax) { this.importeMax = importeMax; }
}