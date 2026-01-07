package umu.tds.proyecto.negocio.modelo;


public class CuentaPersonal extends Cuenta {
	
	/**
	 * Constructor para una cuenta personal con saldo inicial 0.
	 */
	public CuentaPersonal(String nombre) {
		super(nombre);
	}
	
	/**
	 * Constructor para una cuenta personal con un saldo inicial específico.
	 */
	public CuentaPersonal(String nombre, double saldoInicial) {
		super(nombre, saldoInicial);
	}
	
	/**
	 * En una cuenta personal, el saldo para el usuario es siempre el saldo total de la cuenta,
	 * ya que no hay repartos ni deudas con otros participantes.
	 * 
	 * @param nombreUsuario El nombre del usuario que consulta (se ignora en este tipo de cuenta).
	 * @return El saldo actual de la cuenta.
	 */
	@Override
	public double getSaldoParaUsuario(String nombreUsuario) {
		// Opcional: Podríamos validar que el nombreUsuario coincide con el dueño,
		// pero en este modelo simplificado devolvemos el saldo directamente.
		return this.getSaldo();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	
}
