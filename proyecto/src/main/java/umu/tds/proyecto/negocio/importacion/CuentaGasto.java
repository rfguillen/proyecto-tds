package umu.tds.proyecto.negocio.importacion;

import umu.tds.proyecto.negocio.modelo.Gasto;

public class CuentaGasto {
	//Esta clase recoje los gastos junto a su cuenta asociada.
	
	private final String cuenta;
	private final Gasto gasto;
	
	public CuentaGasto(String cuenta, Gasto gasto) {
		this.cuenta = cuenta;
		this.gasto = gasto;
	}
	
	public String getNombreCuenta() {
		return cuenta;
	}
	public Gasto getGasto() {
		return gasto;
	}
	
}

