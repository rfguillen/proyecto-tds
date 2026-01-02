package umu.tds.proyecto.negocio.modelo;

public class CuentaPersonal extends Cuenta {
	
	public CuentaPersonal(String nombre) {
		super(nombre);
	}
	
	@Override
	public double getSaldoParaUsuario(String nombreUsuario) {
		return this.getSaldo();
	}
}
