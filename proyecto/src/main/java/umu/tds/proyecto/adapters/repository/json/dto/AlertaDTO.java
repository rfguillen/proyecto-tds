package umu.tds.proyecto.adapters.repository.json.dto;

public class AlertaDTO {
    public String periodo; // "SEMANAL" o "MENSUAL"
    public double umbral;
    public String categoria; // null si es global

    public AlertaDTO() {}
}