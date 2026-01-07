package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDateTime;

public class Movimiento {
	
	private String concepto;
	private double cantidad;
	private LocalDateTime fecha;
	private Categoria categoria;

	public Movimiento(String concepto, double cantidad, LocalDateTime fecha, Categoria categoria) {
		this.concepto = concepto;
		this.cantidad = cantidad;
		this.fecha = fecha;
		this.categoria = categoria;
		
	}
	
	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public double getCantidadPagadaPor(Participante p) {
		return 0.0;
	}
	
	@Override
	public String toString() {
		return this.getClass().getName()+" [concepto=" + concepto + ", cantidad=" + cantidad + ", fecha=" + fecha + "]";
	}
}