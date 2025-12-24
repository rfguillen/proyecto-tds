package umu.tds.proyecto.negocio.modelo;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

	private String nombre;
	private CuentaPersonal cuentaPersonal;
	private List<Cuenta> cuentas;
	
	public Usuario(String nombre) {
		this.nombre = nombre;
		this.cuentas = new ArrayList<>();
		// Al crear un usuario le damos una cuenta personal
		this.cuentaPersonal = new CuentaPersonal("Mis Gastos");
		this.cuentas.add(this.cuentaPersonal);
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<Cuenta> getCuentas() {
		return cuentas;
	}
	
	public void addCuenta(Cuenta cuenta) {
		this.cuentas.add(cuenta);
	}
	
	public CuentaPersonal getCuentaPersonal() {
		return this.cuentaPersonal;
	}
}
