package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDateTime;

public class Notificacion {
    
    private final String mensaje;
    private final LocalDateTime fecha;
    private boolean leida;
    
    public Notificacion(String mensaje) {
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
        this.leida = false;
    }
    
    public String getMensaje() { return mensaje; }
    public LocalDateTime getFecha() { return fecha; }
    public boolean isLeida() { return leida; }
    public void marcarLeida() { this.leida = true; }
    
    @Override
    public String toString() {
        return (leida ? "[Leída] " : "[Nueva] ") + mensaje + " (" + fecha.toLocalDate() + ")";
    }
}