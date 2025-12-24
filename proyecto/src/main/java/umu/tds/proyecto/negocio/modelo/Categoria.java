package umu.tds.proyecto.negocio.modelo;

import java.util.Objects;

public class Categoria {

	private String nombre;
	
	public Categoria(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Override
	public String toString() {
		return nombre;
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
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Categoria otro = (Categoria) obj;
		return Objects.equals(nombre, otro.nombre);
	}
}
