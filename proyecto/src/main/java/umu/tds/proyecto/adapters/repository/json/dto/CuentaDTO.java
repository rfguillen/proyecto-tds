package umu.tds.proyecto.adapters.repository.json.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO de cuenta persistida. Repersenta cuentas personales y compartidas
 */
public class CuentaDTO {
    public String tipo; // "personal" o "compartida"
    public String nombre;
    public List<MovimientoDTO> movimientos;
    public Map<String, Double> porcentajes; // solo si tipo == "compartida"

    public CuentaDTO() {}
}