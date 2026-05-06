package umu.tds.proyecto.adapters.repository.json.dto;

import java.util.List;
import java.util.Map;

public class CuentaDTO {
    public String tipo; // "personal" o "compartida"
    public String nombre;
    public List<MovimientoDTO> movimientos;
    public Map<String, Double> porcentajes; // solo si tipo == "compartida"

    public CuentaDTO() {}
}