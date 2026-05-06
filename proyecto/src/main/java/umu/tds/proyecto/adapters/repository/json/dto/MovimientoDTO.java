package umu.tds.proyecto.adapters.repository.json.dto;

import java.time.LocalDateTime;

public class MovimientoDTO {
    public String tipo; // "simple" o "compartido"
    public String concepto;
    public double cantidad;
    public LocalDateTime fecha;
    public String categoria;
    public String pagador; // solo si tipo == "compartido"

    public MovimientoDTO() {}
}