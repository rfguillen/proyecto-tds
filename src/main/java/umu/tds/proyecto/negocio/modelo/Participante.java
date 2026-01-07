package umu.tds.proyecto.negocio.modelo;

import java.util.Objects;

public class Participante {
    
    private final String nombre;
    private double porcentajeParticipacion;
    private double saldo;
    
    public Participante(String nombre, double porcentajeParticipacion) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo").trim();
        if (this.nombre.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío");
        setPorcentajeParticipacion(porcentajeParticipacion);
        this.saldo = 0.0;
    }
    
    public String getNombre() { 
    	return nombre; 
    }
    
    public double getPorcentajeParticipacion() { 
    	return porcentajeParticipacion; 
    }
    
    public double getSaldo() { 
    	return saldo; 
    }
    
    protected void setSaldo(double saldo) { 
    	this.saldo = saldo; 
    }

    public void setPorcentajeParticipacion(double porcentajeParticipacion) {
        if (porcentajeParticipacion < 0 || porcentajeParticipacion > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
        }
        this.porcentajeParticipacion = porcentajeParticipacion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase()); 
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Participante)) return false;
        Participante other = (Participante) obj;
        return nombre.equalsIgnoreCase(other.nombre);
    }

    @Override
    public String toString() {
        return nombre;
    }
}