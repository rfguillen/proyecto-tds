package umu.tds.proyecto.negocio.modelo;

import java.util.Objects;

public class Categoria {

	private String nombre;
	
	public Categoria(String nombre) {
	    this.nombre = nombre == null ? null : nombre.trim();
	}
	
	public String getNombre() {
		return nombre;
	}

	
	
	
	@Override
	public String toString() {
		return nombre;
	}
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (!(obj instanceof Categoria)) return false;
	    Categoria otro = (Categoria) obj;
	    
	    String n1 = this.nombre == null ? null : this.nombre.trim().toLowerCase();
	    String n2 = otro.nombre == null ? null : otro.nombre.trim().toLowerCase();
	    
	    return Objects.equals(n1, n2);
	}

	@Override
	public int hashCode() {
	    String n = nombre == null ? null : nombre.trim().toLowerCase();
	    return Objects.hash(n);
	}
}
