package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDate;

public class Gasto {
	
	private String concepto;
	private double cantidad;
	private LocalDate fecha;
	private Categoria categoria;

	public Gasto(String concepto, double cantidad, LocalDate fecha, Categoria categoria) {
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
	
	public LocalDate getFecha() {
		return fecha;
	}
	
	public void setFecha(LocalDate fecha) {
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
		return "Gasto [concepto=" + concepto + ", cantidad=" + cantidad + ", fecha=" + fecha + "]";
	}
}