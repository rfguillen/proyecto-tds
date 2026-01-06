package umu.tds.proyecto.negocio.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Usuario {

	private final String nombre;
	private final CuentaPersonal cuentaPrincipal; // La cuenta base que siempre tiene
	private List<Cuenta> cuentas; // Aquí irán la principal + todas las compartidas/nuevas
	
	public Usuario(String nombre) {
		this.nombre = Objects.requireNonNull(nombre, "El nombre es obligatorio");
		this.cuentas = new ArrayList<>();
		
		// Al crear un usuario, inicializamos su cuenta principal
		this.cuentaPrincipal = new CuentaPersonal("Gastos de " + nombre);
		
		// La añadimos a la lista global de cuentas
		this.addCuenta(this.cuentaPrincipal);
	}
	
	/**
	 * Calcula el saldo sumando lo que el usuario tiene en CADA una de sus cuentas.
	 * Gracias al polimorfismo, no importa si la cuenta es personal o compartida.
	 */
	public double getSaldoTotal() {
		return cuentas.stream()
				.mapToDouble(c -> c.getSaldoParaUsuario(this.nombre))
				.sum();
	}
	
	/**
	 * Obtiene todos los movimientos de todas las cuentas en las que participa.
	 */
	public List<Movimiento> getGastosTotales() {
		List<Movimiento> todos = new ArrayList<>();
		for (Cuenta c : cuentas) {
			todos.addAll(c.getMovimientos());
		}
		return todos;
	}

	/**
	 * Permite al usuario unirse a una nueva cuenta (ej. un grupo de gastos compartido).
	 */
	public void addCuenta(Cuenta cuenta) {
		Objects.requireNonNull(cuenta, "La cuenta no puede ser nula");
		// Evitamos duplicados para no sumar el saldo dos veces 
		if (!this.cuentas.contains(cuenta)) {
			this.cuentas.add(cuenta);
		}
	}
	
	// --- Getters con protección ---

	public String getNombre() {
		return nombre;
	}

	public List<Cuenta> getCuentas() {
		// Protección contra modificaciones externas 
		return Collections.unmodifiableList(cuentas);
	}

	public CuentaPersonal getCuentaPrincipal() {
		return cuentaPrincipal;
	}

	@Override
	public String toString() {
		return this.getClass().getName()+ "[nombre=" + nombre + ", cuentaPrincipal=" + cuentaPrincipal + ", cuentas=" + cuentas + "]";
	}

	}

