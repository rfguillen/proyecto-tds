package umu.tds.proyecto.negocio.modelo;

import java.util.Objects;

public class Participante {
	
	private final String nombre; // Inmutable: el nombre de un participante no debería cambiar
	private double porcentajeParticipacion;
	private double saldo;
	
	public Participante(String nombre, double porcentajeParticipacion) {
		// Validación de precondiciones 
		this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo").trim();
		if (this.nombre.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacío");
		
		setPorcentajeParticipacion(porcentajeParticipacion); // Usamos el método para validar
		this.saldo = 0.0;
	}
	
	public String getNombre() {
		return nombre;
	}

	public double getPorcentajeParticipacion() {
		return porcentajeParticipacion;
	}
	
	/**
	 * Mejora: Validación de rango. Un porcentaje debe estar entre 0 y 100.
	 * Se mantiene protected si solo la Cuenta/Grupo debe gestionarlo.
	 */
	protected void setPorcentajeParticipacion(double porcentajeParticipacion) {
		if (porcentajeParticipacion < 0 || porcentajeParticipacion > 100) {
			throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
		}
		this.porcentajeParticipacion = porcentajeParticipacion;
	}
	
	public double getSaldo() {
		return saldo;
	}
	
	/**
	 * Mejora: En lugar de un setter genérico, a veces es mejor tener métodos 
	 * semánticos como 'actualizarSaldo' o 'incrementarSaldo'.
	 */
	protected void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	// --- Métodos de Identidad

	@Override
	public int hashCode() {
		// Es vital que hashCode y equals usen los mismos campos inmutables
		return Objects.hash(nombre.toLowerCase()); 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Participante)) return false;
		Participante other = (Participante) obj;
		// Comparamos ignorando mayúsculas para evitar que "Juan" y "juan" sean distintos
		return nombre.equalsIgnoreCase(other.nombre);
	}

	@Override
	public String toString() {
		return  this.getClass().getName()+" [nombre=" + nombre + ", porcentajeParticipacion=" + porcentajeParticipacion + ", saldo="
				+ saldo + "]";
	}
	

	
	}
