package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Cuenta {
	
	private String nombre;
	private LocalDate fechaCreacion;
	protected List<Gasto> gastos;
	
	public Cuenta(String nombre) {
		this.nombre = nombre;
		this.fechaCreacion = LocalDate.now();
		this.gastos = new ArrayList<>();
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}
	
	public List<Gasto> getGastos() {
		return gastos;
	}
	
	public void añadirGasto(Gasto gasto) {
		this.gastos.add(gasto);
	}
	
	public void eliminarGasto(Gasto gasto) {
		this.gastos.remove(gasto);
	}
}
