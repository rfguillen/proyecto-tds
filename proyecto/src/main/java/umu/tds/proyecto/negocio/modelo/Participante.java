package umu.tds.proyecto.negocio.modelo;

import java.util.Objects;

public class Participante {
	
	private String nombre;
	private double porcentajeParticipacion;
	private double saldo;
	
	public Participante(String nombre, String email, double porcentajeParticipacion) {
		this.nombre = nombre;
		this.porcentajeParticipacion = porcentajeParticipacion;
		this.saldo = 0.0;
	}
	
	public String getNombre() {
		return nombre;
	}

	public double getPorcentajeParticipacion() {
		return porcentajeParticipacion;
	}
	
	public void setProcentajeParticipacion(double porcentajeParticipacion) {
		this.porcentajeParticipacion = porcentajeParticipacion;
	}
	
	public double getSaldo() {
		return saldo;
	}
	
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj instanceof Participante otro) {
			return Objects.equals(this.nombre, otro.nombre);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return nombre;
	}
}
