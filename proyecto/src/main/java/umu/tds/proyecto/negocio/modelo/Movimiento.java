package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDateTime;

public class Movimiento {
	
	private final String concepto;
	private final double cantidad;
	private final LocalDateTime fecha;
	private final Categoria categoria;

	public Movimiento(String concepto, double cantidad, LocalDateTime fecha, Categoria categoria) {
		this.concepto = concepto;
		this.cantidad = cantidad;
		this.fecha = fecha;
		this.categoria = categoria;
		
	}

	public String getConcepto() {
		return concepto;
	}

	
	public double getCantidad() {
		return cantidad;
	}

	
	public LocalDateTime getFecha() {
		return fecha;
	}
	
	
	
	public Categoria getCategoria() {
		return categoria;
	}
	

	
	public double getCantidadPagadaPor(Participante p) {
		return 0.0;
	}
	
	@Override
	public String toString() {
		return this.getClass().getName()+" [concepto=" + concepto + ", cantidad=" + cantidad + ", fecha=" + fecha + "]";
	}
}