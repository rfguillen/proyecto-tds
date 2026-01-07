package umu.tds.proyecto.negocio.importacion;

import umu.tds.proyecto.negocio.modelo.Movimiento;

public class CuentaGasto {
	//Esta clase recoje los gastos junto a su cuenta asociada.
	
	private final String cuenta;
	private final Movimiento gasto;
	
	public CuentaGasto(String cuenta, Movimiento gasto) {
		this.cuenta = cuenta;
		this.gasto = gasto;
	}
	
	public String getNombreCuenta() {
		return cuenta;
	}
	public Movimiento getGasto() {
		return gasto;
	}
	
}