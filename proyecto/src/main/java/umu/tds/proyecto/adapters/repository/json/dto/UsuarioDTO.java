package umu.tds.proyecto.adapters.repository.json.dto;

import java.util.List;

public class UsuarioDTO {
    public String nombre;
    public List<CuentaDTO> cuentas;
    public List<AlertaDTO> alertas;
    public List<NotificacionDTO> notificaciones;

    public UsuarioDTO() {}
}