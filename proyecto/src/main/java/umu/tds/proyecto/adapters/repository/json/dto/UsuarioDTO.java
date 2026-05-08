package umu.tds.proyecto.adapters.repository.json.dto;

import java.util.List;

/**
 * DTO raíz usado por jackson para persistir el estado principal de la aplicación
 */
public class UsuarioDTO {
    public String nombre;
    public List<CuentaDTO> cuentas;
    public List<AlertaDTO> alertas;
    public List<NotificacionDTO> notificaciones;

    public UsuarioDTO() {}
}