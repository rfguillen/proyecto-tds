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
	
	
	public List<Movimiento> getGastosTotales () {
		List<Movimiento> movimientosUsuario= new ArrayList<Movimiento>();
	
		
		for (Cuenta c : cuentas) {   //recorro las cuentas para sacar los gastos
			List<Movimiento> movimientosCuenta=c.getMovimientos();
			
			for (Movimiento m : movimientosCuenta) {//añado los gastos de cada cuenta
				movimientosUsuario.add(m);
			}
		}
		return movimientosUsuario;
	}
	
	public double getSaldoTotal() {
		double saldo=0;
		
		// Sumar saldo de la cuenta personal
		if (cuentaPersonal != null) {
			saldo += cuentaPersonal.getSaldo();
		}
		
		// Sumar saldo (deudas) en las cuentas compartidas
		// He supuesto que el nombre del participante en el grupo es igual al nombre del usuario
		for (Cuenta c : cuentas) {
			saldo += c.getSaldoParaUsuario(this.nombre);
		}
		
		return saldo;
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
