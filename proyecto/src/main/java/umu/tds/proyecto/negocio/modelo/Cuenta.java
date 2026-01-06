package umu.tds.proyecto.negocio.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Cuenta {
	
	private final String nombre; // Inmutable: el nombre de una cuenta no debería cambiar
	private final LocalDate fechaCreacion; // Inmutable: la fecha de creación es fija
	protected List<Movimiento> movimientos;
	protected static final Categoria INGRESO = new Categoria("Ingreso");
	protected static final Categoria GASTO = new Categoria("Gasto");
	protected double saldo;
	
	/**
	 * Constructor principal con saldo inicial.
	 */
	public Cuenta(String nombre, double saldo) {
		// Validación de precondiciones (Sesión 3)
		this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo").trim();
		if (this.nombre.isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede estar vacío");
		}
		if (saldo < 0) {
			throw new IllegalArgumentException("El saldo inicial no puede ser negativo");
		}
		
		this.fechaCreacion = LocalDate.now();
		this.movimientos = new ArrayList<>();
		this.saldo = saldo;
	}
	
	/**
	 * Constructor delegado: saldo inicial = 0.
	 */
	public Cuenta(String nombre) {
		this(nombre, 0.0); // Delegación al constructor principal (evita duplicación)
	}
	
	/**
	 * Ingresa dinero en la cuenta y registra el movimiento.
	 */
	public void ingresarDinero(double dinero) {
		validarCantidadPositiva(dinero, "ingresar");
		
		this.saldo += dinero;
		Movimiento ingreso = new Movimiento(
			"Ingreso de " + String.format("%.2f", dinero) + "€", 
			dinero, 
			LocalDateTime.now(), 
			INGRESO
		);
		movimientos.add(ingreso);
	}
	
	/**
	 * Retira dinero de la cuenta si hay saldo suficiente.
	 * @return true si se pudo retirar, false si no hay fondos suficientes.
	 */
	public boolean sacarDinero(double dinero) {
		validarCantidadPositiva(dinero, "sacar");
		
		if (dinero > saldo) {
			return false; // No hay fondos suficientes
		}
		
		saldo -= dinero;
		Movimiento retiro = new Movimiento(
			"Retirada de " + String.format("%.2f", dinero) + "€", 
			dinero, 
			LocalDateTime.now(), 
			GASTO
		);
		movimientos.add(retiro);
		return true;
	}
	
	/**
	 * Método auxiliar privado para evitar duplicación de código (DRY).
	 */
	private void validarCantidadPositiva(double cantidad, String operacion) {
		if (cantidad <= 0) {
			throw new IllegalArgumentException(
				"La cantidad a " + operacion + " debe ser positiva"
			);
		}
	}

	// --- Getters con protección ---

	public String getNombre() {
		return nombre;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion; // LocalDate es inmutable, no necesita copia defensiva
	}

	/**
	 * Devuelve una lista no modificable para proteger el estado interno (Sesión 2).
	 */
	public List<Movimiento> getMovimientos() {
		return Collections.unmodifiableList(movimientos);
	}

	public double getSaldo() {
		return saldo;
	}

	/**
	 * Setter protegido: solo las subclases pueden modificar el saldo directamente.
	 */
	protected void setSaldo(double saldo) {
		if (saldo < 0) {
			throw new IllegalArgumentException("El saldo no puede ser negativo");
		}
		this.saldo = saldo;
	}
	
	/**
	 * Método abstracto: cada tipo de cuenta calcula el saldo de forma diferente.
	 * - CuentaPersonal: devuelve el saldo total.
	 * - CuentaCompartida: calcula lo que debe/le deben al usuario según los gastos.
	 */
	public abstract double getSaldoParaUsuario(String nombreUsuario);
	
	@Override
	public String toString() {
		return String.format("%s [Creada: %s, Saldo: %.2f€, Movimientos: %d]", 
			nombre, fechaCreacion, saldo, movimientos.size());
	}
	
	@Override
	public int hashCode() {
		// Usamos solo campos inmutables para el hash (Sesión 2)
		return Objects.hash(nombre, fechaCreacion);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Cuenta)) return false;
		Cuenta other = (Cuenta) obj;
		// Dos cuentas son iguales si tienen el mismo nombre y fecha de creación
		return Objects.equals(nombre, other.nombre) 
			&& Objects.equals(fechaCreacion, other.fechaCreacion);
	}
}
	