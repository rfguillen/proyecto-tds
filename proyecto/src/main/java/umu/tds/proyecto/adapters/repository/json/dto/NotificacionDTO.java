package umu.tds.proyecto.adapters.repository.json.dto;

import java.time.LocalDateTime;

/**
 * DTO de notificación persistida en JSON
 */
public class NotificacionDTO {
    public String mensaje;
    public LocalDateTime fecha;
    public boolean leida;

    public NotificacionDTO() {}
}